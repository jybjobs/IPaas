package com.cloud.paas.appservice.util.yaml.pv;

/**
 * Created by 17798 on 2018/6/7.
 */
public class PersistentVolumeSpec {

    private String[] accessModes;

    private Object capacity;

    private CephFSPersistentVolumeSource cephfs;

    private String persistentVolumeReclaimPolicy;

    public String[] getAccessModes() {
        return accessModes;
    }

    public void setAccessModes(String[] accessModes) {
        this.accessModes = accessModes;
    }

    public Object getCapacity() {
        return capacity;
    }

    public void setCapacity(Object capacity) {
        this.capacity = capacity;
    }

    public CephFSPersistentVolumeSource getCephfs() {
        return cephfs;
    }

    public void setCephfs(CephFSPersistentVolumeSource cephfs) {
        this.cephfs = cephfs;
    }

    public String getPersistentVolumeReclaimPolicy() {
        return persistentVolumeReclaimPolicy;
    }

    public void setPersistentVolumeReclaimPolicy(String persistentVolumeReclaimPolicy) {
        this.persistentVolumeReclaimPolicy = persistentVolumeReclaimPolicy;
    }
}
