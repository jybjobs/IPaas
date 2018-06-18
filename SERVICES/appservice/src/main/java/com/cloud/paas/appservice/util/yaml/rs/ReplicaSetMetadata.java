package com.cloud.paas.appservice.util.yaml.rs;

import com.cloud.paas.appservice.util.yaml.common.ObjectMeta;
import com.cloud.paas.appservice.util.yaml.common.OwnerReference;

import java.util.*;

/**
 * Created by 17798 on 2018/4/17.
 */
public class ReplicaSetMetadata extends ObjectMeta {

    private String selfLink;

    private String uid;

    private String resourceVersion;

    private Integer generation;

    private List<OwnerReference> ownerReferences;

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public Integer getGeneration() {
        return generation;
    }

    public void setGeneration(Integer generation) {
        this.generation = generation;
    }

    public List<OwnerReference> getOwnerReferences() {
        return ownerReferences;
    }

    public void setOwnerReferences(List<OwnerReference> ownerReferences) {
        this.ownerReferences = ownerReferences;
    }
}
