package com.cloud.paas.appservice.vo.srv;

import java.util.List;

public class SrvOperationListVO {

    /**
     *应用编号
     */
    private Integer appId;

    List<SrvOperationVO> serDetailList;

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public List<SrvOperationVO> getSerDetailList() {
        return serDetailList;
    }

    public void setSerDetailList(List<SrvOperationVO> serDetailList) {
        this.serDetailList = serDetailList;
    }
}
