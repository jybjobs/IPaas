package com.cloud.paas.appservice.vo.srv;

import com.cloud.paas.appservice.model.SrvDetail;
import com.cloud.paas.appservice.model.SrvVersionDetail;

/**
 * Created by 17798 on 2018/4/1.
 */
public class SrvVersionDetailVO extends SrvVersionDetail {

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
