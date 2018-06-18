package com.cloud.paas.appservice.util.yaml.common;

/**
 * @Author: wyj
 * @desc: 返回状态的对象
 * @Date: Created in 2017-12-19 15:16
 * @Modified By:
 */
public class Status {
    // 类型
    private String kind;
    // api版本
    private String apiVersion;
    // 状态
    private String status;
    // 失败的消息
    private String message;
    // 状态码
    private Integer code;
    // 原因
    private String reason;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
