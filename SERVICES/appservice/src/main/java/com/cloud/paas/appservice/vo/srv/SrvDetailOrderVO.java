package com.cloud.paas.appservice.vo.srv;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.cloud.paas.appservice.model.SrvInstDetail;

import java.util.Date;

/**
 * Created by CSS on 2017/12/14.
 */
public class SrvDetailOrderVO extends SrvInstDetail{

    private String srvNameCh;

    private String srvNameEn;

    private String srvDesc;

    private String srvVersion;

    private Integer srvType;

    private String srvImage;

    private String srvImageVersion;

    private Integer tenantId;

    public String getSrvNameCh() {
        return srvNameCh;
    }

    public void setSrvNameCh(String srvNameCh) {
        this.srvNameCh = srvNameCh;
    }

    public String getSrvNameEn() {
        return srvNameEn;
    }

    public void setSrvNameEn(String srvNameEn) {
        this.srvNameEn = srvNameEn;
    }

    public String getSrvDesc() {
        return srvDesc;
    }

    public void setSrvDesc(String srvDesc) {
        this.srvDesc = srvDesc;
    }

    public String getSrvVersion() {
        return srvVersion;
    }

    public void setSrvVersion(String srvVersion) {
        this.srvVersion = srvVersion;
    }

    public Integer getSrvType() {
        return srvType;
    }

    public void setSrvType(Integer srvType) {
        this.srvType = srvType;
    }

    public String getSrvImage() {
        return srvImage;
    }

    public void setSrvImage(String srvImage) {
        this.srvImage = srvImage;
    }

    public String getSrvImageVersion() {
        return srvImageVersion;
    }

    public void setSrvImageVersion(String srvImageVersion) {
        this.srvImageVersion = srvImageVersion;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

//TODO 删除

    String srvId;
        /**
     * 服务所属应用
     */
    private String appNameZh;

    /**
     * 服务状态
     */
    private Long state;
    /**
     * 服务启动时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 服务停止时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stopTime;

    public String getSrvId() {
        return srvId;
    }

    public void setSrvId(String srvId) {
        this.srvId = srvId;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public String getAppNameZh() {
        return appNameZh;
    }

    public void setAppNameZh(String appNameZh) {
        this.appNameZh = appNameZh;
    }

}
