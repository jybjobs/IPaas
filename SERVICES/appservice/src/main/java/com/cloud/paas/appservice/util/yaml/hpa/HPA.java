package com.cloud.paas.appservice.util.yaml.hpa;

import com.cloud.paas.appservice.util.yaml.common.ObjectBase;
import io.fabric8.kubernetes.api.model.ObjectMeta;

/**
 * @Author: srf
 * @desc: HPA对象
 * @Date: Created in 2018-03-28 14-18
 * @Modified By:
 */
public class HPA extends ObjectBase {
    /**
     * matedata
     */
    private ObjectMeta metadata;
    /**
     * deployment 描述
     */
    private HPASpec spec;

    public ObjectMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(ObjectMeta metadata) {
        this.metadata = metadata;
    }

    public HPASpec getSpec() {
        return spec;
    }

    public void setSpec(HPASpec spec) {
        this.spec = spec;
    }
}
