package com.cloud.paas.appservice.util.yaml.hpa;

import java.util.List;

/**
 * @Author: srf
 * @desc: HPASpec对象
 * @Date: Created in 2018-03-28 14-26
 * @Modified By:
 */
public class HPASpec {
    /**
     * 目标Deloyment的apiVersion
     */
    private HPAScaleTargetRef scaleTargetRef;
    /**
     * 最小副本数量
     */
    private int minReplicas;
    /**
     * 最大副本数量
     */
    private int maxReplicas;
    /**
     * 度量
     */
    private List<HPAMetrics> metrics;

    public HPAScaleTargetRef getScaleTargetRef() {
        return scaleTargetRef;
    }

    public void setScaleTargetRef(HPAScaleTargetRef scaleTargetRef) {
        this.scaleTargetRef = scaleTargetRef;
    }

    public int getMinReplicas() {
        return minReplicas;
    }

    public void setMinReplicas(int minReplicas) {
        this.minReplicas = minReplicas;
    }

    public int getMaxReplicas() {
        return maxReplicas;
    }

    public void setMaxReplicas(int maxReplicas) {
        this.maxReplicas = maxReplicas;
    }

    public List<HPAMetrics> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<HPAMetrics> metrics) {
        this.metrics = metrics;
    }
}
