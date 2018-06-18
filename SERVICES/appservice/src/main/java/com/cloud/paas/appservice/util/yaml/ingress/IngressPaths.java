
package com.cloud.paas.appservice.util.yaml.ingress;

/**
 * @Author: wyj
 * @desc: ingress路径
 * @Date: Created in 2018-01-04 9:45
 * @Modified By:
 */
public class IngressPaths {

    /**
     * ingress配置路径
     */
    private String path;
    /**
     * ingress service后端配置
     */
    private IngressBackend backend;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public IngressBackend getBackend() {
        return backend;
    }

    public void setBackend(IngressBackend backend) {
        this.backend = backend;
    }
}
