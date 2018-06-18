package com.cloud.paas.appservice.util.yaml.deployment;

import java.util.List;

/**
 * @Author: wyj
 * @desc: deployment状态
 * @Date: Created in 2017-12-14 10:57
 * @Modified By:
 */
public class DeploymentStatus {

    private String observedGeneration;
    /**
     * 副本数量
     */
    private String replicas;
    /**
     * 在更新的副本数量
     */
    private String updatedReplicas;
    /**
     * 可用的副本数量
     */
    private String availableReplicas;
    /**
     * deployment详细状态
     */
    private List<DeploymentCondition> conditions;

    public String getObservedGeneration() {
        return observedGeneration;
    }

    public void setObservedGeneration(String observedGeneration) {
        this.observedGeneration = observedGeneration;
    }

    public String getReplicas() {
        return replicas;
    }

    public void setReplicas(String replicas) {
        this.replicas = replicas;
    }

    public String getUpdatedReplicas() {
        return updatedReplicas;
    }

    public void setUpdatedReplicas(String updatedReplicas) {
        this.updatedReplicas = updatedReplicas;
    }

    public String getAvailableReplicas() {
        return availableReplicas;
    }

    public void setAvailableReplicas(String availableReplicas) {
        this.availableReplicas = availableReplicas;
    }

    public List<DeploymentCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<DeploymentCondition> conditions) {
        this.conditions = conditions;
    }
}
