package com.cloud.paas.appservice.util.yaml.scale;

import com.cloud.paas.appservice.util.yaml.common.ObjectMeta;

/**
 * @Author: srf
 * @desc: ScaleMeta对象
 * @Date: Created in 2018-03-27 09-24
 * @Modified By:
 */
public class ScaleMeta extends ObjectMeta {
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
