package com.cloud.paas.monitor.util.influxentity;

import java.util.List;

/**
 * @Author: srf
 * @desc: Series对象
 * @Date: Created in 2018-04-08 16-44
 * @Modified By:
 */
public class Series {
    private String name;
    private List<String> columns;
    private List<List<String>> values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<List<String>> getValues() {
        return values;
    }

    public void setValues(List<List<String>> values) {
        this.values = values;
    }
}
