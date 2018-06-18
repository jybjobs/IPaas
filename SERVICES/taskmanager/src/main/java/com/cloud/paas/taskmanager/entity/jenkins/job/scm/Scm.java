package com.cloud.paas.taskmanager.entity.jenkins.job.scm;

import javax.xml.bind.annotation.*;

/**
 * @Author: srf
 * @desc: Scm对象
 * @Date: Created in 2018-04-23 10:34
 * @Modified By:
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Scm {
    @XmlAttribute(name = "class")
    private String scmClass;
    @XmlAttribute
    private String plugin;
    private Locations locations;
    private boolean quietOperation;
    private int configVersion;
    private UserRemoteConfigs userRemoteConfigs;
    private Branches branches;

    public String getScmClass() {
        return scmClass;
    }

    public void setScmClass(String scmClass) {
        this.scmClass = scmClass;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public Locations getLocations() {
        return locations;
    }

    public void setLocations(Locations locations) {
        this.locations = locations;
    }

    public boolean isQuietOperation() {
        return quietOperation;
    }

    public void setQuietOperation(boolean quietOperation) {
        this.quietOperation = quietOperation;
    }

    public int getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(int configVersion) {
        this.configVersion = configVersion;
    }

    public UserRemoteConfigs getUserRemoteConfigs() {
        return userRemoteConfigs;
    }

    public void setUserRemoteConfigs(UserRemoteConfigs userRemoteConfigs) {
        this.userRemoteConfigs = userRemoteConfigs;
    }

    public Branches getBranches() {
        return branches;
    }

    public void setBranches(Branches branches) {
        this.branches = branches;
    }
}
