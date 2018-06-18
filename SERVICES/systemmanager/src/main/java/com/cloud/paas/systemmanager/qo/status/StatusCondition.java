package com.cloud.paas.systemmanager.qo.status;

import com.cloud.paas.systemmanager.model.ValueObject;

/**
 * @author: hzy
 * @desc: 状态码查询条件
 * @Date: Created in 2018-01-08 18:28
 * @modified By:
 **/

public class StatusCondition extends ValueObject {
    //状态码查询条件（状态码/状态信息）
    private String condition;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
