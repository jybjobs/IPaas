package com.cloud.paas.taskmanager.entity.jenkins.job.scm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class UserRemoteConfigs {
    @XmlElement(name="hudson.plugins.git.UserRemoteConfig")
    private UserRemoteConfig userRemoteConfig;

    public UserRemoteConfig getUserRemoteConfig() {
        return userRemoteConfig;
    }

    public void setUserRemoteConfig(UserRemoteConfig userRemoteConfig) {
        this.userRemoteConfig = userRemoteConfig;
    }
}
