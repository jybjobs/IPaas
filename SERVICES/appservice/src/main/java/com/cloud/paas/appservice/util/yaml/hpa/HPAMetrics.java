package com.cloud.paas.appservice.util.yaml.hpa;

/**
 * @Author: srf
 * @desc: metrics对象
 * @Date: Created in 2018-03-30 14-29
 * @Modified By:
 */
public class HPAMetrics {
    /**
     * 类型
     */
    private String type;
    /**
     * 资源
     */
    private HPAResource resource;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HPAResource getResource() {
        return resource;
    }

    public void setResource(HPAResource resource) {
        this.resource = resource;
    }
}
