package com.cloud.paas.appservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class User extends ValueObject{
    /**
     *用户编号
     */
    private Integer userId;
    /**
     *用户名
     */
    private String userNameCh;
    /**
     *用户英文名
     */
    private String userNameEn;
    /**
     *创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     *修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     *创建人
     */
    private String creator;

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getUserNameCh() {
        return userNameCh;
    }
    public void setUserNameCh(String userNameCh) {
        this.userNameCh = userNameCh == null ? null : userNameCh.trim();
    }
    public String getUserNameEn() {
        return userNameEn;
    }
    public void setUserNameEn(String userNameEn) {
        this.userNameEn = userNameEn == null ? null : userNameEn.trim();
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