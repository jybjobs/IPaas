package com.cloud.paas.taskmanager.entity.jenkins.job;

import com.cloud.paas.taskmanager.entity.jenkins.job.builders.Builders;
import com.cloud.paas.taskmanager.entity.jenkins.job.properties.Properties;
import com.cloud.paas.taskmanager.entity.jenkins.job.scm.Scm;
import com.cloud.paas.taskmanager.entity.jenkins.job.triggers.Triggers;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * @Author: srf
 * @desc: project对象
 * @Date: Created in 2018-04-23 09:36
 * @Modified By:
 */
@XmlType(propOrder={"properties","scm","triggers","builders","jdk"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Project implements Serializable {
    private Properties properties;
    private Scm scm;
    private String jdk;
    private Triggers triggers;
    private Builders builders;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Scm getScm() {
        return scm;
    }

    public void setScm(Scm scm) {
        this.scm = scm;
    }

    public String getJdk() {
        return jdk;
    }

    public void setJdk(String jdk) {
        this.jdk = jdk;
    }

    public Triggers getTriggers() {
        return triggers;
    }

    public void setTriggers(Triggers triggers) {
        this.triggers = triggers;
    }

    public Builders getBuilders() {
        return builders;
    }

    public void setBuilders(Builders builders) {
        this.builders = builders;
    }
}
