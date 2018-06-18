package com.cloud.paas.appservice.util.yaml.pod;

/**
 * @Author: wyj
 * @desc: 挂载到容器内部的存储卷配置
 * @Date: Created in 2017-12-14 14:37
 * @Modified By:
 */
public class PodVolumeMount {

    /**
     * 引用的共享存储卷名称
     */
    private String name;
    /**
     * 存储卷在容器内的绝对路径
     */
    private String mountPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMountPath() {
        return mountPath;
    }

    public void setMountPath(String mountPath) {
        this.mountPath = mountPath;
    }
}
