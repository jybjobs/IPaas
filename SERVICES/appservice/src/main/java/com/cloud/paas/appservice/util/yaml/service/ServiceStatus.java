package com.cloud.paas.appservice.util.yaml.service;

/**
 * @Author: wyj
 * @desc: service status
 * @Date: Created in 2017-12-14 15:17
 * @Modified By:
 */
public class ServiceStatus {

    private ServiceLoadbalancer loadBalancer;

    public ServiceLoadbalancer getLoadBalancer() {
        return loadBalancer;
    }

    public void setLoadBalancer(ServiceLoadbalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }
}
