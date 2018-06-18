package com.cloud.paas.appservice.model;

import java.util.Date;

public class CtnStorageRelation  extends ValueObject{
    /**
     * 编号
     */
    private Long id;

    /**
     * 容器编号
     */
    private Long ctnId;

    /**
     * 存储类型
     */
    private Integer storageType;

    /**
     * 用途
     */
    private String usage;

    /**
     *目录
     */
    private String path;

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


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getCtnId() {
        return ctnId;
    }


    public void setCtnId(Long ctnId) {
        this.ctnId = ctnId;
    }


    public Integer getStorageType() {
        return storageType;
    }


    public void setStorageType(Integer storageType) {
        this.storageType = storageType;
    }


    public String getUsage() {
        return usage;
    }


    public void setUsage(String usage) {
        this.usage = usage == null ? null : usage.trim();
    }


    public String getPath() {
        return path;
    }


    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
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