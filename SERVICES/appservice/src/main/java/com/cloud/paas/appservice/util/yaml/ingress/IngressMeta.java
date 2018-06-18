package com.cloud.paas.appservice.util.yaml.ingress;

/**
 * @Author: wyj
 * @desc:
 * @Date: Created in 2018-01-03 18:50
 * @Modified By:
 */
public class IngressMeta {

    /**
     * ingress名称
     */
    private String name;
    /**
     * 命名空间
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
