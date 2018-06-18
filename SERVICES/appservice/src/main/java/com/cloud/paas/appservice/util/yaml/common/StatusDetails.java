package com.cloud.paas.appservice.util.yaml.common;

/**
 * @Author: srf
 * @desc: StatusDetails对象
 * @Date: Created in 2018-03-29 11-05
 * @Modified By:
 */
public class StatusDetails {
    //原因
    private StatusCause[] causes;
    //组
    private String group;
    // 类型
    private String kind;
    //名称
    private String name;
    //重试时间
    private Integer retryAfterSeconds;
    //UID
    private String uid;

    public StatusCause[] getCauses() {
        return causes;
    }

    public void setCauses(StatusCause[] causes) {
        this.causes = causes;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRetryAfterSeconds() {
        return retryAfterSeconds;
    }

    public void setRetryAfterSeconds(Integer retryAfterSeconds) {
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
