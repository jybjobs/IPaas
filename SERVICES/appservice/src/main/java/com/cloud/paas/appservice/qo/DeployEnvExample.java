package com.cloud.paas.appservice.qo;

import com.cloud.paas.appservice.model.DeployEnv;

/**
 * Created by 17798 on 2018/6/4.
 */
public class DeployEnvExample extends DeployEnv {

    private String condition;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
