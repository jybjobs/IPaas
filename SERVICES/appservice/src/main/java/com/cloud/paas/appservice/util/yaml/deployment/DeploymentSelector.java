package com.cloud.paas.appservice.util.yaml.deployment;

/**
 * @Author: wyj
 * @desc: Deployment label selector
 * @Date: Created in 2017-12-14 11:13
 * @Modified By:
 */
public class DeploymentSelector {

    /**
     * Deployment MatchLabel
     */
    private DeploymentMatchLabel matchLabels;

    public DeploymentMatchLabel getMatchLabels() {
        return matchLabels;
    }

    public void setMatchLabels(DeploymentMatchLabel matchLabels) {
        this.matchLabels = matchLabels;
    }
}
