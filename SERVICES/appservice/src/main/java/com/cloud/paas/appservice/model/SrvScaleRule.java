package com.cloud.paas.appservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class SrvScaleRule extends ValueObject{
    /**
     * 规则编号
     */
    private Integer ruleId;

    /**
     * 服务实例编号
     */
    private Integer srvInstId;

    /**
     * 规则名称（英文）
     */
    private String ruleName;

    /**
     * 服务最大副本数
     */
    private Byte maxSrvInstNum;

    /**
     * 服务规则
     */
    private Byte metric;

    /**
     * 资源平均使用百分比
     */
    private Byte targetAverageUtilization;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    private String creator;

    public Integer getRuleId() {
        return ruleId;
    }
    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }
    public Integer getSrvInstId() {
        return srvInstId;
    }
    public void setSrvInstId(Integer srvInstId) {
        this.srvInstId = srvInstId;
    }
    public Byte getMaxSrvInstNum() {
        return maxSrvInstNum;
    }
    public void setMaxSrvInstNum(Byte maxSrvInstNum) {
        this.maxSrvInstNum = maxSrvInstNum;
    }
    public String getRuleName() {
        return ruleName;
    }
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
    public Byte getMetric() {
        return metric;
    }
    public void setMetric(Byte metric) {
        this.metric = metric;
    }
    public Byte getTargetAverageUtilization() {
        return targetAverageUtilization;
    }
    public void setTargetAverageUtilization(Byte targetAverageUtilization) {
        this.targetAverageUtilization = targetAverageUtilization;
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