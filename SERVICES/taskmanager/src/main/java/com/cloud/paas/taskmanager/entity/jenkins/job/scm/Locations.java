package com.cloud.paas.taskmanager.entity.jenkins.job.scm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @Author: srf
 * @desc: Locations对象
 * @Date: Created in 2018-04-23 10:37
 * @Modified By:
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Locations {
    @XmlElement(name="hudson.scm.SubversionSCM_-ModuleLocation")
    private ModuleLocation moduleLocation;

    public ModuleLocation getModuleLocation() {
        return moduleLocation;
    }

    public void setModuleLocation(ModuleLocation moduleLocation) {
        this.moduleLocation = moduleLocation;
    }
}
