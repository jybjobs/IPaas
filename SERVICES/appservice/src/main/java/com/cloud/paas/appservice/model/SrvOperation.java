package com.cloud.paas.appservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class SrvOperation extends ValueObject{
    /**
     *服务操作id
     */
    private Integer srvOperationId;
    /**
     *服务id
     */
    private Integer srvId;
    /**
     *操作类型
     */
    private Byte operationType;
    /**
     *操作描述
     */
    private String operationDesc;

    /**
     *状态
     */
    private Long state;
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
    public Integer getSrvOperationId() {
        return srvOperationId;
    }
    public void setSrvOperationId(Integer srvOperationId) {
        this.srvOperationId = srvOperationId;
    }
    public Integer getSrvId() {
        return srvId;
    }
    public void setSrvId(Integer srvId) {
        this.srvId = srvId;
    }
    public Byte getOperationType() {
        return operationType;
    }
    public void setOperationType(Byte operationType) {
        this.operationType = operationType;
    }
    public String getOperationDesc() {
        return operationDesc;
    }
    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc == null ? null : operationDesc.trim();
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
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