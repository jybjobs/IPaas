
package com.cloud.paas.appservice.util.yaml.ingress;

/**
 * @Author: wyj
 * @desc: ingress规则
 * @Date: Created in 2018-01-04 9:40
 * @Modified By:
 */
public class IngressRules {

    /**
     * 必须为域名
     */
    private String host;
    /**
     * ingress http配置（对应路径）
     */
    private IngressHttp http;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public IngressHttp getHttp() {
        return http;
    }

    public void setHttp(IngressHttp http) {
        this.http = http;
    }
}
