package com.cloud.paas.appservice.util.yaml.pod;

import java.util.List;

/**
 * @Author: wyj
 * @desc: pod容器
 * @Date: Created in 2017-12-14 14:07
 * @Modified By:
 */
public class PodContainer {
    /**
     * 容器名称
     */
    private String name;
    /**
     * 容器的镜像
     */
    private String image;
    /**
     * 容器需要暴露的端口号列表
     */
    private List<PodPort> ports;
    /**
     * 容器环境变量列表
     */
    private List<PodEnv> env;
    /**
     * 容器资源
     */
    private PodResource resources;
    /**
     * 挂载到容器内部的存储卷配置
     */
    private List<PodVolumeMount> volumeMounts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<PodPort> getPorts() {
        return ports;
    }

    public void setPorts(List<PodPort> ports) {
        this.ports = ports;
    }

    public List<PodEnv> getEnv() {
        return env;
    }

    public void setEnv(List<PodEnv> env) {
        this.env = env;
    }

    public PodResource getResources() {
        return resources;
    }

    public void setResources(PodResource resources) {
        this.resources = resources;
    }

    public List<PodVolumeMount> getVolumeMounts() {
        return volumeMounts;
    }

    public void setVolumeMounts(List<PodVolumeMount> volumeMounts) {
        this.volumeMounts = volumeMounts;
    }
}
