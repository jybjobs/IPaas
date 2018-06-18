package com.cloud.paas.imageregistry.qo;

import com.cloud.paas.imageregistry.model.ValueObject;

/**
 * Created by kaiwen on 2018/1/9.
 */
public class ConditionQuery extends ValueObject {
    private String condition;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
