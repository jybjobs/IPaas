package com.cloud.paas.appservice.vo.app;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
/**
 * Created by CSS on 2017/12/11.
 */
public class AppSrvListVOSupport extends AppSrvListVO{

    /**
     * 应用状态
     */
    private Long appState;

    /**
     * 服务个数
     */
    private int srvSize;

    /**
     *应用开始时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     *应用停止时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stopTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public Long getAppState() {
        return appState;
    }

    @Override
    public void setAppState(Long appState) {
        this.appState = appState;
    }

    public int getSrvSize() {
        return srvSize;
    }

    public void setSrvSize(int srvSize) {
        this.srvSize = srvSize;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }
}
