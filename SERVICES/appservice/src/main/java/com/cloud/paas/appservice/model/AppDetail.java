package com.cloud.paas.appservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AppDetail extends ValueObject{
    /**
     *应用编号
     */
    private Integer appId;

    /**
     *应用名称
     */
    @NotBlank(message = "{apps.appNameZh.notBlank}")
    private String appNameZh;

    /**
     * 应用英文名
     */
    @NotBlank(message = "{apps.appNameEn.notBlank}")
    private String appNameEn;

    /**
     * 应用描述
     */
    private String remark;

    /**
     * 应用类型
     */
    @NotNull(message = "{apps.appType.notNull}")
    private Integer appType;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 最后修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建者
     */
    private String creator;


    public Integer getAppId() {
        return appId;
    }


    public void setAppId(Integer appId) {
        this.appId = appId;
    }


    public String getAppNameZh() {
        return appNameZh;
    }


    public void setAppNameZh(String appNameZh) {
        this.appNameZh = appNameZh == null ? null : appNameZh.trim();
    }


    public String getAppNameEn() {
        return appNameEn;
    }


    public void setAppNameEn(String appNameEn) {
        this.appNameEn = appNameEn == null ? null : appNameEn.trim();
    }


    public String getRemark() {
        return remark;
    }


    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }


    public Integer getAppType() {
        return appType;
    }


    public void setAppType(Integer appType) {
        this.appType = appType;
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