package com.cloud.paas.appservice.vo.srv;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.cloud.paas.appservice.model.SrvOperation;

import java.util.Date;

public class SrvOperationVO extends SrvOperation{
    /**
     * 启动时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 停止时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stopTime;

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
}
