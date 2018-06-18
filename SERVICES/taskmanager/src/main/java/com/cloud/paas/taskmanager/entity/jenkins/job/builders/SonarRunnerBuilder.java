package com.cloud.paas.taskmanager.entity.jenkins.job.builders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @Author: srf
 * @desc: SonarRunnerBuilder对象
 * @Date: Created in 2018-04-23 16:22
 * @Modified By:
 */
@XmlType(propOrder={"properties","javaOpts"})
@XmlAccessorType(XmlAccessType.FIELD)
public class SonarRunnerBuilder {
    @XmlAttribute
    private String plugin;
    private String properties;
    private String javaOpts;

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getJavaOpts() {
        return javaOpts;
    }

    public void setJavaOpts(String javaOpts) {
        this.javaOpts = javaOpts;
    }
}
