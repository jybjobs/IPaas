package com.cloud.paas.appservice.util.yaml.deployment;

import com.cloud.paas.appservice.util.yaml.pod.PodTemplateSpec;

/**
 * @Author: wyj
 * @desc: deployment 详细描述
 * @Date: Created in 2017-12-14 10:53
 * @Modified By:
 */
public class DeploymentSpec {
    /**
     * 副本数量
     */
    private Integer replicas;
    /**
     * deployment label选择器
     */
    private DeploymentSelector selector;
    /**
     * pod template 描述
     */
    private PodTemplateSpec template;
    /**
     * deployment策略
     */
    private DeploymentStrategy strategy;

    /**
     * 新创建的Pod状态为Ready的最短持续时间
     */
    private Integer minReadySeconds;

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }

    public DeploymentSelector getSelector() {
        return selector;
    }

    public void setSelector(DeploymentSelector selector) {
        this.selector = selector;
    }

    public PodTemplateSpec getTemplate() {
        return template;
    }

    public void setTemplate(PodTemplateSpec template) {
        this.template = template;
    }

    public DeploymentStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(DeploymentStrategy strategy) {
        this.strategy = strategy;
    }

    public Integer getMinReadySeconds() {
        return minReadySeconds;
    }

    public void setMinReadySeconds(Integer minReadySeconds) {
        this.minReadySeconds = minReadySeconds;
    }
}
