package com.cloud.paas.taskmanager.entity.jenkins.job.builders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @Author: srf
 * @desc: Maven对象
 * @Date: Created in 2018/5/20 23:25
 * @Modified By:
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Maven {
    private String targets;
    private String mavenName = "3.5.3";// TODO
    private String pom;
    private boolean usePrivateRepository = false;
    private boolean injectBuildVariables = false;

    public String getTargets() {
        return targets;
    }

    public void setTargets(String targets) {
        this.targets = targets;
    }

    public String getMavenName() {
        return mavenName;
    }

    public void setMavenName(String mavenName) {
        this.mavenName = mavenName;
    }

    public String getPom() {
        return pom;
    }

    public void setPom(String pom) {
        this.pom = pom;
    }

    public boolean isUsePrivateRepository() {
        return usePrivateRepository;
    }

    public void setUsePrivateRepository(boolean usePrivateRepository) {
        this.usePrivateRepository = usePrivateRepository;
    }

    public boolean isInjectBuildVariables() {
        return injectBuildVariables;
    }

    public void setInjectBuildVariables(boolean injectBuildVariables) {
        this.injectBuildVariables = injectBuildVariables;
    }
}
