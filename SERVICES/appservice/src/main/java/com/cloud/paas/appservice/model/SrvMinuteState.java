package com.cloud.paas.appservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


/**
 * @Author: kaiwen
 * @Description: 服务状态历史统计-分钟
 * @Date: Create in  2017/12/5
 */
public class SrvMinuteState extends ValueObject{
    /**
     * 编号
     */
    private Integer id;
    /**
     * 服务编号
     */
    private Integer srvId;
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
     *  使用磁盘
     */
    private Float diskUsage;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 创建者
     */
    private String creator;
    /**
     * set和get 方法
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSrvId() {
        return srvId;
    }

    public void setSrvId(Integer srvId) {
        this.srvId = srvId;
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