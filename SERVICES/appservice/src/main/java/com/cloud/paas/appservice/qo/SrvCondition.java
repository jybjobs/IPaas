package com.cloud.paas.appservice.qo;

/**
 * 查询指定用户下指定应用的服务列表时传人userId和appId
 * Created by kaiwen on 2017/12/12.
 */
public class SrvCondition {
    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 应用名称
     */
    private String appNameZh;
    /**
     * 服务名称
     */
    private String srvNameCh;

    /**
     * 服务状态
     */
    private Integer state;

    public SrvCondition() {
    }

    public SrvCondition(Integer userId, String appNameZh) {
        this.userId = userId;
        this.appNameZh = appNameZh;
    }

    public SrvCondition(Integer userId, String appNameZh, String srvNameCh) {
        this.userId = userId;
        this.appNameZh = appNameZh;
        this.srvNameCh = srvNameCh;
    }

    public SrvCondition(Integer userId, String appNameZh, String srvNameCh, Integer state) {
        this.userId = userId;
        this.appNameZh = appNameZh;
        this.srvNameCh = srvNameCh;
        this.state = state;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAppNameZh() {
        return appNameZh;
    }

    public void setAppNameZh(String appNameZh) {
        this.appNameZh = appNameZh;
    }

    public String getSrvNameCh() {
        return srvNameCh;
    }

    public void setSrvNameCh(String srvNameCh) {
        this.srvNameCh = srvNameCh;
    }
}
