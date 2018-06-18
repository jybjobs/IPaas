package com.cloud.paas.appservice.util.yaml.pv;

/**
 * Created by 17798 on 2018/6/7.
 */
public class SecretReference {

    private String name;

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
