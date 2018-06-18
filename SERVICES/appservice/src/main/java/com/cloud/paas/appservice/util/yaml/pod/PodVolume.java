package com.cloud.paas.appservice.util.yaml.pod;

/**
 * @Author: wyj
 * @desc: pod volume
 * @Date: Created in 2017-12-14 11:32
 * @Modified By:
 */
public class PodVolume {

    /**
     * volume name
     */
    private String name;
    /**
     * 类型为emptyDir的存储卷
     */
    private PodEmptyDir emptyDir;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PodEmptyDir getEmptyDir() {
        return emptyDir;
    }

    public void setEmptyDir(PodEmptyDir emptyDir) {
        this.emptyDir = emptyDir;
    }

}
