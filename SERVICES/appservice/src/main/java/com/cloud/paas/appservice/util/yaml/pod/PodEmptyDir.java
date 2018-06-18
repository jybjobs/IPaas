package com.cloud.paas.appservice.util.yaml.pod;

import io.fabric8.kubernetes.api.model.Quantity;

/**
 * Created by 17798 on 2018/4/27.
 */
public class PodEmptyDir {

    private String medium;

    private Quantity sizeLimit;

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public Quantity getSizeLimit() {
        return sizeLimit;
    }

    public void setSizeLimit(Quantity sizeLimit) {
        this.sizeLimit = sizeLimit;
    }
}
