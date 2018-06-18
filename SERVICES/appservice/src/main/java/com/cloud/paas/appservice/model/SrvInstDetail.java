package com.cloud.paas.appservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class SrvInstDetail extends ValueObject {
    /**
     * 服务实例编号
     */
    private Integer srvInstId;

    /**
     * 服务版本编号
     */
    private Integer srvVersionId;

    /**
     * 初始服务实例副本数
     */
    @NotNull(message = "{srvInsts.srvInstNum.notNull}", groups = {SrvInstDetail.SrvInstAddValidate.class, SrvInstDetail.SrvInstUpdateValidate.class})
    private Byte srvInstNum;

    /**
     * cpu核数
     */
    @NotBlank(message = "{srvInsts.cpu.notBlank}", groups = {SrvInstDetail.SrvInstAddValidate.class, SrvInstDetail.SrvInstUpdateValidate.class})
    private String cpu;

    /**
     * 内存
     */
    @NotBlank(message = "{srvInsts.mem.notBlank}", groups = {SrvInstDetail.SrvInstAddValidate.class, SrvInstDetail.SrvInstUpdateValidate.class})
    private String mem;

    /**
     * 域名前缀
     */
    private String domainPrefix;

    /**
     * 域名后缀
     */
    private String domainSuffix;

    /**
     * 节点端口
     */
    private Integer nodePort;

    /**
     * 存储配额
     */
    private Integer storageQuota;

    /**
     * 应用编号
     */
    @NotNull(message = "{srvInsts.appId.notNull}", groups = SrvInstDetail.SrvInstAddValidate.class)
    private Integer appId;

    /**
     * 应用中文名
     */
    @NotBlank(message = "{srvInsts.appNameZh.notBlank}", groups = SrvInstDetail.SrvInstAddValidate.class)
    private String appNameZh;

    /**
     * 启动顺序
     */
    private Byte startOrder;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 服务实例状态
     * 未发布=>2134000 初始状态
     * 创建中=>2133000,启动中=>2130000 中间状态
     * 构建成功=>2131000,失败=>2132000,运行中=>2135000 最终状态
     */
    private Integer srvInstStatus;

    /**
     * 是否是历史 1为历史 0为正常状态
     */
    private Integer history;

    public interface SrvInstAddValidate {
    }

    public interface SrvInstUpdateValidate {
    }

    public Integer getSrvInstId() {
        return srvInstId;
    }

    public void setSrvInstId(Integer srvInstId) {
        this.srvInstId = srvInstId;
    }

    public Integer getSrvVersionId() {
        return srvVersionId;
    }

    public void setSrvVersionId(Integer srvVersionId) {
        this.srvVersionId = srvVersionId;
    }

    public Byte getSrvInstNum() {
        return srvInstNum;
    }

    public void setSrvInstNum(Byte srvInstNum) {
        this.srvInstNum = srvInstNum;
    }

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

    public String getDomainPrefix() {
        return domainPrefix;
    }

    public void setDomainPrefix(String domainPrefix) {
        this.domainPrefix = domainPrefix == null ? null : domainPrefix.trim();
    }

    public String getDomainSuffix() {
        return domainSuffix;
    }

    public void setDomainSuffix(String domainSuffix) {
        this.domainSuffix = domainSuffix == null ? null : domainSuffix.trim();
    }

    public Integer getNodePort() {
        return nodePort;
    }

    public void setNodePort(Integer nodePort) {
        this.nodePort = nodePort;
    }

    public Integer getStorageQuota() {
        return storageQuota;
    }

    public void setStorageQuota(Integer storageQuota) {
        this.storageQuota = storageQuota;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppNameZh() {
        return appNameZh;
    }

    public void setAppNameZh(String appNameZh) {
        this.appNameZh = appNameZh;
    }

    public Byte getStartOrder() {
        return startOrder;
    }

    public void setStartOrder(Byte startOrder) {
        this.startOrder = startOrder;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getSrvInstStatus() {
        return srvInstStatus;
    }

    public void setSrvInstStatus(Integer srvInstStatus) {
        this.srvInstStatus = srvInstStatus;
    }

    public Integer getHistory() {
        return history;
    }

    public void setHistory(Integer history) {
        this.history = history;
    }
}