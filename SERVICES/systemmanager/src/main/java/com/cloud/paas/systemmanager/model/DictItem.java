package com.cloud.paas.systemmanager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
/**
 * 字典内容
 */
public class DictItem extends ValueObject{
    /**
     * 字典编号
     */
    private Integer dictItemId;

    /**
     * 对应类型编号
     */
    private Integer dictTypeId;

    /**
     * 字典名称
     */
    @NotBlank(message = "内容名称不能为空")
    private String dictItemName;

    /**
     * 字典编码
     */
    @NotBlank(message = "字典编码不能为空")
    private String dictItemCode;

    /**
     * 字典排序
     */
    private Integer sort;

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
     * 删除状态 0未删除 1删除
     */
    private Integer delFlag;

    public Integer getDictItemId() {
        return dictItemId;
    }

    public void setDictItemId(Integer dictItemId) {
        this.dictItemId = dictItemId;
    }

    public Integer getDictTypeId() {
        return dictTypeId;
    }

    public void setDictTypeId(Integer dictTypeId) {
        this.dictTypeId = dictTypeId;
    }

    public String getDictItemName() {
        return dictItemName;
    }

    public void setDictItemName(String dictItemName) {
        this.dictItemName = dictItemName;
    }

    public String getDictItemCode() {
        return dictItemCode;
    }

    public void setDictItemCode(String dictItemCode) {
        this.dictItemCode = dictItemCode;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}