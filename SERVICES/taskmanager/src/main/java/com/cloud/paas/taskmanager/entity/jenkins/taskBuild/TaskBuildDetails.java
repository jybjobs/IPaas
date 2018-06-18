package com.cloud.paas.taskmanager.entity.jenkins.taskBuild;

import com.offbytwo.jenkins.model.BuildResult;

/**
 * @Author: srf
 * @desc: TaskDetails对象
 * @Date: Created in 2018/5/22 14:59
 * @Modified By:
 */
public class TaskBuildDetails {
    private int index;
    private BuildResult buildStatus;
    private String date;
    private String imageTag;
    private String log;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public BuildResult getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(BuildResult buildStatus) {
        this.buildStatus = buildStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageTag() {
        return imageTag;
    }

    public void setImageTag(String imageTag) {
        this.imageTag = imageTag;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
