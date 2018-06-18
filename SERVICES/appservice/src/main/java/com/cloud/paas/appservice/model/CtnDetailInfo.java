package com.cloud.paas.appservice.model;

import java.util.Date;

public class CtnDetailInfo  extends ValueObject{
    /**
     * 容器编号
     */
    private Long ctnId;

    /**
     * 服务编号
     */
    private Long srvId;

    /**
     * 容器名称
     */
    private String ctnNameZh;

    /**
     * 容器英文名称
     */
    private String ctnNameEn;

    /**
     * 容器实际编号
     */
    private Long ctnRealId;

    /**
     * cpu
     */
    private Float cpu;

    /**
     * 内存
     */
    private Float mem;

    /**
     * 磁盘
     */
    private Float storage;

    /**
     * 网络模式
     */
    private Integer netPattern;

    /**
     * 主机IP
     */
    private String hostIp;

    /**
     * 主机端口
     */
    private Integer hostPort;

    /**
     *容器IP
     */
    private String ctnIp;

    /**
     * 容器端口
     */
    private Integer ctnPort;

    /**
     * 容器状态
     */
    private Integer ctnStatus;

    /**
     * 容器启动时间
     */
    private Date ctnStartTime;

    /**
     *容器停止时间
     */
    private Date ctnStopTime;

    /**
     * 存储方式
     */
    private Integer storageWay;

    /**
     * 存储类型
     */
    private Integer storageType;

    /**
     * 存储配额
     */
    private Integer storageQuota;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改时间
     */
    private Date updateTime;


    public Long getCtnId() {
        return ctnId;
    }


    public void setCtnId(Long ctnId) {
        this.ctnId = ctnId;
    }


    public Long getSrvId() {
        return srvId;
    }


    public void setSrvId(Long srvId) {
        this.srvId = srvId;
    }


    public String getCtnNameZh() {
        return ctnNameZh;
    }


    public void setCtnNameZh(String ctnNameZh) {
        this.ctnNameZh = ctnNameZh == null ? null : ctnNameZh.trim();
    }


    public String getCtnNameEn() {
        return ctnNameEn;
    }


    public void setCtnNameEn(String ctnNameEn) {
        this.ctnNameEn = ctnNameEn == null ? null : ctnNameEn.trim();
    }


    public Long getCtnRealId() {
        return ctnRealId;
    }


    public void setCtnRealId(Long ctnRealId) {
        this.ctnRealId = ctnRealId;
    }


    public Float getCpu() {
        return cpu;
    }


    public void setCpu(Float cpu) {
        this.cpu = cpu;
    }


    public Float getMem() {
        return mem;
    }


    public void setMem(Float mem) {
        this.mem = mem;
    }


    public Float getStorage() {
        return storage;
    }


    public void setStorage(Float storage) {
        this.storage = storage;
    }


    public Integer getNetPattern() {
        return netPattern;
    }


    public void setNetPattern(Integer netPattern) {
        this.netPattern = netPattern;
    }


    public String getHostIp() {
        return hostIp;
    }


    public void setHostIp(String hostIp) {
        this.hostIp = hostIp == null ? null : hostIp.trim();
    }


    public Integer getHostPort() {
        return hostPort;
    }


    public void setHostPort(Integer hostPort) {
        this.hostPort = hostPort;
    }


    public String getCtnIp() {
        return ctnIp;
    }


    public void setCtnIp(String ctnIp) {
        this.ctnIp = ctnIp == null ? null : ctnIp.trim();
    }


    public Integer getCtnPort() {
        return ctnPort;
    }


    public void setCtnPort(Integer ctnPort) {
        this.ctnPort = ctnPort;
    }


    public Integer getCtnStatus() {
        return ctnStatus;
    }


    public void setCtnStatus(Integer ctnStatus) {
        this.ctnStatus = ctnStatus;
    }


    public Date getCtnStartTime() {
        return ctnStartTime;
    }


    public void setCtnStartTime(Date ctnStartTime) {
        this.ctnStartTime = ctnStartTime;
    }


    public Date getCtnStopTime() {
        return ctnStopTime;
    }


    public void setCtnStopTime(Date ctnStopTime) {
        this.ctnStopTime = ctnStopTime;
    }


    public Integer getStorageWay() {
        return storageWay;
    }


    public void setStorageWay(Integer storageWay) {
        this.storageWay = storageWay;
    }


    public Integer getStorageType() {
        return storageType;
    }


    public void setStorageType(Integer storageType) {
        this.storageType = storageType;
    }


    public Integer getStorageQuota() {
        return storageQuota;
    }


    public void setStorageQuota(Integer storageQuota) {
        this.storageQuota = storageQuota;
    }


    public String getCreator() {
        return creator;
    }


    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }


    public Date getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Date getUpdateTime() {
        return updateTime;
    }


    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}