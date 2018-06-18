package com.cloud.paas.appservice.util.yaml.pvc;

import io.fabric8.kubernetes.api.model.LabelSelector;

/**
 * Created by 17798 on 2018/4/23.
 */
public class PersistentVolumeClaimSpec {

    private String[] accessModes;

    private ResourceRequirements resources;

    private LabelSelector selector;

    private String storageClassName;

    private String volumeName;

    public String[] getAccessModes() {
        return accessModes;
    }

    public void setAccessModes(String[] accessModes) {
        this.accessModes = accessModes;
    }

    public ResourceRequirements getResources() {
        return resources;
    }

    public void setResources(ResourceRequirements resources) {
        this.resources = resources;
    }

    public LabelSelector getSelector() {
        return selector;
    }

    public void setSelector(LabelSelector selector) {
        this.selector = selector;
    }

    public String getStorageClassName() {
        return storageClassName;
    }

    public void setStorageClassName(String storageClassName) {
        this.storageClassName = storageClassName;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }
}
