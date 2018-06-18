package com.cloud.paas.monitor.util;

import com.alibaba.fastjson.JSON;
import com.cloud.paas.monitor.util.configuration.Unit;
import com.cloud.paas.monitor.util.influxentity.InfluxAverageByTime;
import com.cloud.paas.monitor.util.influxentity.MeanByTimeResponse;
import com.cloud.paas.configuration.PropertiesConfUtil;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.rest.RestClient;
import com.cloud.paas.util.rest.RestConstant;
import com.cloud.paas.util.result.Result;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cloud.paas.util.timezone.TimeZoneUtil.*;

/**
 * @Author: srf
 * @desc: InfluxDBOperationUtil对象
 * @Date: Created in 2018-04-09 14-05
 * @Modified By:
 */
public class InfluxDBOperationUtil {
    private volatile static InfluxDBOperationUtil influxDBOperationUtil = null;

    private InfluxDBOperationUtil() {
    }

    public static InfluxDBOperationUtil getinstance() {
        if (influxDBOperationUtil == null) {
            synchronized (InfluxDBOperationUtil.class) {
                if (influxDBOperationUtil == null) {
                    influxDBOperationUtil = new InfluxDBOperationUtil();
                }
            }
        }
        return influxDBOperationUtil;
    }

    /**
     * 求Deployment一段时间的平均值
     *
     * @param monitorState
     * @param app
     * @param limits
     * @param startTime
     * @param endTime
     * @param interval
     * @param unit
     * @return
     */
    public Result queryDeployStateByPeriod(MonitorState monitorState, String app, Map<String, String> limits, String startTime, String endTime, int interval, Unit unit) {
        Result result = CodeStatusUtil.resultByCodeEn("DEPLOY_STATE_QUERY_SUCCESS");
        Result cpuResult = queryDeployCPUAverageUsageRate(app, startTime, endTime, interval, unit, convertCpuQuantity(limits.get("cpu")));
        Result memoryResult = queryDeployMemoryAverageUsageRate(app, startTime, endTime, interval, unit, convertMemoryQuantity(limits.get("memory")));
        result = verifyAverageQuery(result, monitorState, cpuResult, memoryResult);
        return result;
    }

    /**
     * @Description
     * @param cpuStr cpu的带单位数值
     * @result cpu的统一数值
     */
    private double convertCpuQuantity(String cpuStr) {
        if (cpuStr.contains("m")) {
            return Double.parseDouble(cpuStr.replace("m", ""));
        } else {
            return 1000 * Double.parseDouble(cpuStr);
        }
    }

    /**
     * @Description
     * @param memStr 内存的带单位数值
     * @result 内存的统一数值
     */
    private double convertMemoryQuantity(String memStr) {
        if (memStr.contains("Gi")) {
            return 1024 * 1024 * 1024 * Double.parseDouble(memStr.replace("Gi", ""));
        } else if (memStr.contains("Mi")){
            return 1024 * 1024 * Double.parseDouble(memStr.replace("Mi", ""));
        } else if (memStr.contains("Ki")){
            return 1024 * Double.parseDouble(memStr.replace("Ki", ""));
        } else {
            return Double.parseDouble(memStr);
        }
    }

    /**
     * 设置容器的平均值列表
     *
     * @param monitorState
     * @param podName
     * @param limits
     * @param startTime
     * @param endTime
     * @param interval
     * @param unit
     * @return
     */
    public Result queryCtnStateByPeriod(MonitorState monitorState, String podName, Map<String, String> limits, String startTime, String endTime, int interval, Unit unit) {
        Result result = CodeStatusUtil.resultByCodeEn("CTN_STATE_QUERY_SUCCESS");
        Result cpuResult = queryPodCPUAverageUsageRate(podName, startTime, endTime, interval, unit, convertCpuQuantity(limits.get("cpu")));
        Result memoryResult = queryPodMemoryAverageUsageRate(podName, startTime, endTime, interval, unit, convertMemoryQuantity(limits.get("memory")));
        result = verifyAverageQuery(result, monitorState, cpuResult, memoryResult);
        return result;
    }

