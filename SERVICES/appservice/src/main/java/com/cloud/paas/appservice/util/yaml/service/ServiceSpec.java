package com.cloud.paas.appservice.util.yaml.service;

import java.util.List;

/**
 * @Author: wyj
 * @desc:
 * @Date: Created in 2017-12-14 15:15
 * @Modified By:
 */
public class ServiceSpec {
    /**
     * 端口列表
     */
    private List<ServicePort> ports;
    /**
     * selector
     */
    private ServiceSelector selector;
    /**
     * 类型
     */
    private String type;
    /**
     * clusterIP
     */
    private String clusterIP;

    /**
     * sessionAffinity
     */
    private String sessionAffinity;

    public List<ServicePort> getPorts() {
        return ports;
    }

    public void setPorts(List<ServicePort> ports) {
        this.ports = ports;
    }

    public ServiceSelector getSelector() {
        return selector;
    }

    public void setSelector(ServiceSelector selector) {
        this.selector = selector;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClusterIP() {
        return clusterIP;
    }

    public void setClusterIP(String clusterIP) {
        this.clusterIP = clusterIP;
    }

    public String getSessionAffinity() {
        return sessionAffinity;
    }

    public void setSessionAffinity(String sessionAffinity) {
        this.sessionAffinity = sessionAffinity;
    }
}
