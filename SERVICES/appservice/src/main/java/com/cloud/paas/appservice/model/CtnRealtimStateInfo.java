package com.cloud.paas.appservice.model;

import java.util.Date;

public class CtnRealtimStateInfo extends ValueObject{
    /**
     * 编号
     */
    private String id;

    /**
     * 容器编号
     */
    private Integer acpCtnId;

    /**
     * 状态
     */
    private Integer ctnStatus;

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


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }


    public Integer getAcpCtnId() {
        return acpCtnId;
    }


    public void setAcpCtnId(Integer acpCtnId) {
        this.acpCtnId = acpCtnId;
    }


    public Integer getCtnStatus() {
        return ctnStatus;
    }


    public void setCtnStatus(Integer ctnStatus) {
        this.ctnStatus = ctnStatus;
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