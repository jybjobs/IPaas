package com.cloud.paas.monitor.qo;

import java.util.Date;

/**
 * @author: zcy
 * @desc: 历史记录查询条件
 * @Date: Created in 2018-01-12 15:09
 * @modified By:
 **/
public class HistoryCondition {
    private int interval;
    private int type;
    private Date startTime;
    private Date finishTime;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}
