package com.cloud.paas.systemmanager.qo.tenant;

import com.cloud.paas.systemmanager.model.ValueObject;


/**
 * 租户管理条件查询
 */
public class TenantCondition extends ValueObject {
    private  String condition;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
