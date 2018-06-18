package com.cloud.paas.appservice.util.yaml.service;

/**
 * @Author: wyj
 * @desc:
 * @Date: Created in 2017-12-14 15:21
 * @Modified By:
 */
public class ServiceIngress {

    private String hostname;

    private String ip;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
