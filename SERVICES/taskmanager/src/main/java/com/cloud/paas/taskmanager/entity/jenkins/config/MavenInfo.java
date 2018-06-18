package com.cloud.paas.taskmanager.entity.jenkins.config;

/**
 * @Author: srf
 * @desc: 对象
 * @Date: Created in 2018/5/23 16:35
 * @Modified By:
 */
public class MavenInfo {
    private String mavenCommand;
    private String path;

    public String getMavenCommand() {
        return mavenCommand;
    }

    public void setMavenCommand(String mavenCommand) {
        this.mavenCommand = mavenCommand;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
