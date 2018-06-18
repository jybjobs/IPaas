package com.cloud.paas.appservice.util.yaml.hpa;

/**
 * @Author: srf
 * @desc: HPAScaleTargetRef对象
 * @Date: Created in 2018-03-30 14-37
 * @Modified By:
 */
public class HPAScaleTargetRef {
    /**
     * 目标的api版本
     */
    private String apiVersion;
    /**
     * 目标的kind
     */
    private String kind;
    /**
     * 目标的name
     */
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
