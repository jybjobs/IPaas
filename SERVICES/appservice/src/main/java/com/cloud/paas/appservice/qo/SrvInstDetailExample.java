package com.cloud.paas.appservice.qo;

import com.cloud.paas.appservice.model.SrvInstDetail;

public class SrvInstDetailExample extends SrvInstDetail {

    private Integer srvId;

    private Integer currentSrvInstId;

    /**
     * 操作类型
     */
    private Integer opType;

    private Integer updateType;

    private String condition;

    private String srvNameEn;

    private String appNameEn;

    private Integer status;

    private Integer noHistory;

    public Integer getSrvId() {
        return srvId;
    }

    public void setSrvId(Integer srvId) {
        this.srvId = srvId;
    }

    public Integer getCurrentSrvInstId() {
        return currentSrvInstId;
    }

    public void setCurrentSrvInstId(Integer currentSrvInstId) {
        this.currentSrvInstId = currentSrvInstId;
    }

    public Integer getOpType() {
        return opType;
    }

    public void setOpType(Integer opType) {
        this.opType = opType;
    }

    public Integer getUpdateType() {
        return updateType;
    }

    public void setUpdateType(Integer updateType) {
        this.updateType = updateType;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getSrvNameEn() {
        return srvNameEn;
    }

    public void setSrvNameEn(String srvNameEn) {
        this.srvNameEn = srvNameEn;
    }

    public String getAppNameEn() {
        return appNameEn;
    }

    public void setAppNameEn(String appNameEn) {
        this.appNameEn = appNameEn;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNoHistory() {
        return noHistory;
    }

    public void setNoHistory(Integer noHistory) {
        this.noHistory = noHistory;
    }
}