package com.cloud.paas.appservice.util.yaml.pod;


/**
 * @Author: wyj
 * @desc: 容器资源
 * @Date: Created in 2017-12-14 14:21
 * @Modified By:
 */
public class PodResource {
    /**
     * pod资源需求
     */
    private PodResourceRequest requests;
    /**
     * pod资源限制
     */
    private PodResourceLimit limits;

    public PodResourceRequest getRequests() {
        return requests;
    }

    public void setRequests(PodResourceRequest requests) {
        this.requests = requests;
    }

    public PodResourceLimit getLimits() {
        return limits;
    }

    public void setLimits(PodResourceLimit limits) {
        this.limits = limits;
    }
}
