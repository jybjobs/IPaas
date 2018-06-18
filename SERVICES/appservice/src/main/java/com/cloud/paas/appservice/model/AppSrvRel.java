package com.cloud.paas.appservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AppSrvRel  extends ValueObject{
    /**
     * 编号
     */
    private Integer relId;

    /**
     * 应用编号
     */
    private Integer appId;

    /**
     * 服务编号
     */
    private Integer srvId;

    /**
     * 启动顺序
     */
    private Integer startOrder;

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
     * 创建人
     */
    private String creator;


    public Integer getRelId() {
        return relId;
    }


    public void setRelId(Integer relId) {
        this.relId = relId;
    }


    public Integer getAppId() {
        return appId;
    }


    public void setAppId(Integer appId) {
        this.appId = appId;
    }


    public Integer getSrvId() {
        return srvId;
    }


    public void setSrvId(Integer srvId) {
        this.srvId = srvId;
    }


    public Integer getStartOrder() {
        return startOrder;
    }


    public void setStartOrder(Integer startOrder) {
        this.startOrder = startOrder;
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