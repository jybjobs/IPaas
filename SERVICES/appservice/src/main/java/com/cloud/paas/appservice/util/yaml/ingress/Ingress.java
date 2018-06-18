package com.cloud.paas.appservice.util.yaml.ingress;

import com.cloud.paas.appservice.util.yaml.common.ObjectBase;

/**
 * @Author: wyj
 * @desc: ingress实体类
 * @Date: Created in 2018-01-03 18:47
 * @Modified By:
 */
public class Ingress extends ObjectBase {

    /**
     * ingress信息
     */
    private IngressMeta metadata;

    /**
     * ingress详细描述
     */
    private IngressSpec spec;


    public IngressMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(IngressMeta metadata) {
        this.metadata = metadata;
    }

    public IngressSpec getSpec() {
        return spec;
    }

    public void setSpec(IngressSpec spec) {
        this.spec = spec;
    }
}
