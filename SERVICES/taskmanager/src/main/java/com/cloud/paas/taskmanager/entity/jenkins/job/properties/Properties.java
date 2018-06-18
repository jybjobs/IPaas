package com.cloud.paas.taskmanager.entity.jenkins.job.properties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @Author: srf
 * @desc: Properties对象
 * @Date: Created in 2018-04-23 09:55
 * @Modified By:
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Properties {
    @XmlElement(name ="jenkins.model.BuildDiscarderProperty")
    private BuildDiscarderProperty buildDiscarderProperty;

    public BuildDiscarderProperty getBuildDiscarderProperty() {
        return buildDiscarderProperty;
    }

    public void setBuildDiscarderProperty(BuildDiscarderProperty buildDiscarderProperty) {
        this.buildDiscarderProperty = buildDiscarderProperty;
    }
}
