package com.cloud.paas.appservice.util.yaml.pod;

/**
 * @Author: wyj
 * @desc: pod端口号
 * @Date: Created in 2017-12-14 14:12
 * @Modified By:
 */
public class PodPort {

    /**
     * 容器需要监听的端口号
     */
    private Integer containerPort;
    /**
     * 端口协议
     */
    private String protocol;

    public Integer getContainerPort() {
        return containerPort;
    }

    public void setContainerPort(Integer containerPort) {
        this.containerPort = containerPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
