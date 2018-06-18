package com.cloud.paas.appservice.util.yaml.deployment;

import com.cloud.paas.appservice.util.yaml.common.ObjectBase;

/**
 * @Author: wyj
 * @desc: Deployment对象
 * @Date: Created in 2017-12-14 10:36
 * @Modified By:
 */
public class Deployment extends ObjectBase{

    /**
     * matedata
     */
    private DeploymentMeta metadata;
    /**
     * deployment 描述
     */
    private DeploymentSpec spec;
    /**
     * deployment status
     */
    private DeploymentStatus status;

    public DeploymentMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(DeploymentMeta metadata) {
        this.metadata = metadata;
    }

    public DeploymentSpec getSpec() {
        return spec;
    }

    public void setSpec(DeploymentSpec spec) {
        this.spec = spec;
    }

    public DeploymentStatus getStatus() {
        return status;
    }

    public void setStatus(DeploymentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Deployment{" +
                "metadata=" + metadata +
                ", spec=" + spec +
                ", status=" + status +
                '}';
    }
}
