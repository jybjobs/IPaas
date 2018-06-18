package com.cloud.paas.appservice.model;

import java.util.Date;

public class AppRealtimeStateInfo extends ValueObject{
    /**
     * 编号
     */
    private Integer id;
    /**
     * 应用编号
     */
    private Integer appId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 使用cpu
     */
    private Float cpuUsage;
    /**
     * 使用内存
     */
    private Float memUsage;
    /**
     * 使用磁盘
     */
    private Float diskUsage;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建者
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Float getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Float cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Float getMemUsage() {
        return memUsage;
    }

    public void setMemUsage(Float memUsage) {
        this.memUsage = memUsage;
    }

    public Float getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(Float diskUsage) {
        this.diskUsage = diskUsage;
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