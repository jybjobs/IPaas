package com.cloud.paas.appservice.util.yaml.common;

/**
 * @Author: srf
 * @desc: StatusCause对象
 * @Date: Created in 2018-03-29 11-03
 * @Modified By:
 */
public class StatusCause {
    //资源字段
    private String field;
    // 失败的消息
    private String message;
    // 原因
    private String reason;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
