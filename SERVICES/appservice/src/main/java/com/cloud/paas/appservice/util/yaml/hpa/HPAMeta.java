package com.cloud.paas.appservice.util.yaml.hpa;

/**
 * @Author: srf
 * @desc: HPAMeta对象
 * @Date: Created in 2018-03-28 14-19
 * @Modified By:
 */
public class HPAMeta {
    /**
     * meta名字
     */
    private String name;
    /**
     * k8s命名空间
     */
    private String namespace;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
