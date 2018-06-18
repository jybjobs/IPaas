package com.cloud.paas.taskmanager.entity.jenkins.job.properties;

import javax.xml.bind.annotation.*;

/**
 * @Author: srf
 * @desc: Strategy对象
 * @Date: Created in 2018-04-23 10:23
 * @Modified By:
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"daysToKeep","numToKeep"})
public class Strategy {
    @XmlAttribute(name = "class")
    private String strategyClass;
    private int daysToKeep;
    private int numToKeep;

    public String getStrategyClass() {
        return strategyClass;
    }

    public void setStrategyClass(String strategyClass) {
        this.strategyClass = strategyClass;
    }

    public int getDaysToKeep() {
        return daysToKeep;
    }

    public void setDaysToKeep(int daysToKeep) {
        this.daysToKeep = daysToKeep;
    }

    public int getNumToKeep() {
        return numToKeep;
    }

    public void setNumToKeep(int numToKeep) {
        this.numToKeep = numToKeep;
    }

}
