package com.cloud.paas.appservice.util.yaml.rs;

import java.util.List;

/**
 * ReplicaSetList 对象
 * Created by 17798 on 2018/4/17.
 */
public class ReplicaSetList {

    private String kind;

    private String apiVersion;

    private ReplicaSetListMetadata metadata;

    private List<ReplicaSet> items;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public ReplicaSetListMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ReplicaSetListMetadata metadata) {
        this.metadata = metadata;
    }

    public List<ReplicaSet> getItems() {
        return items;
    }

    public void setItems(List<ReplicaSet> items) {
        this.items = items;
    }
}
