package com.cloud.paas.appservice.util.yaml.pv;

import com.cloud.paas.appservice.util.yaml.common.ObjectBase;
import com.cloud.paas.appservice.util.yaml.common.ObjectMeta;

/**
 * Created by 17798 on 2018/6/7.
 */
public class PersistentVolume extends ObjectBase {

    public final static String PERSISTENT_VOLUME_RECLAIM_POLICY = "Recycle";

    private ObjectMeta metadata;

    private PersistentVolumeSpec spec;

    private PersistentVolumeStatus status;

    public ObjectMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(ObjectMeta metadata) {
        this.metadata = metadata;
    }

    public PersistentVolumeSpec getSpec() {
        return spec;
    }

    public void setSpec(PersistentVolumeSpec spec) {
        this.spec = spec;
    }

    public PersistentVolumeStatus getStatus() {
        return status;
    }

    public void setStatus(PersistentVolumeStatus status) {
        this.status = status;
    }
}
