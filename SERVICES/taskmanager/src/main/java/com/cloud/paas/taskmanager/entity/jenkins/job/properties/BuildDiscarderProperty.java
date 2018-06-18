package com.cloud.paas.taskmanager.entity.jenkins.job.properties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @Author: srf
 * @desc: BuildDiscarderProperty对象
 * @Date: Created in 2018-04-23 09:56
 * @Modified By:
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BuildDiscarderProperty {
    private Strategy strategy;

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}
