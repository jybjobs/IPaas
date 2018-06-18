package com.cloud.paas.appservice.vo.app;

/**
 * Created by CSS on 2017/12/8.
 */
public class AppDetailVOSupport  extends AppDetailVO{

    /**
     * 应用状态
     */
    private Long appState;

    /**
     * 服务个数
     */
    private int srvSize;

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
}
