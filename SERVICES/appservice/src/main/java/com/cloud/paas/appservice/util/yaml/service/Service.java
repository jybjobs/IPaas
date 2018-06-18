package com.cloud.paas.appservice.util.yaml.service;

import com.cloud.paas.appservice.util.yaml.common.ObjectBase;

/**
 * @Author: wyj
 * @desc: service对象
 * @Date: Created in 2017-12-14 14:56
 * @Modified By:
 */
public class Service extends ObjectBase {
    /**
     * service元数据
     */
    private ServiceMeta metadata;
    /**
     * service 详情
     */
    private ServiceSpec spec;
    /**
     * service 状态
     */
    private ServiceStatus status;

    public ServiceMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(ServiceMeta metadata) {
        this.metadata = metadata;
    }

    public ServiceSpec getSpec() {
        return spec;
    }

    public void setSpec(ServiceSpec spec) {
        this.spec = spec;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }
}
