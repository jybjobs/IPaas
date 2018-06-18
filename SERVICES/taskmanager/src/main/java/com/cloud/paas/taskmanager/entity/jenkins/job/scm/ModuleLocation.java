package com.cloud.paas.taskmanager.entity.jenkins.job.scm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @Author: srf
 * @desc: ModuleLocation对象
 * @Date: Created in 2018-04-23 11:04
 * @Modified By:
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ModuleLocation {
    private String remote;
    @XmlTransient
    private String username;
    @XmlTransient
    private String pwd;
    private String credentialsId;
    private String local;
    private DepthOption depthOption;
    private boolean ignoreExternalsOption;
    private boolean cancelProcessOnExternalsFail;

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCredentialsId() {
        return credentialsId;
    }

    public void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public DepthOption getDepthOption() {
        return depthOption;
    }

    public void setDepthOption(DepthOption depthOption) {
        this.depthOption = depthOption;
    }

    public boolean isIgnoreExternalsOption() {
        return ignoreExternalsOption;
    }

    public void setIgnoreExternalsOption(boolean ignoreExternalsOption) {
        this.ignoreExternalsOption = ignoreExternalsOption;
    }

    public boolean isCancelProcessOnExternalsFail() {
        return cancelProcessOnExternalsFail;
    }

    public void setCancelProcessOnExternalsFail(boolean cancelProcessOnExternalsFail) {
        this.cancelProcessOnExternalsFail = cancelProcessOnExternalsFail;
    }
}
