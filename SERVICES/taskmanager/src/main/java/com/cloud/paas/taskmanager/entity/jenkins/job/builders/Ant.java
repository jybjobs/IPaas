package com.cloud.paas.taskmanager.entity.jenkins.job.builders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @Author: srf
 * @desc: Ant对象
 * @Date: Created in 2018-04-23 16:33
 * @Modified By:
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Ant {
    @XmlAttribute
    private String plugin;
    @XmlElement
    private String targets = "";
    @XmlElement
    private String antName;

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public String getTargets() {
        return targets;
    }

    public void setTargets(String targets) {
        this.targets = targets;
    }

    public String getAntName() {
        return antName;
    }

    public void setAntName(String antName) {
        this.antName = antName;
    }
}
