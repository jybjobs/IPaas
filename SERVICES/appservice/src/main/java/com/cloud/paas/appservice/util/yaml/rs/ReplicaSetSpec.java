package com.cloud.paas.appservice.util.yaml.rs;

import io.fabric8.kubernetes.api.model.PodTemplate;

/**
 * Created by 17798 on 2018/4/17.
 */
public class ReplicaSetSpec {

    private Integer replicas;

    private ReplicaSetSelector selector;

    private PodTemplate template;

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }

    public ReplicaSetSelector getSelector() {
        return selector;
    }

    public void setSelector(ReplicaSetSelector selector) {
        this.selector = selector;
    }

    public PodTemplate getTemplate() {
        return template;
    }

    public void setTemplate(PodTemplate template) {
        this.template = template;
    }
}
