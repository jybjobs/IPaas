package com.cloud.paas.taskmanager.entity.jenkins.config;

import com.cloud.paas.taskmanager.entity.jenkins.job.builders.Shell;
import com.cloud.paas.taskmanager.entity.jenkins.job.properties.Strategy;
import com.cloud.paas.taskmanager.entity.jenkins.job.triggers.SCMTrigger;

/**
 * @Author: srf
 * @desc: CommonConfig对象
 * @Date: Created in 2018-04-25 15:34
 * @Modified By:
 */
public class CommonConfig {
    private String jobName;
    private Strategy strategy;
    private SCMTrigger scmTrigger;
    private SonarProperties sonarProperties;
    private Shell shell;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public SCMTrigger getScmTrigger() {
        return scmTrigger;
    }

    public void setScmTrigger(SCMTrigger scmTrigger) {
        this.scmTrigger = scmTrigger;
    }

    public Shell getShell() {
        return shell;
    }

    public void setShell(Shell shell) {
        this.shell = shell;
    }
}
