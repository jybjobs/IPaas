package com.cloud.paas.appservice.qo;

import com.cloud.paas.appservice.model.SrvDeployment;

public class SrvDeploymentExample extends SrvDeployment {

    /**
     * 请求的cpu资源
     */
    private String cpu;

    /**
     * 请求的mem
     */
    private String mem;

    /**
     * 实例数量
     */
    private Byte srvInstNum;

    /**
     * 服务版本编号
     */
    private Integer srvVersionId;

    /**
     * 是否同步至资源模板 0为否，1为是
     */
    private Byte isSync;

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMem() {
        return mem;
    }

    public void setMem(String mem) {
        this.mem = mem;
    }

    public Byte getSrvInstNum() {
        return srvInstNum;
    }

    public void setSrvInstNum(Byte srvInstNum) {
        this.srvInstNum = srvInstNum;
    }

    public Integer getSrvVersionId() {
        return srvVersionId;
    }

    public void setSrvVersionId(Integer srvVersionId) {
        this.srvVersionId = srvVersionId;
    }

    public Byte getIsSync() {
        return isSync;
    }

    public void setIsSync(Byte isSync) {
        this.isSync = isSync;
    }
}