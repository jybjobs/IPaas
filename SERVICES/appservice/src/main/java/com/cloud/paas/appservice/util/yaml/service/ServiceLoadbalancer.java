package com.cloud.paas.appservice.util.yaml.service;

import java.util.List;

/**
 * @Author: wyj
 * @desc:
 * @Date: Created in 2017-12-14 15:21
 * @Modified By:
 */
public class ServiceLoadbalancer {

   private List<ServiceIngress> ingress;

    public List<ServiceIngress> getIngress() {
        return ingress;
    }

    public void setIngress(List<ServiceIngress> ingress) {
        this.ingress = ingress;
    }
}
