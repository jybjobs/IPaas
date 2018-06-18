package com.cloud.paas.appservice.util.yaml.rs;

/**
 * ReplicaSetListMetadata对象
 * Created by 17798 on 2018/4/17.
 */
public class ReplicaSetListMetadata {

    private String selfLink;

    private String resourceVersion;

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }
}
