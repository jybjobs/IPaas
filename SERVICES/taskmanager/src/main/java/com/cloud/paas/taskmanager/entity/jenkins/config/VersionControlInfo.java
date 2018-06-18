package com.cloud.paas.taskmanager.entity.jenkins.config;

public class VersionControlInfo {
    private VersionControl type;
    private String path;
    private String branch;

    public VersionControl getType() {
        return type;
    }

    public void setType(VersionControl type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