    private Result verifyAverageQuery(Result result, MonitorState monitorState, Result cpuResult, Result memoryResult) {
        if (cpuResult.getSuccess() == 0) {
            result = cpuResult;
        } else if (memoryResult.getSuccess() == 0) {
            result = memoryResult;
        } else {
            monitorState.setCpuAverages((List<InfluxAverageByTime>) cpuResult.getData());
            monitorState.setMemoryAverages((List<InfluxAverageByTime>) memoryResult.getData());
            result.setData(monitorState);
        }
        return result;
    }

    /**
     * 求Deployment的CPU的平均利用率
     */
    private Result queryDeployCPUAverageUsageRate(String app, String startTime, String endTime, int interval, Unit unit, double limits) {
        return queryDeployAverageUsageRate(app, "cpu/usage_rate", startTime, endTime, interval, unit, limits);
    }

    /**
     * 求Deployment的Memory的平均利用率
     */
    private Result queryDeployMemoryAverageUsageRate(String app, String startTime, String endTime, int interval, Unit unit, double limits) {
        return queryDeployAverageUsageRate(app, "memory/usage", startTime, endTime, interval, unit, limits);
    }

    /**
     * 求Pod的CPU的平均利用率
     */
    private Result queryPodCPUAverageUsageRate(String name, String startTime, String endTime, int interval, Unit unit, double limits) {
        return queryPodAverageUsageRate(name, "cpu/usage_rate", startTime, endTime, interval, unit, limits);
    }

    /**
     * 求Pod的Memory的平均利用率
     */
    private Result queryPodMemoryAverageUsageRate(String name, String startTime, String endTime, int interval, Unit unit, double limits) {
        return queryPodAverageUsageRate(name, "memory/usage", startTime, endTime, interval, unit, limits);
    }

    /**
     * 查询Deployment的平均利用率
     */
    private Result queryDeployAverageUsageRate(String app, String measure, String startTime, String endTime, int interval, Unit unit, double limits) {
        // 1.查询一段时间的统计平均值
        Result getMeanByTimeResult = getDeployMeanByTime(app, measure, startTime, endTime, interval + unit.toString());
        if (getMeanByTimeResult.getSuccess() == 0) {
            return getMeanByTimeResult;
        }
        //2.求平均利用率
        return averageList((MeanByTimeResponse) getMeanByTimeResult.getData(), limits);
    }

    /**
     * 查询单个Pod的平均利用率
     */
    private Result queryPodAverageUsageRate(String name, String measure, String startTime, String endTime, int interval, Unit unit, double limits) {
        //1.查询获取一段时间的统计数据
        Result getMeanByTimeResult = getPodMeanByTime(name, measure, startTime, endTime, interval + unit.toString());
        if (getMeanByTimeResult.getSuccess() == 0) {
            return getMeanByTimeResult;
        }
        //2.求平均利用率
        return averageList((MeanByTimeResponse) getMeanByTimeResult.getData(), limits);
    }

    private Result getDeployMeanByTime(String app, String measure, String startTime, String endTime, String interval) {
        // 1.调用influxDB查询接口
        Result response = influxDBQueryByLabels("mean(\"value\")", measure, app, startTime, endTime, interval);
        if (response.getSuccess() == 0) {
            return response;
        }
        // 2.将json串转换为SumByTimeResponse对象
        MeanByTimeResponse meanByTimeResponse = JSON.parseObject((String) response.getData(), MeanByTimeResponse.class);
        response.setData(meanByTimeResponse);
        return response;
    }

    /**
     * 获取Pod一段时间的统计数据
     *
     * @param name Pod名， startTime 开始时间， endTime 结束时间， interval 颗粒度
     * @return influxSumByTimes 统计列表
     */
    private Result getPodMeanByTime(String name, String measure, String startTime, String endTime, String interval) {
        // 1.调用influxDB查询接口
        Result response = influxDBQueryByName("mean(\"value\")", measure, name, startTime, endTime, interval);
        if (response.getSuccess() == 0) {
            return response;
        }
        // 2.将json串转换为MeanByTimeResponse对象
        MeanByTimeResponse meanByTimeResponse = JSON.parseObject((String) response.getData(), MeanByTimeResponse.class);
        response.setData(meanByTimeResponse);
        return response;
    }

