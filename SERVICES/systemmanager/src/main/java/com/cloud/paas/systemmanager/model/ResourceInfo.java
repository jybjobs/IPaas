package com.cloud.paas.systemmanager.model;

import java.util.Date;

/**
 * @Author: weiwei
 * @Description:总资源详细类
 * @Date: Create  2018/1/11
 * @Modified by:
 */
public class ResourceInfo extends ValueObject{
    /**
     * 资源编号
     */
    private Integer resId;

    /**
     * cpu
     */
    private Float cpu;

    /**
     * 内存
     */
    private Float mem;

    /**
     * 磁盘
     */
    private Float disk;

    /**
     * 网络带宽
     */
    private Float netBandwidth;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改时间
     */
    private Date updateTime;

    /**
     * 以下是get和set方法
     */
    public Integer getResId() {
        return resId;
    }
    public void setResId(Integer resId) {
        this.resId = resId;
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

    public Float getDisk() {
        return disk;
    }
    public void setDisk(Float disk) {
        this.disk = disk;
    }

    public Float getNetBandwidth() {
        return netBandwidth;
    }
    public void setNetBandwidth(Float netBandwidth) {
        this.netBandwidth = netBandwidth;
    }

    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
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
}