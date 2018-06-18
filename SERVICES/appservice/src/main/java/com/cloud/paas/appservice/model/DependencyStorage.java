package com.cloud.paas.appservice.model;

import java.util.Date;

/**
 * 依赖资源-存储
 */
public class DependencyStorage extends ValueObject{

    /**
     * 存储编号
     */
    private Integer acpDependencyStorageId;

    /**
     * 挂载的目录
     */
    private String mountDir;

    /**
     * 存储空间
     */
    private String storage;

    /**
     * pv名称
     */
    private String pvName;

    /**
     * pv路径
     */
    private String pvPath;

    /**
     * pv状态
     */
    private String pvStatus;

    /**
     * pvc名称
     */
    private String pvcName;

    /**
     * pvc状态
     */
    private String pvcStatus;

    /**
     * 部署名称
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

    public Integer getAcpDependencyStorageId() {
        return acpDependencyStorageId;
    }

    public void setAcpDependencyStorageId(Integer acpDependencyStorageId) {
        this.acpDependencyStorageId = acpDependencyStorageId;
    }

    public String getMountDir() {
        return mountDir;
    }

    public void setMountDir(String mountDir) {
        this.mountDir = mountDir;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getPvName() {
        return pvName;
    }

    public void setPvName(String pvName) {
        this.pvName = pvName;
    }

    public String getPvPath() {
        return pvPath;
    }

    public void setPvPath(String pvPath) {
        this.pvPath = pvPath;
    }

    public String getPvStatus() {
        return pvStatus;
    }

    public void setPvStatus(String pvStatus) {
        this.pvStatus = pvStatus;
    }

    public String getPvcName() {
        return pvcName;
    }

    public void setPvcName(String pvcName) {
        this.pvcName = pvcName;
    }

    public String getPvcStatus() {
        return pvcStatus;
    }

    public void setPvcStatus(String pvcStatus) {
        this.pvcStatus = pvcStatus;
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