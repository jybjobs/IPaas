package com.cloud.paas.appservice.qo;

import com.cloud.paas.appservice.model.ValueObject;

/**
 * Created by CSS on 2017/12/8.
 */
public class AppExample extends ValueObject{

    /**
     *应用编号
     */
    private Integer appId;
    /**
     *应用名称
     */
    private String appNameZh;
    /**
     * 应用英文名
     */
    private String appNameEn;
    /**
     * 应用状态
     */
    private Long state;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 多选框 名称 版本
     */
    private String condition;

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppNameZh() {
        return appNameZh;
    }

    public void setAppNameZh(String appNameZh) {
        this.appNameZh = appNameZh;
    }

    public String getAppNameEn() {
        return appNameEn;
    }

    public void setAppNameEn(String appNameEn) {
        this.appNameEn = appNameEn;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
