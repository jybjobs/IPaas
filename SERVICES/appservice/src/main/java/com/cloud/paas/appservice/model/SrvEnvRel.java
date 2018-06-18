package com.cloud.paas.appservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class SrvEnvRel extends ValueObject{
    /**
     *id
     */
    private Integer id;
    /**
     *服务id
     */
    private Integer srvId;
    /**
     *变量类型
     */
    private Byte envType;
    /**
     *名称
     */
    private String envKey;
    /**
     *名称描述
     */
    private String envKeyDesc;
    /**
     *值
     */
    private String envValue;
    /**
     *值描述
     */
    private String envValueDesc;
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
    public Byte getEnvType() {
        return envType;
    }
    public void setEnvType(Byte envType) {
        this.envType = envType;
    }
    public String getEnvKey() {
        return envKey;
    }
    public void setEnvKey(String envKey) {
        this.envKey = envKey == null ? null : envKey.trim();
    }
    public String getEnvKeyDesc() {
        return envKeyDesc;
    }
    public void setEnvKeyDesc(String envKeyDesc) {
        this.envKeyDesc = envKeyDesc == null ? null : envKeyDesc.trim();
    }
    public String getEnvValue() {
        return envValue;
    }
    public void setEnvValue(String envValue) {
        this.envValue = envValue == null ? null : envValue.trim();
    }
    public String getEnvValueDesc() {
        return envValueDesc;
    }
    public void setEnvValueDesc(String envValueDesc) {
        this.envValueDesc = envValueDesc == null ? null : envValueDesc.trim();}
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