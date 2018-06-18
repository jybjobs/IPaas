package com.cloud.paas.taskmanager.entity.jenkins.job.triggers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @Author: srf
 * @desc: SCMTrigger对象
 * @Date: Created in 2018-04-23 16:15
 * @Modified By:
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SCMTrigger {
    @XmlElement
    private String spec;

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
}
