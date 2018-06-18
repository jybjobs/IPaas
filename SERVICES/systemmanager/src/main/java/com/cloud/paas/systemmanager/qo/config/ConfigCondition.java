package com.cloud.paas.systemmanager.qo.config;

import com.cloud.paas.systemmanager.model.ValueObject;

public class ConfigCondition extends ValueObject {
    private String condition;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
