package com.cloud.paas.monitor.util.Pod;

/**
 * @Author: wyj
 * @desc: pod资源需求
 * @Date: Created in 2017-12-14 14:29
 * @Modified By:
 */
public class PodResourceRequest {

    /**
     * cpu
     */
    private String cpu;
    /**
     * memory
     */
    private String memory;

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }
}
