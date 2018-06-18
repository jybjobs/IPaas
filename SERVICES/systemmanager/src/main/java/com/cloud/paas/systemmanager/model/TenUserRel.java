package com.cloud.paas.systemmanager.model;

import java.util.Date;

/**
 * @Author: weiwei
 * @Description:租户用户关系类
 * @Date: Create  2018/1/11
 * @Modified by:
 */
public class TenUserRel extends ValueObject{
    /**
     * 关系编号
     */
    private Integer tenantUserRelId;

    /**
     * 租户编号
     */
    private Integer tenantId;

    /**
     * 用户编号
     */
    private Integer userId;

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
    public Integer getTenantUserRelId() {
        return tenantUserRelId;
    }
    public void setTenantUserRelId(Integer tenantUserRelId) {
        this.tenantUserRelId = tenantUserRelId;
    }

    public Integer getTenantId() {
        return tenantId;
    }
    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
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