package com.cloud.paas.taskmanager.entity.jenkins.config;

/**
 * @Author: srf
 * @desc: SonarProperties对象
 * @Date: Created in 2018-04-25 15:39
 * @Modified By:
 */
public class SonarProperties {
    private String projectKey;
    private String projectName;
    private String projectVersion;

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }
}
