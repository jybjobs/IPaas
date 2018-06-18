package com.cloud.paas.appservice.vo.app;

import java.util.Date;

/**
 *  app+srvListOrder   为app添加字段
 * Created by CSS on 2017/12/14.
 */
public class AppSrvListOrderVOSupport extends AppSrvListOrderVO{
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
    private Date startTime;

    /**
     * 应用停止时间
     */
    private Date stopTime;

    /**
     * 应用运行时长
     */
    private Date runningTime;

   public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    public Long getAppState() {
        return appState;
    }

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

    public Date getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(Date runningTime) {
        this.runningTime = runningTime;
    }
}
