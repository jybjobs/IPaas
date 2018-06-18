package com.cloud.paas.appservice.util.yaml.pod;

import com.cloud.paas.appservice.util.yaml.common.ObjectMeta;

/**
 * @Author: srf
 * @desc: Pod对象
 * @Date: Created in 2018-04-09 16-37
 * @Modified By:
 */
public class Pod {
    private ObjectMeta metadata;
    private PodSpec spec;
    private PodStatus status;

    public ObjectMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(ObjectMeta metadata) {
        this.metadata = metadata;
    }

    public PodSpec getSpec() {
        return spec;
    }

    public void setSpec(PodSpec spec) {
        this.spec = spec;
    }

    public PodStatus getStatus() {
        return status;
    }

    public void setStatus(PodStatus status) {
        this.status = status;
    }
}
