package com.cloud.paas.monitor.util.influxentity;

import java.util.List;

/**
 * @Author: srf
 * @desc: InfluxResponse对象
 * @Date: Created in 2018-04-08 16-39
 * @Modified By:
 */
public class MeanByTimeResponse {
    private List<Results> results;

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }
}
