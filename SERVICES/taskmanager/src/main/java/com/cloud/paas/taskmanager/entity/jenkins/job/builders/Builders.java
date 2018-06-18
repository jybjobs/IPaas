package com.cloud.paas.taskmanager.entity.jenkins.job.builders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @Author: srf
 * @desc: Builders对象
 * @Date: Created in 2018-04-23 16:19
 * @Modified By:
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"sonarRunnerBuilder","ant","maven","shell"})
public class Builders {
    @XmlElement(name="hudson.plugins.sonar.SonarRunnerBuilder")
    private SonarRunnerBuilder sonarRunnerBuilder;
    @XmlElement(name="hudson.tasks.Ant")
    private Ant ant;
    @XmlElement(name="hudson.tasks.Maven")
    private Maven maven;
    @XmlElement(name="hudson.tasks.Shell")
    private Shell shell;

    public SonarRunnerBuilder getSonarRunnerBuilder() {
        return sonarRunnerBuilder;
    }

    public void setSonarRunnerBuilder(SonarRunnerBuilder sonarRunnerBuilder) {
        this.sonarRunnerBuilder = sonarRunnerBuilder;
    }

    public Ant getAnt() {
        return ant;
    }

    public void setAnt(Ant ant) {
        this.ant = ant;
    }

    public Maven getMaven() {
        return maven;
    }

    public void setMaven(Maven maven) {
        this.maven = maven;
    }

    public Shell getShell() {
        return shell;
    }

    public void setShell(Shell shell) {
        this.shell = shell;
    }
}
