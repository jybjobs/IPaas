package com.cloud.paas.appservice.util.yaml.pod;

import com.cloud.paas.appservice.util.yaml.common.ObjectMeta;

/**
 * @Author: wyj
 * @desc: pod template 描述
 * @Date: Created in 2017-12-14 11:19
 * @Modified By:
 */
public class PodTemplateSpec {
    /**
     * pod template metadata
     */
    private ObjectMeta metadata;
    /**
     * pod详细信息
     */
    private PodSpec spec;

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
}
