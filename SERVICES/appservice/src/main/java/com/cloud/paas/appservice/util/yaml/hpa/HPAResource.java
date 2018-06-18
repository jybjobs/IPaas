package com.cloud.paas.appservice.util.yaml.hpa;

/**
 * @Author: srf
 * @desc: HPAResource对象
 * @Date: Created in 2018-03-30 14-34
 * @Modified By:
 */
public class HPAResource {
    private String name;
    private Byte targetAverageUtilization;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getTargetAverageUtilization() {
        return targetAverageUtilization;
    }

    public void setTargetAverageUtilization(Byte targetAverageUtilization) {
        this.targetAverageUtilization = targetAverageUtilization;
    }
}
