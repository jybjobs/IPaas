package com.cloud.paas.appservice.util.yaml.rs;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by 17798 on 2018/4/17.
 */
public class ReplicaSetSelector {

    private JSONObject matchLabels;

    public JSONObject getMatchLabels() {
        return matchLabels;
    }

    public void setMatchLabels(JSONObject matchLabels) {
        this.matchLabels = matchLabels;
    }
}
