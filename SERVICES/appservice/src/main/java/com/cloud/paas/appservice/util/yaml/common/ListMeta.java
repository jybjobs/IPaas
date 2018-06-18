package com.cloud.paas.appservice.util.yaml.common;

/**
 * @Author: srf
 * @desc: ListMeta对象
 * @Date: Created in 2018-03-29 11-11
 * @Modified By:
 */
public class ListMeta {
    //继续
    private String continueStr;
    //资源版本
    private String resourceVersion;
    //当前对象的链接，只读
    private String selfLink;

    public String getContinueStr() {
        return continueStr;
    }

    public void setContinueStr(String continueStr) {
        this.continueStr = continueStr;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }
}
