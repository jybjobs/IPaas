package com.cloud.paas.appservice.util.yaml.scale;

/**
 * @Author: srf
 * @desc: ScaleSpec对象
 * @Date: Created in 2018-03-27 09-41
 * @Modified By:
 */
public class ScaleSpec {
    /**
     * 副本数量
     */
    private Integer replicas;

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }
}
