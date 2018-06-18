package com.cloud.paas.taskmanager.entity.jenkins.job.scm;

public class UserRemoteConfig {
    private String url;
    private String credentialsId;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCredentialsId() {
        return credentialsId;
    }

    public void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId;
    }
}
