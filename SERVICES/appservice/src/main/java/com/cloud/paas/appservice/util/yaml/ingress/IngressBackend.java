
package com.cloud.paas.appservice.util.yaml.ingress;

/**
 * @Author: wyj
 * @desc: ingress backend对应的后端service和端口
 * @Date: Created in 2018-01-04 9:46
 * @Modified By:
 */
public class IngressBackend {

    /**
     * service的名称
     */
    private String serviceName;

    /**
     * service 端口
     */
    private Integer servicePort;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getServicePort() {
        return servicePort;
    }

    public void setServicePort(Integer servicePort) {
        this.servicePort = servicePort;
    }
}
