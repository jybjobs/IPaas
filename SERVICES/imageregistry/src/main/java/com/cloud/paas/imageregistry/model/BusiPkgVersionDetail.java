package com.cloud.paas.imageregistry.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: css
 * @Description: 业务版本实体类
 * @Date: Create in 11:19 2017/11/24
 * @Modified by:
 */
public class BusiPkgVersionDetail extends ValueObject {
    /**
     * 业务包版本编号
     */
    private Integer busiPkgVersionId;
    /**
     * 业务包编号
     */
    private Integer busiPkgId;

    /**
     * 业务包版本
     */
    @NotBlank(message = "{busipkgversion.busipkgversion.notblank}",groups = BusiPkgVersionAddValidate.class)
    private String busiPkgVersion;
    /**
     * 业务包版本大小
     */
    private BigDecimal busiPkgVersionSize;
    /**
     * 业务包格式
     */
    private String busiPkgPostfix;
    /**
     * 业务包版本描述
     */
    private String busiPkgVersionRemark;
    /**
     * 业务包状态
     */
    private Integer busiPkgStatus;
    /**
     * 业务包阶段
     */
    @NotNull(message = "{busipkgversion.busipkgstage.notnull}",groups = BusiPkgVersionAddValidate.class)
    private Integer busiPkgStage;
    /**
     * 是否被引用
     */
    private Integer busiPkgReference;
    /**
     * 上传方式
     */
    private Integer uploadWay;
    /**
     * 上传文件名
     */
    private String uploadName;
    /**
     * 存放路径
     */
    private String path;
    /**
     * 配置编号
     */
    private Integer hostConfId;
    /**
     * 主机IP
     */
    private String hostIp;
    /**
     * 端口
     */
    private Integer port;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String passwd;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 最后修改时间
     */
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 业务包版本校验分组
     */
    public interface BusiPkgVersionAddValidate{};

    public Integer getBusiPkgVersionId() {
        return busiPkgVersionId;
    }


    public void setBusiPkgVersionId(Integer busiPkgVersionId) {
        this.busiPkgVersionId = busiPkgVersionId;
    }


    public Integer getBusiPkgId() {
        return busiPkgId;
    }


    public void setBusiPkgId(Integer busiPkgId) {
        this.busiPkgId = busiPkgId;
    }


    public String getBusiPkgVersion() {
        return busiPkgVersion;
    }


    public void setBusiPkgVersion(String busiPkgVersion) {
        this.busiPkgVersion = busiPkgVersion == null ? null : busiPkgVersion.trim();
    }


    public BigDecimal getBusiPkgVersionSize() {
        return busiPkgVersionSize;
    }

    public void setBusiPkgVersionSize(BigDecimal busiPkgVersionSize) {
        this.busiPkgVersionSize = busiPkgVersionSize;
    }

    public String getBusiPkgPostfix() {
        return busiPkgPostfix;
    }


    public void setBusiPkgPostfix(String busiPkgPostfix) {
        this.busiPkgPostfix = busiPkgPostfix == null ? null : busiPkgPostfix.trim();
    }


    public String getBusiPkgVersionRemark() {
        return busiPkgVersionRemark;
    }

    public void setBusiPkgVersionRemark(String busiPkgVersionRemark) {
        this.busiPkgVersionRemark = busiPkgVersionRemark == null ? null : busiPkgVersionRemark.trim();
    }


    public Integer getBusiPkgStatus() {
        return busiPkgStatus;
    }


    public void setBusiPkgStatus(Integer busiPkgStatus) {
        this.busiPkgStatus = busiPkgStatus;
    }


    public Integer getBusiPkgStage() {
        return busiPkgStage;
    }


    public void setBusiPkgStage(Integer busiPkgStage) {
        this.busiPkgStage = busiPkgStage;
    }


    public Integer getBusiPkgReference() {
        return busiPkgReference;
    }


    public void setBusiPkgReference(Integer busiPkgReference) {
        this.busiPkgReference = busiPkgReference;
    }


    public Integer getUploadWay() {
        return uploadWay;
    }


    public void setUploadWay(Integer uploadWay) {
        this.uploadWay = uploadWay;
    }


    public String getPath() {
        return path;
    }


    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }


    public Integer getHostConfId() {
        return hostConfId;
    }


    public void setHostConfId(Integer hostConfId) {
        this.hostConfId = hostConfId;
    }


    public String getHostIp() {
        return hostIp;
    }


    public void setHostIp(String hostIp) {
        this.hostIp = hostIp == null ? null : hostIp.trim();
    }


    public Integer getPort() {
        return port;
    }


    public void setPort(Integer port) {
        this.port = port;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }


    public String getPasswd() {
        return passwd;
    }


    public void setPasswd(String passwd) {
        this.passwd = passwd == null ? null : passwd.trim();
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


    public String getUploadName() {
        return uploadName;
    }

    public void setUploadName(String uploadName) {
        this.uploadName = uploadName;
    }

    @Override
    public String toString() {
        return "BusiPkgVersionDetail{" +
                "busiPkgVersionId=" + busiPkgVersionId +
                ", busiPkgId=" + busiPkgId +
                ", busiPkgVersion='" + busiPkgVersion + '\'' +
                ", busiPkgVersionSize=" + busiPkgVersionSize +
                ", busiPkgPostfix='" + busiPkgPostfix + '\'' +
                ", busiPkgVersionRemark='" + busiPkgVersionRemark + '\'' +
                ", busiPkgStatus=" + busiPkgStatus +
                ", busiPkgStage=" + busiPkgStage +
                ", busiPkgReference=" + busiPkgReference +
                ", uploadWay=" + uploadWay +
                ", uploadName='" + uploadName + '\'' +
                ", path='" + path + '\'' +
                ", hostConfId=" + hostConfId +
                ", hostIp='" + hostIp + '\'' +
                ", port=" + port +
                ", userName='" + userName + '\'' +
                ", passwd='" + passwd + '\'' +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}