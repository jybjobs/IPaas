package com.cloud.paas.appservice.util.yaml.deployment;

import com.cloud.paas.appservice.util.yaml.common.ObjectBase;

import java.util.List;

/**
 * @Author: wyj
 * @desc: deployment list
 * @Date: Created in 2017-12-14 15:01
 * @Modified By:
 */
public class DeploymentList extends ObjectBase{

    private List<Deployment> items;

    public List<Deployment> getItems() {
        return items;
    }

    public void setItems(List<Deployment> items) {
        this.items = items;
    }
}
