package com.cloud.paas.monitor.service.impl.srvmonitor;

import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.monitor.dao.BaseDAO;
import com.cloud.paas.monitor.model.ValueObject;
import com.cloud.paas.monitor.qo.HistoryCondition;
import com.cloud.paas.monitor.service.impl.BaseServiceImpl;
import com.cloud.paas.monitor.service.srvmonitor.SrvMonitorService;
import com.cloud.paas.monitor.util.MonitorState;
import com.cloud.paas.monitor.util.InfluxDBOperationUtil;
import com.cloud.paas.monitor.util.configuration.Unit;
import com.cloud.paas.configuration.PropertiesConfUtil;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.rest.RestClient;
import com.cloud.paas.util.rest.RestConstant;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: zcy
 * @desc: 服务状态service实现类
 * @Date: Created in 2018-01-12 15:14
 * @modified By:
 **/
@SuppressWarnings({"serial", "rawtypes", "unchecked"})
@Service("SrvMonitorService")
public class SrvMonitorServiceImpl extends BaseServiceImpl<ValueObject> implements SrvMonitorService {
    private static final Logger logger = LoggerFactory.getLogger(SrvMonitorServiceImpl.class);

    @Override
    public BaseDAO<ValueObject> getBaseDAO() {
        return null;
    }

    @Override
    public Result getSrvState(int userid, int srvid) {
        Result result = new Result(1, "10100", "查询成功", 3, 3, null);
        // TODO: 2018/1/12
        return result;
    }


    /**
     * 求deployment状态
     */
    @Override
    public Result getDeployState(String deployName) {
        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Result result = getDeployStateByPeriod(deployName, df.format(dt), df.format(dt), 1, "m");
        return result;
    }

    private Result getDeployInfo(int srvInstId) {
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_DEPLOYMENT_INFO) + srvInstId;
        String resultStr = RestClient.doGet(url);
        Result result = JSONObject.parseObject(resultStr, Result.class);
        return result;
    }

    /**
     * 求deployment一段时间的状态
     */
    @Override
    public Result getDeployStateByPeriod(String deployName, String starttime, String endtime, int interval, String unitStr) {
        Result result;
        // 1.设置app
        String app = deployName;
        // 2.检查粒度单位
        Unit unit = Unit.getName(unitStr);
        if (unit == null) {
            result = CodeStatusUtil.resultByCodeEn("INTERVAL_FORMAT");
            return result;
        }
        // 3.获取limits
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_LIMITS_BY_DEPLOYMENT) + deployName;
        result = JSONObject.parseObject(RestClient.doGet(url), Result.class);
        if (result.getSuccess() == 0) return result;
        Map<String, String> limits = getLimits((JSONObject) result.getData());
        // 4.求平均值
        result = new Result(1, "10100", "查询成功", 3, 3, null);
        MonitorState monitorState = new MonitorState();
        monitorState.setName(deployName);
        Result deployStateResult = InfluxDBOperationUtil.getinstance().queryDeployStateByPeriod(monitorState, app, limits, starttime, endtime, interval, unit);
        if(deployStateResult.getSuccess() == 0) return deployStateResult;
        result.setData(deployStateResult.getData());
        return result;
    }

    /**
     * 使用labels求Pod名称
     */
    private Result listCtnByAppAndVersion(String app, String version) {
        Result result;
        String resultStr;
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_PODNAME_BY_LABELS);
        url = url.replace("{app}", app);
        url = url.replace("{version}", version);
        resultStr = RestClient.doGet(url);
        result = JSONObject.parseObject(resultStr, Result.class);
        return result;
    }

    /**
     * 求平均值
     */
    private Map<String, String> getLimits(JSONObject json) {
        Map<String, String> map = new HashMap<>();
        map.put("cpu", json.getString("cpu"));
        map.put("memory", json.getString("memory"));
        return map;
    }

    @Override
    public Result getCtnStateWithCtnName(String podName){
        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Result result = getCtnStateByPeriodWithCtnName(podName, df.format(dt), df.format(dt), 1, "m");
        return result;
    }

    @Override
    public Result getCtnStateByPeriodWithCtnName(String podName, String starttime, String endtime, int interval, String unitStr) {
        // 1.检测Pod存在
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_LIMITS_BY_POD);
        url = url.replace("{name}", podName);
        String resultStr = RestClient.doGet(url);
        Result result = JSONObject.parseObject(resultStr, Result.class);
        if (result.getSuccess() == 0) {
            result = CodeStatusUtil.resultByCodeEn("POD_EXIST_FALSE");
            return result;
        } else {
            // 2.获取limits
            Map<String, String> limits = getLimits((JSONObject) result.getData());
            // 3.求平均值
            return getCtnStateByPeriodWithCtnName(podName, starttime, endtime, interval, unitStr, limits);
        }
    }

    private Result getCtnStateByPeriodWithCtnName(String podName, String starttime, String endtime, int interval, String unitStr, Map<String, String> limits) {
        Result result = new Result(1, "10100", "查询成功", 3, 3, null);
        // 1.检查粒度单位
        Unit unit = Unit.getName(unitStr);
        if (unit == null) {
            result = CodeStatusUtil.resultByCodeEn("INTERVAL_FORMAT");
            return result;
        }
        // 2.求平均值
        MonitorState monitorState = new MonitorState();
        monitorState.setName(podName);
        Result ctnStateResult = InfluxDBOperationUtil.getinstance().queryCtnStateByPeriod(monitorState, podName, limits, starttime, endtime, interval, unit);
        if(ctnStateResult.getSuccess() == 0) return ctnStateResult;
        result.setData(ctnStateResult.getData());
        return result;
    }

    @Override
    public Result getHistroy(int userid, int srvid, HistoryCondition historyCondition) {
        Result result = new Result(1, "10100", "查询成功", 3, 3, null);
        // TODO: 2018/1/12
        return result;
    }
}
