package com.cloud.paas.monitor.util.influxentity;

import java.util.List;

/**
 * @Author: srf
 * @desc: Results对象
 * @Date: Created in 2018-04-08 16-42
 * @Modified By:
 */
public class Results {
    private Integer statement_id;
    private List<Series> series;

    public Integer getStatement_id() {
        return statement_id;
    }

    public void setStatement_id(Integer statement_id) {
        this.statement_id = statement_id;
    }

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }
}
