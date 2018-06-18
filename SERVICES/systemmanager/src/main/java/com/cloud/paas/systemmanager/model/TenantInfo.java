package com.cloud.paas.systemmanager.model;

import java.util.Date;

/**
 * @Author: weiwei
 * @Description:租户详细类
 * @Date: Create  2018/1/11
 * @Modified by:
 */
public class TenantInfo extends ValueObject{
    /**
     * 租户编号
     */
    private Integer tenantId;

    /**
     * 租户名
     */
    private String tenantName;

    /**
     * cpu配额
     */
    private Float cpuQuota;

    /**
     * 内存配额
     */
    private Float memQuota;

    /**
     * 磁盘配额
     */
    private Float diskQuota;

    /**
     * 管理员编号
     */
    private Integer managerUserId;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后更新时间
     */
    private Date updateTime;

    /**
     * 以下是get和set方法
     */
    public Integer getTenantId() {
        return tenantId;
    }
    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName == null ? null : tenantName.trim();
    }

    public Float getCpuQuota() {
        return cpuQuota;
    }
    public void setCpuQuota(Float cpuQuota) {
        this.cpuQuota = cpuQuota;
    }

    public Float getMemQuota() {
        return memQuota;
    }
    public void setMemQuota(Float memQuota) {
        this.memQuota = memQuota;
    }

    public Float getDiskQuota() {
        return diskQuota;
    }
    public void setDiskQuota(Float diskQuota) {
        this.diskQuota = diskQuota;
    }

    public Integer getManagerUserId() {
        return managerUserId;
    }
    public void setManagerUserId(Integer managerUserId) {
        this.managerUserId = managerUserId;
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