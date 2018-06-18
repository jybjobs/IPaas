package com.cloud.paas.appservice.util.yaml.pod;


import com.cloud.paas.appservice.util.yaml.common.ObjectMeta;

import java.util.List;

/**
 * @Author: srf
 * @desc: PodList对象
 * @Date: Created in 2018-04-09 16-38
 * @Modified By:
 */
public class PodList {
    private String apiVersion;
    private String kind;
    private ObjectMeta metadata;
    private List<Pod> items;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public ObjectMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(ObjectMeta metadata) {
        this.metadata = metadata;
    }

    public List<Pod> getItems() {
        return items;
    }

    public void setItems(List<Pod> items) {
        this.items = items;
    }
}
