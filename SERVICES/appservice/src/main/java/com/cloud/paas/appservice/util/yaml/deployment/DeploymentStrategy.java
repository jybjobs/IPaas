package com.cloud.paas.appservice.util.yaml.deployment;

/**
 * @Author: wyj
 * @desc: deployment策略
 * @Date: Created in 2017-12-14 14:45
 * @Modified By:
 */
public class DeploymentStrategy {
    /**
     * 类型
     */
    private String type;

    private DeploymentRollingUpdate rollingUpdate;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DeploymentRollingUpdate getRollingUpdate() {
        return rollingUpdate;
    }

    public void setRollingUpdate(DeploymentRollingUpdate rollingUpdate) {
        this.rollingUpdate = rollingUpdate;
    }
}
