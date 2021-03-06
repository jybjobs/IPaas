package com.cloud.paas.monitor.util;

import com.cloud.paas.monitor.util.influxentity.InfluxAverageByTime;

import java.util.List;

/**
 * @Author: srf
 * @desc: CtnState对象
 * @Date: Created in 2018-04-09 14-19
 * @Modified By:
 */
public class MonitorState {
    private String name;

    private List<InfluxAverageByTime> cpuAverages;

    private List<InfluxAverageByTime> memoryAverages;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<InfluxAverageByTime> getCpuAverages() {
        return cpuAverages;
    }

    public void setCpuAverages(List<InfluxAverageByTime> cpuAverages) {
        this.cpuAverages = cpuAverages;
    }

    public List<InfluxAverageByTime> getMemoryAverages() {
        return memoryAverages;
    }

    public void setMemoryAverages(List<InfluxAverageByTime> memoryAverages) {
        this.memoryAverages = memoryAverages;
    }
}