    /**
     * 获取平局值
     *
     * @param meanByTimeResponse 一段时间的统计数据, period 时间跨度, interval 颗粒度
     * @return
     */
    private Result averageList(MeanByTimeResponse meanByTimeResponse, double limits) {
        Result result = CodeStatusUtil.resultByCodeEn("AVERAGE_GET_SUCCESS");
        // 1.获取Values部分
        if (meanByTimeResponse.getResults().get(0).getSeries() == null)
            return CodeStatusUtil.resultByCodeEn("INFLUXDB_NO_DATA");
        ;
        List<List<String>> values = meanByTimeResponse.getResults().get(0).getSeries().get(0).getValues();
        // 2.将Values部分装换为influxSumByTimes列表
        Result influxAveragesByTimeResult = changeValuesToInfluxSumByTimeList(values, limits, result);
        result = influxAveragesByTimeResult;
        return result;
    }

    /**
     * 将Values部分转换为influxSumByTimes列表的方法
     *
     * @param values,interval
     * @return influxSumByTimes列表
     */
    private Result changeValuesToInfluxSumByTimeList(List<List<String>> values, double limits, Result result) {
        List<InfluxAverageByTime> influxSumByTimes = new ArrayList<>();
        for (List<String> value : values) {
            InfluxAverageByTime influxSumByTime = new InfluxAverageByTime();
            if (value.get(1) == null) value.set(1, "0");
            influxSumByTime.setAverage(calcAverage(value.get(1), limits));
            try {
                influxSumByTime.setTime(changeToCST(value.get(0)));
            } catch (ParseException e) {
                result = CodeStatusUtil.resultByCodeEn("TIME_FORMAT_ERROR");
                return result;
            }
            influxSumByTimes.add(influxSumByTime);
        }
        result.setData(influxSumByTimes);
        return result;
    }

    /**
     * 计算平均值
     *
     * @param value
     * @param limits
     * @return
     */
    private String calcAverage(String value, double limits) {
        String averageStr;
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.applyPattern("0");
        double average = 100 * Double.parseDouble(value) / limits;
        averageStr = decimalFormat.format(average) + "%";
        return averageStr;
    }

    /**
     *
     * @param select SELECT语句
     * @param measurement 表名
     * @param app app
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param interval 颗粒度
     * @return
     */
    public Result influxDBQueryByLabels(String select, String measurement, String app, String startTime, String endTime, String interval){
        Result result = CodeStatusUtil.resultByCodeEn("INFLUXDB_QUERY_SUCCESS");
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_INFLUXDB);
        url = url.replace(" AND \"pod_name\" = '{name}'","");
        String labels = "app:" + app;
        url = url.replace("{labels}", labels);
        return influxDBQuery(url, result, select, measurement, startTime, endTime, interval);
    }

    /**
     *
     * @param select SELECT语句
     * @param measurement 表名
     * @param name Pod名
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param interval 颗粒度
     * @return
     */
    public Result influxDBQueryByName(String select, String measurement, String name, String startTime, String endTime, String interval){
        Result result = CodeStatusUtil.resultByCodeEn("INFLUXDB_QUERY_SUCCESS");
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_INFLUXDB);
        url = url.replace(" AND \"labels\" =~ /{labels}/","");
        url = url.replace("{name}", name);
        return influxDBQuery(url, result, select, measurement, startTime, endTime, interval);
    }

    /**
     * 查询操作
     * @param url
     * @return
     */
    public Result influxDBQuery(String url, Result result, String select, String measurement, String startTime, String endTime, String interval) {
        url = url.replace("{select}", select);
        url = url.replace("{measurement}", measurement);
        url = url.replace("{nameSpace}", PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_NAMESPACES));
        String startStr = null;
        String endStr = null;
        try {
            startStr = changeToUTC(startTime);
            endStr = changeToUTC(endTime);
        } catch (ParseException e) {
            result = CodeStatusUtil.resultByCodeEn("TIME_FORMAT_ERROR");
            return result;
        }
        url = url.replace("{startTime}", startStr);
        url = url.replace("{endTime}", endStr);
        url = url.replace("{interval}", interval);
        String response = RestClient.doGet(url);
        result.setData(response);
        return result;
    }
}
