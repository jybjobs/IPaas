package com.cloud.paas.appservice.util.yaml.deployment;

/**
 * @Author: wyj
 * @desc: deployment滚动升级
 * @Date: Created in 2017-12-14 14:47
 * @Modified By:
 */
public class DeploymentRollingUpdate {

    private String maxUnavailable;

    private String maxSurge;

    public String getMaxUnavailable() {
        return maxUnavailable;
    }

    public void setMaxUnavailable(String maxUnavailable) {
        this.maxUnavailable = maxUnavailable;
    }

    public String getMaxSurge() {
        return maxSurge;
    }

    public void setMaxSurge(String maxSurge) {
        this.maxSurge = maxSurge;
    }
}
