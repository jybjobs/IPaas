package com.cloud.paas.appservice.util.yaml.pod;

import java.util.List;

/**
 * @Author: wyj
 * @desc: pod详细描述
 * @Date: Created in 2017-12-14 11:25
 * @Modified By:
 */
public class PodSpec {

    /**
     * pod定义的共享存储卷列表
     */
    private List<PodVolume> volumes;
    /**
     * pod 容器列表
     */
    private List<PodContainer> containers;

    public List<PodVolume> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<PodVolume> volumes) {
        this.volumes = volumes;
    }

    public List<PodContainer> getContainers() {
        return containers;
    }

    public void setContainers(List<PodContainer> containers) {
        this.containers = containers;
    }
}
