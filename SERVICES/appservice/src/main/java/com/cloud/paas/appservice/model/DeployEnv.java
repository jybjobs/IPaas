package com.cloud.paas.appservice.model;

import java.util.Date;

public class DeployEnv extends ValueObject {

    /**
     * 部署环境变量
     */
    private Integer deployEnvId;

    /**
     * 环境变量key
     */
    private String deployEnvKey;

    /**
     * 环境变量值
     */
    private String deployEnvValue;

    /**
     * 环境变量描述
     */
    private String deployEnvDesc;

    /**
     * 部署编号
     */
    private Integer deploymentId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private String creator;

    public Integer getDeployEnvId() {
        return deployEnvId;
    }

    public void setDeployEnvId(Integer deployEnvId) {
        this.deployEnvId = deployEnvId;
    }

    public String getDeployEnvKey() {
        return deployEnvKey;
    }

    public void setDeployEnvKey(String deployEnvKey) {
        this.deployEnvKey = deployEnvKey;
    }

    public String getDeployEnvValue() {
        return deployEnvValue;
    }

    public void setDeployEnvValue(String deployEnvValue) {
        this.deployEnvValue = deployEnvValue;
    }

    public String getDeployEnvDesc() {
        return deployEnvDesc;
    }

    public void setDeployEnvDesc(String deployEnvDesc) {
        this.deployEnvDesc = deployEnvDesc;
    }

    public Integer getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(Integer deploymentId) {
        this.deploymentId = deploymentId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}