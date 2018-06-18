package com.cloud.paas.taskmanager.entity.jenkins.job.scm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Branches {
    @XmlElement(name="hudson.plugins.git.BranchSpec")
    private BranchSpec branchSpec;

    public BranchSpec getBranchSpec() {
        return branchSpec;
    }

    public void setBranchSpec(BranchSpec branchSpec) {
        this.branchSpec = branchSpec;
    }
}
