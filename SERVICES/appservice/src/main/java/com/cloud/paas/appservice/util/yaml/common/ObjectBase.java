package com.cloud.paas.appservice.util.yaml.common;

/**
 * @Author: wyj
 * @desc: api版本和类型的基本信息
 * @Date: Created in 2017-12-14 14:52
 * @Modified By:
 */
public class ObjectBase {

    /**
     * api版本
     */
    private String apiVersion;
    /**
     * 类型
     */
    private String kind;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
