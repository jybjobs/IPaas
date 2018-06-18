package com.cloud.paas.appservice.util.yaml.rs;

import com.cloud.paas.appservice.util.yaml.common.Status;

/**
 * Created by 17798 on 2018/4/17.
 */
public class ReplicaSet {

    private ReplicaSetMetadata metadata;

    private ReplicaSetSpec spec;

    private Status status;

    public ReplicaSetMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ReplicaSetMetadata metadata) {
        this.metadata = metadata;
    }

    public ReplicaSetSpec getSpec() {
        return spec;
    }

    public void setSpec(ReplicaSetSpec spec) {
        this.spec = spec;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
