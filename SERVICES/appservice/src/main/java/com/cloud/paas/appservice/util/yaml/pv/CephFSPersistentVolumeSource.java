package com.cloud.paas.appservice.util.yaml.pv;

/**
 * Created by 17798 on 2018/6/7.
 */
public class CephFSPersistentVolumeSource {

    private String[] monitors;

    private String path;

    private Boolean readOnly;

    private SecretReference secretRef;

    private String user;

    public String[] getMonitors() {
        return monitors;
    }

    public void setMonitors(String[] monitors) {
        this.monitors = monitors;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public SecretReference getSecretRef() {
        return secretRef;
    }

    public void setSecretRef(SecretReference secretRef) {
        this.secretRef = secretRef;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
