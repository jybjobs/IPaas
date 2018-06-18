package com.cloud.paas.appservice.util.yaml.pvc;

/**
 * Created by 17798 on 2018/6/8.
 */
public class ResourceRequirements {

    private Object requests;

    private Object limits ;

    public Object getRequests() {
        return requests;
    }

    public void setRequests(Object requests) {
        this.requests = requests;
    }

    public Object getLimits() {
        return limits;
    }

    public void setLimits(Object limits) {
        this.limits = limits;
    }
}
