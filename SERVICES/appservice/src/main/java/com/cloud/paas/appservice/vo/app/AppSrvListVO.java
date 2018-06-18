package com.cloud.paas.appservice.vo.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.cloud.paas.appservice.model.AppDetail;
import com.cloud.paas.appservice.vo.srv.SrvDetailOrderVO;

import java.util.Date;
import java.util.List;

/**
 * Created by CSS on 2017/12/11.
 */
public class AppSrvListVO  extends AppDetail{
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

    /**
     *应用运行时间
     */
    private String runningTime;
    /**
     * 服务列表
     */
    List<SrvDetailOrderVO> srvDetailOrderVOList;

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

    public String getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(String runningTime) {
        this.runningTime = runningTime;
    }

    public List<SrvDetailOrderVO> getSrvDetailOrderVOList() {
        return srvDetailOrderVOList;
    }

    public void setSrvDetailOrderVOList(List<SrvDetailOrderVO> srvDetailOrderVOList) {
        this.srvDetailOrderVOList = srvDetailOrderVOList;
    }
}
