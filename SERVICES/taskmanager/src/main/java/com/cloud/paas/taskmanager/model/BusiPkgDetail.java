package com.cloud.paas.taskmanager.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class BusiPkgDetail extends ValueObject {
    /**
     * 业务包id
     */
    private Integer busiPkgId;
    /**
     * 业务包名称
     */
    @NotBlank(message = "{busipkg.busipkgnamezh.notblank}",groups = BusiPkgAddValidate.class)
    private String busiPkgNameZh;
    /**
     * 业务包英文名
     */
    @NotBlank(message = "{busipkg.busipkgnameen.notblank}",groups = BusiPkgAddValidate.class)
    private String busiPkgNameEn;
    /**
     * 业务包类型
     */
    @NotNull(message = "{busipkg.busipkgbusipkgtype.notnull}",groups = BusiPkgAddValidate.class)
    private Integer busiPkgType;
    /**
     * 业务包描述
     */
    private String busiPkgRemark;
    /**
     * 业务包权限
     */
    @NotNull(message = "{busipkg.busipkgbusipkgauth.notnull}",groups = BusiPkgAddValidate.class)
    private Integer busiPkgAuth;
    /**
     * 业务包大小
     */
    private BigDecimal busiPkgSize;
    /**
     * 图片存放路径
     */
    private String busiPkgImgPath;
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

    /**************************业务包改造 开始*************************/
    /**
     * 上传方式
     */
    private Integer uploadWay;

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
     * 拉取文件
     */
    private String remoteFile;

    /**************************业务包改造 结束*************************/

    /**
     * 业务包校验分组
     */
    public interface BusiPkgAddValidate{};

    public Integer getBusiPkgId() {
        return busiPkgId;
    }

    public void setBusiPkgId(Integer busiPkgId) {
        this.busiPkgId = busiPkgId;
    }

    public String getBusiPkgNameZh() {
        return busiPkgNameZh;
    }

    public void setBusiPkgNameZh(String busiPkgNameZh) {
        this.busiPkgNameZh = busiPkgNameZh == null ? null : busiPkgNameZh.trim();
    }

    public String getBusiPkgNameEn() {
        return busiPkgNameEn;
    }

    public void setBusiPkgNameEn(String busiPkgNameEn) {
        this.busiPkgNameEn = busiPkgNameEn == null ? null : busiPkgNameEn.trim();
    }

    public Integer getBusiPkgType() {
        return busiPkgType;
    }

    public void setBusiPkgType(Integer busiPkgType) {
        this.busiPkgType = busiPkgType;
    }

    public String getBusiPkgRemark() {
        return busiPkgRemark;
    }

    public void setBusiPkgRemark(String busiPkgRemark) {
        this.busiPkgRemark = busiPkgRemark == null ? null : busiPkgRemark.trim();
    }

    public Integer getBusiPkgAuth() {
        return busiPkgAuth;
    }

    public void setBusiPkgAuth(Integer busiPkgAuth) {
        this.busiPkgAuth = busiPkgAuth;
    }

    public BigDecimal getBusiPkgSize() {
        return busiPkgSize;
    }

    public void setBusiPkgSize(BigDecimal busiPkgSize) {
        this.busiPkgSize = busiPkgSize;
    }

    public String getBusiPkgImgPath() {
        return busiPkgImgPath;
    }

    public void setBusiPkgImgPath(String busiPkgImgPath) {
        this.busiPkgImgPath = busiPkgImgPath;
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

    /******************************************************/
    public Integer getUploadWay() {
        return uploadWay;
    }

    public void setUploadWay(Integer uploadWay) {
        this.uploadWay = uploadWay;
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
        this.hostIp = hostIp;
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
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getRemoteFile() {
        return remoteFile;
    }

    public void setRemoteFile(String remoteFile) {
        this.remoteFile = remoteFile;
    }
}