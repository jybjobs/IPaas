package com.cloud.paas.appservice.util.yaml.deployment;

/**
 * @Author: wyj
 * @desc:
 * @Date: Created in 2017-12-14 11:00
 * @Modified By:
 */
public class DeploymentCondition {
    /**
     * 类型
     */
    private String type;
    /**
     * 详细状态
     */
    private String status;
    /**
     * 最后更新时间
     */
    private String lastUpdateTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
