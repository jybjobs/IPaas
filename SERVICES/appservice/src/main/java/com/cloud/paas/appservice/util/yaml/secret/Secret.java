package com.cloud.paas.appservice.util.yaml.secret;

import com.cloud.paas.appservice.util.yaml.common.ObjectBase;
import com.cloud.paas.appservice.util.yaml.common.ObjectMeta;

/**
 * Created by 17798 on 2018/6/7.
 */
public class Secret extends ObjectBase {

    private Object data;

    private ObjectMeta metadata;

    private Object stringData;

    private String type;

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

    public Object getStringData() {
        return stringData;
    }

    public void setStringData(Object stringData) {
        this.stringData = stringData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
