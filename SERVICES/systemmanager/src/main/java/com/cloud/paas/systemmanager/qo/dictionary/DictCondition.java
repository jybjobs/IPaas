package com.cloud.paas.systemmanager.qo.dictionary;

import com.cloud.paas.systemmanager.model.ValueObject;

/**
 * 根据条件（类型名/类型标题/字典名称）查询字典类型及内容的条件
 */

public class DictCondition extends ValueObject {
    private String condition;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
