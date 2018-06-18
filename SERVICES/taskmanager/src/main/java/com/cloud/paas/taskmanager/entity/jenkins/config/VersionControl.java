package com.cloud.paas.taskmanager.entity.jenkins.config;

/**
 * @Author: srf
 * @desc: VersionControl对象
 * @Date: Created in 2018-04-28 10:16
 * @Modified By:
 */
public enum VersionControl {
    svn("svn"), git("git");
    private String name;
    VersionControl(String name){
        this.name = name;
    }
    public static VersionControl get(String name) {
        for(VersionControl depthOption: VersionControl.values()) {
            if(depthOption.name.equals(name)) return depthOption;
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
