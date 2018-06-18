package com.cloud.paas.appservice.model;

import java.util.Date;

public class AppResourceInfo extends ValueObject{
    /**
     * 编号
     */
    private Integer id;
    /**
     * 应用编号
     */
    private Integer appId;
    /**
     * cpu大小
     */
    private Float cpu;
    /**
     * 内存大小
     */
    private Float mem;
    /**
     * 存储大小
     */
    private Float storage;
    /**
     * 文件系统类型
     */
    private Byte storageType;
    /**
     * 网络
     */
    private Float net;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 创建人
     */
    private String creator;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Float getCpu() {
        return cpu;
    }

    public void setCpu(Float cpu) {
        this.cpu = cpu;
    }

    public Float getMem() {
        return mem;
    }

    public void setMem(Float mem) {
        this.mem = mem;
    }

    public Float getStorage() {
        return storage;
    }

    public void setStorage(Float storage) {
        this.storage = storage;
    }

    public Byte getStorageType() {
        return storageType;
    }

    public void setStorageType(Byte storageType) {
        this.storageType = storageType;
    }

    public Float getNet() {
        return net;
    }

    public void setNet(Float net) {
        this.net = net;
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
        this.creator = creator == null ? null : creator.trim();
    }
}