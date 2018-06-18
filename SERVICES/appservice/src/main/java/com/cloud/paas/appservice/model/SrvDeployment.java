package com.cloud.paas.appservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SrvDeployment extends ValueObject {

    /**
     * 部署编号
     */
    @NotNull(message = "{srvDeployment.deploymentId.notNull}", groups = SrvDeploymentAddValidate.class)
    private Integer deploymentId;

    /**
     * 部署名称
     */
    @NotBlank(message = "{srvDeployment.deploymentName.notBlank}", groups = {SrvDeploymentAddValidate.class, SrvDeploymentUpdateValidate.class})
    private String deploymentName;

    /**
     * 镜像url
     */
    @NotBlank(message = "{srvDeployment.imageUrl.notBlank}", groups = {SrvDeploymentAddValidate.class, SrvDeploymentUpdateValidate.class})
    private String imageUrl;

    /**
     * 部署类型
     */
    @NotNull(message = "{srvDeployment.publishType.notNull}", groups = {SrvDeploymentAddValidate.class, SrvDeploymentUpdateValidate.class})
    private Integer publishType;

    /**
     * 当前运行的实例编号
     */
    @NotNull(message = "{srvDeployment.curInstId.notNull}", groups = {SrvDeploymentAddValidate.class, SrvDeploymentUpdateValidate.class})
    private Integer curInstId;

    /**
     * 等待运行的实例编号
     */
    @NotNull(message = "{srvDeployment.newInstId.notNull}", groups = {SrvDeploymentUpdateValidate.class})
    private Integer newInstId;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    @NotBlank(message = "{srvDeployment.creator.notBlank}", groups = {SrvDeploymentAddValidate.class, SrvDeploymentUpdateValidate.class})
    private String creator;

    /**
     * 租户编号
     */
    @NotNull(message = "{srvDeployment.tenantId.notNull}", groups = {SrvDeploymentAddValidate.class, SrvDeploymentUpdateValidate.class})
    private Integer tenantId;

    /**
     * 服务编号
     */
    @NotNull(message = "{srvDeployment.srvId.notNull}", groups = {SrvDeploymentAddValidate.class, SrvDeploymentUpdateValidate.class})
    private Integer srvId;

    /**
     * 环境编号
     */
    @NotNull(message = "{srvDeployment.envId.notNull}", groups = {SrvDeploymentAddValidate.class, SrvDeploymentUpdateValidate.class})
    private Integer envId;

    /**
     * 是否自动部署 0为否，1为自动部署
     */
    private Integer autoPublish;

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
     * 服务部署分组校验
     */
    public interface SrvDeploymentAddValidate {
    }

    public interface SrvDeploymentUpdateValidate {
    }

    public Integer getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(Integer deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getDeploymentName() {
        return deploymentName;
    }

    public void setDeploymentName(String deploymentName) {
        this.deploymentName = deploymentName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getPublishType() {
        return publishType;
    }

    public void setPublishType(Integer publishType) {
        this.publishType = publishType;
    }

    public Integer getCurInstId() {
        return curInstId;
    }

    public void setCurInstId(Integer curInstId) {
        this.curInstId = curInstId;
    }

    public Integer getNewInstId() {
        return newInstId;
    }

    public void setNewInstId(Integer newInstId) {
        this.newInstId = newInstId;
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

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getSrvId() {
        return srvId;
    }

    public void setSrvId(Integer srvId) {
        this.srvId = srvId;
    }

    public Integer getEnvId() {
        return envId;
    }

    public void setEnvId(Integer envId) {
        this.envId = envId;
    }

    public Integer getAutoPublish() {
        return autoPublish;
    }

    public void setAutoPublish(Integer autoPublish) {
        this.autoPublish = autoPublish;
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

    public Byte getSrvInstNum() {
        return srvInstNum;
    }

    public void setSrvInstNum(Byte srvInstNum) {
        this.srvInstNum = srvInstNum;
    }
}