package com.cloud.paas.appservice.qo;

import com.cloud.paas.appservice.model.ValueObject;

/**
 * 输入条件查询
 * @author
 * @create 2017-12-20 20:23
 **/
public class SrvExample extends ValueObject{
    private String condition;
    /**
     *服务名称
     */
    private String srvNameCh;
    /**
     *镜像名称
     */
    private String srvImage;

    /**
     *镜像版本
     */
    private String srvImageVersion;
    /**
     * 状态
     */
    private Long state;
    /**
     *创建人
     */
    private String creator;
    /**
     *应用名称
     */
    private String appNameZh;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getSrvNameCh() {
        return srvNameCh;
    }

    public void setSrvNameCh(String srvNameCh) {
        this.srvNameCh = srvNameCh;
    }

    public String getSrvImage() {
        return srvImage;
    }

    public void setSrvImage(String srvImage) {
        this.srvImage = srvImage;
    }

    public String getSrvImageVersion() {
        return srvImageVersion;
    }

    public void setSrvImageVersion(String srvImageVersion) {
        this.srvImageVersion = srvImageVersion;
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

    public String getAppNameZh() {
        return appNameZh;
    }

    public void setAppNameZh(String appNameZh) {
        this.appNameZh = appNameZh;
    }
}
