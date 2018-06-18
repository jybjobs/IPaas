package com.cloud.paas.appservice.util.yaml.common;

/**
 * @Author: wyj
 * @desc: 公共Label标签
 * @Date: Created in 2017-12-14 10:50
 * @Modified By:
 */
public class ObjectLabel {

    /**
     * 标签
     */
    private String app;

    private String version;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
