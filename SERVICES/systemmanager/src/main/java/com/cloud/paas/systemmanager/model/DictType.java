package com.cloud.paas.systemmanager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
* 字典类型
 */
public class DictType extends ValueObject{
    /**
     * 类型编号   主键
     */
    private Integer dictTypeId;

    /**
     * 类型名   唯一
     */
    @NotBlank(message = "类型名不能为空")
    private String dictTypeName;

    /**
     * 类型标题
     */
    @NotBlank(message = "类型标题不能为空")
    private String title;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 最后修改时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 禁用状态  0 启用 1 禁用
     */
    private Integer disableFlag;

    /**
     * 删除状态 0未删除 1删除
     */
    private Integer delFlag;

    /**
     * 版本（暂未启用）
     */
    private String dictTypeVersion;

    public Integer getDictTypeId() {
        return dictTypeId;
    }

    public void setDictTypeId(Integer dictTypeId) {
        this.dictTypeId = dictTypeId;
    }

    public String getDictTypeName() {
        return dictTypeName;
    }

    public void setDictTypeName(String dictTypeName) {
        this.dictTypeName = dictTypeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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

    public Integer getDisableFlag() {
        return disableFlag;
    }

    public void setDisableFlag(Integer disableFlag) {
        this.disableFlag = disableFlag;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getDictTypeVersion() {
        return dictTypeVersion;
    }

    public void setDictTypeVersion(String dictTypeVersion) {
        this.dictTypeVersion = dictTypeVersion;
    }





}