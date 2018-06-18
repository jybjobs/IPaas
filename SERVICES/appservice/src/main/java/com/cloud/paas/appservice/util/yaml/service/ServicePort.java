package com.cloud.paas.appservice.util.yaml.service;

/**
 * @Author: wyj
 * @desc: service port
 * @Date: Created in 2017-12-14 15:25
 * @Modified By:
 */
public class ServicePort {

    /**
     * 端口协议
     */
    private String protocol;

    private Integer port;

    private Integer targetPort;

    private Integer nodePort;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(Integer targetPort) {
        this.targetPort = targetPort;
    }

    public Integer getNodePort() {
        return nodePort;
    }

    public void setNodePort(Integer nodePort) {
        this.nodePort = nodePort;
    }
}
