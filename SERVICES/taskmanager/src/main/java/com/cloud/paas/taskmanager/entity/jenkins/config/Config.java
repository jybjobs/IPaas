package com.cloud.paas.taskmanager.entity.jenkins.config;

import com.cloud.paas.taskmanager.entity.jenkins.job.properties.Strategy;

/**
 * @Author: srf
 * @desc: CommonConfig对象
 * @Date: Created in 2018-04-25 15:34
 * @Modified By:
 */
public class Config {
    private String dockerImageName;
    private Strategy strategy;
    private String jdk;
    private VersionControlInfo versionControlInfo;
    private String scmTriggerSpec;
    private SonarProperties sonarProperties;
    private boolean ant;
    private MavenInfo mavenInfo;
    private String imageUrl;
    private String command;

    public String getDockerImageName() {
        return dockerImageName;
    }

    public void setDockerImageName(String dockerImageName) {
        this.dockerImageName = dockerImageName;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public String getJdk() {
        return jdk;
    }

    public void setJdk(String jdk) {
        this.jdk = jdk;
    }

    public VersionControlInfo getVersionControlInfo() {
        return versionControlInfo;
    }

    public void setVersionControlInfo(VersionControlInfo versionControlInfo) {
        this.versionControlInfo = versionControlInfo;
    }

    public String getScmTriggerSpec() {
        return scmTriggerSpec;
    }

    public void setScmTriggerSpec(String scmTriggerSpec) {
        this.scmTriggerSpec = scmTriggerSpec;
    }

    public SonarProperties getSonarProperties() {
        return sonarProperties;
    }

    public void setSonarProperties(SonarProperties sonarProperties) {
        this.sonarProperties = sonarProperties;
    }

    public boolean existAnt() {
        return ant;
    }

    public void setAnt(boolean ant) {
        this.ant = ant;
    }

    public MavenInfo getMavenInfo() {
        return mavenInfo;
    }

    public void setMavenInfo(MavenInfo mavenInfo) {
        this.mavenInfo = mavenInfo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
