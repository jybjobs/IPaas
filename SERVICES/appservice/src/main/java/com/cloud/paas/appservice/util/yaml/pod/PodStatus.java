package com.cloud.paas.appservice.util.yaml.pod;

/**
 * @Author: srf
 * @desc: PodStatus对象
 * @Date: Created in 2018-04-15 11-50
 * @Modified By:
 */
public class PodStatus {
    private String hostIP;
    private String podIP;

    public String getHostIP() {
        return hostIP;
    }

    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    public String getPodIP() {
        return podIP;
    }

    public void setPodIP(String podIP) {
        this.podIP = podIP;
    }
}
