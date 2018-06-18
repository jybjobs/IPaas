package com.cloud.paas.taskmanager.entity.jenkins.job.triggers;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @Author: srf
 * @desc: Triggers对象
 * @Date: Created in 2018-04-23 16:15
 * @Modified By:
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Triggers {
    @XmlElement(name="hudson.triggers.SCMTrigger")
    private SCMTrigger scmTrigger;

    public SCMTrigger getScmTrigger() {
        return scmTrigger;
    }

    public void setScmTrigger(SCMTrigger scmTrigger) {
        this.scmTrigger = scmTrigger;
    }
}
