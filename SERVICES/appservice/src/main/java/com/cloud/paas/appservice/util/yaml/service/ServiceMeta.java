package com.cloud.paas.appservice.util.yaml.service;

import com.cloud.paas.appservice.util.yaml.common.ObjectMeta;

/**
 * @Author: wyj
 * @desc:
 * @Date: Created in 2017-12-18 20:38
 * @Modified By:
 */
public class ServiceMeta extends ObjectMeta{

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
