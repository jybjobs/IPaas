package com.cloud.paas.monitor.util.influxentity;

import java.util.Date;

/**
 * @Author: srf
 * @desc: InfluxCPUUsageRate对象
 * @Date: Created in 2018-04-08 10-34
 * @Modified By:
 */
public class InfluxAverageByTime {
    private String time;
    private String average;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }
}
