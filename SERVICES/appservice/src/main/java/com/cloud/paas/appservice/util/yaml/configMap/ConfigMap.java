package com.cloud.paas.appservice.util.yaml.configMap;

import com.cloud.paas.appservice.util.yaml.common.ObjectBase;
import com.cloud.paas.appservice.util.yaml.common.ObjectMeta;

/**
 * Created by 17798 on 2018/6/4.
 */
public class ConfigMap extends ObjectBase {

    private Object data;

    private ObjectMeta metadata;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ObjectMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(ObjectMeta metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "ConfigMap{" +
                "data=" + data +
                ", metadata=" + metadata +
                '}';
    }
}
