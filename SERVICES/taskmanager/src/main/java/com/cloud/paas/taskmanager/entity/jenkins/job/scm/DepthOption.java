package com.cloud.paas.taskmanager.entity.jenkins.job.scm;

import javax.xml.bind.annotation.XmlEnumValue;

public enum DepthOption {
    infinity("infinity"), empty("empty"), files("files"), immediates("immediates"), @XmlEnumValue("as-it-is") asItIs("as-it-is");
    private String name;
    private DepthOption(String name){
        this.name = name;
    }
    public static DepthOption get(String name) {
        for(DepthOption depthOption: DepthOption.values()) {
            if(depthOption.name.equals(name)) return depthOption;
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
