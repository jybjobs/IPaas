package com.cloud.paas.appservice.qo;

import com.cloud.paas.appservice.model.SrvDetail;
import com.cloud.paas.appservice.model.SrvVersionDetail;

public class SrvVersionDetailExample extends SrvVersionDetail {

    private String condition;

    private SrvDetail srvDetail = new SrvDetail();

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public SrvDetail getSrvDetail() {
        return srvDetail;
    }

    public void setSrvDetail(SrvDetail srvDetail) {
        this.srvDetail = srvDetail;
    }



}