package com.cloud.paas.appservice.util.yaml.pvc;

import com.cloud.paas.appservice.util.yaml.common.ObjectMeta;

/**
 * Created by 17798 on 2018/4/23.
 */
public class PersistentVolumeClaim {

    private String apiVersion;

    private String kind;

    private ObjectMeta metadata;

    private PersistentVolumeClaimSpec spec;

    private PersistentVolumeClaimStatus status;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public ObjectMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(ObjectMeta metadata) {
        this.metadata = metadata;
    }

    public PersistentVolumeClaimSpec getSpec() {
        return spec;
    }

    public void setSpec(PersistentVolumeClaimSpec spec) {
        this.spec = spec;
    }

    public PersistentVolumeClaimStatus getStatus() {
        return status;
    }

    public void setStatus(PersistentVolumeClaimStatus status) {
        this.status = status;
    }
}
