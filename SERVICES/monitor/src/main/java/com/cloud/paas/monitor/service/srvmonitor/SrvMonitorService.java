package com.cloud.paas.monitor.service.srvmonitor;

import com.cloud.paas.monitor.model.ValueObject;
import com.cloud.paas.monitor.qo.HistoryCondition;
import com.cloud.paas.monitor.service.BaseService;
import com.cloud.paas.util.result.Result;

/**
 * @author: zcy
 * @desc: 服务监控service
 * @Date: Created in 2018-01-12 14:58
 * @modified By:
 **/
public interface SrvMonitorService extends BaseService<ValueObject>{
    public Result getSrvState(int userid,int srvid);

    public Result getDeployState(String deployName);

    public Result getDeployStateByPeriod(String deployName, String starttime, String endtime, int interval , String unit);

    public Result getCtnStateWithCtnName(String podName);

    public Result getCtnStateByPeriodWithCtnName(String podName, String starttime, String endtime, int interval , String unit);

    public Result getHistroy(int userid, int srvid, HistoryCondition historyCondition);
}
