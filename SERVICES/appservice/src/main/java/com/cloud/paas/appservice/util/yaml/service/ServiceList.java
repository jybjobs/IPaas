package com.cloud.paas.appservice.util.yaml.service;

import com.cloud.paas.appservice.util.yaml.common.ObjectBase;

import java.util.List;

/**
 * @Author: wyj
 * @desc: service list
 * @Date: Created in 2017-12-14 15:32
 * @Modified By:
 */
public class ServiceList extends ObjectBase{

    private List<Service> items;

    public List<Service> getItems() {
        return items;
    }

    public void setItems(List<Service> items) {
        this.items = items;
    }
}
