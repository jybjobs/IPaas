package com.cloud.paas.taskmanager.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SrvDetail extends ValueObject {
    /**
     * 服务id
     */
    @NotNull(message = "{srvs.srvId.notNull}", groups = SrvUpdateValidate.class)
    private Integer srvId;

    /**
     * 服务标志的路径
     */
    private String srvDescImgPath;

    /**
     * 服务名称
     */
    @NotBlank(message = "{srvs.srvNameCh.notBlank}", groups = {SrvAddValidate.class, SrvUpdateValidate.class})
    private String srvNameCh;

    /**
     * 服务英文名称
     */
    @NotBlank(message = "{srvs.srvNameEn.notBlank}", groups = SrvAddValidate.class)
    private String srvNameEn;

    /**
     * 服务描述
     */
    private String srvDesc;

    /**
     * 服务类型
     */
    @NotNull(message = "{srvs.srvType.notNull}", groups = SrvAddValidate.class)
    private Byte srvType;

    /**
     * 业务包
     */
    @NotBlank(message = "{srvs.busiPkg.notBlank}", groups = SrvAddValidate.class)
    private String busiPkg;

    /**
     * 业务包编号
     */
    @NotNull(message = "{srvs.busiPkgId.notNull}", groups = SrvAddValidate.class)
    private Integer busiPkgId;

    /**
     * 镜像名称
     */
    @NotBlank(message = "{srvs.srvImage.notBlank}", groups = SrvAddValidate.class)
    private String srvImage;

    /**
     * 镜像id
     */
    @NotNull(message = "{srvs.srvImageId.notNull}", groups = SrvAddValidate.class)
    private Integer srvImageId;

    /**
     * 镜像版本
     */
    @NotBlank(message = "{srvs.srvImageVersion.notBlank}", groups = SrvAddValidate.class)
    private String srvImageVersion;
    /**
     * 镜像版本id
     */
    @NotNull(message = "{srvs.srvImageVersionId.notNull}", groups = SrvAddValidate.class)
    private Integer srvImageVersionId;

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
     * 租户编号
     */
    private int tenantId;

    /**
     * 代码管理类型
     */
    private int codeMngType;

    /**
     * 代码地址
     */
    private String url;

    /**
     * 分支数组（json）
     */
    private String branch;

    /**
     * 代码仓库用户名
     */
    private String userName;

    /**
     * 代码仓库密码
     */
    private String password;

    /**
     * 标签数组（json）
     */
    private String tags;

    /**
     * 成员数组（json）
     */
    private String memberUsers;

    /**
     * 语言
     */
    private String language;

    /**
     * 语言版本
     */
    private String langVersion;

    /**
     * 构建脚本类型（maven或ant）
     */
    private String buildScriptType;

    /**
     * 构建脚本路径
     */
    private String buildScriptPath;

    /**
     * 项目打包路径
     */
    private String projectBuildPath;

    /**
     * 中间件
     */
    private String middleware;

    /**
     * 中间件版本
     */
    private String middlewareVersion;

    /**
     * 日志路径
     */
    private String logPath;

    /**
     * 持久化路径
     */
    private String persisPath;


    /**
     * 镜服务校验分组
     */
    public interface SrvAddValidate {
    }

    public interface SrvUpdateValidate {
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getSrvId() {
        return srvId;
    }

    public void setSrvId(Integer srvId) {
        this.srvId = srvId;
    }

    public String getSrvNameCh() {
        return srvNameCh;
    }

    public void setSrvNameCh(String srvNameCh) {
        this.srvNameCh = srvNameCh == null ? null : srvNameCh.trim();
    }

    public String getSrvNameEn() {
        return srvNameEn;
    }

    public void setSrvNameEn(String srvNameEn) {
        this.srvNameEn = srvNameEn == null ? null : srvNameEn.trim();
    }

    public String getSrvDescImgPath() {
        return srvDescImgPath;
    }

    public void setSrvDescImgPath(String srvDescImgPath) {
        this.srvDescImgPath = srvDescImgPath;
    }

    public String getSrvDesc() {
        return srvDesc;
    }

    public void setSrvDesc(String srvDesc) {
        this.srvDesc = srvDesc;
    }

    public Byte getSrvType() {
        return srvType;
    }

    public void setSrvType(Byte srvType) {
        this.srvType = srvType;
    }

    public String getBusiPkg() {
        return busiPkg;
    }

    public void setBusiPkg(String busiPkg) {
        this.busiPkg = busiPkg;
    }

    public Integer getBusiPkgId() {
        return busiPkgId;
    }

    public void setBusiPkgId(Integer busiPkgId) {
        this.busiPkgId = busiPkgId;
    }

    public String getSrvImage() {
        return srvImage;
    }

    public void setSrvImage(String srvImage) {
        this.srvImage = srvImage == null ? null : srvImage.trim();
    }

    public Integer getSrvImageId() {
        return srvImageId;
    }

    public void setSrvImageId(Integer srvImageId) {
        this.srvImageId = srvImageId;
    }

    public String getSrvImageVersion() {
        return srvImageVersion;
    }

    public void setSrvImageVersion(String srvImageVersion) {
        this.srvImageVersion = srvImageVersion == null ? null : srvImageVersion.trim();
    }

    public Integer getSrvImageVersionId() {
        return srvImageVersionId;
    }

    public void setSrvImageVersionId(Integer srvImageVersionId) {
        this.srvImageVersionId = srvImageVersionId;
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
        this.creator = creator == null ? null : creator.trim();
    }

    public int getCodeMngType() {
        return codeMngType;
    }

    public void setCodeMngType(int codeMngType) {
        this.codeMngType = codeMngType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getMemberUsers() {
        return memberUsers;
    }

    public void setMemberUsers(String memberUsers) {
        this.memberUsers = memberUsers;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLangVersion() {
        return langVersion;
    }

    public void setLangVersion(String langVersion) {
        this.langVersion = langVersion;
    }

    public String getBuildScriptType() {
        return buildScriptType;
    }

    public void setBuildScriptType(String buildScriptType) {
        this.buildScriptType = buildScriptType;
    }

    public String getBuildScriptPath() {
        return buildScriptPath;
    }

    public void setBuildScriptPath(String buildScriptPath) {
        this.buildScriptPath = buildScriptPath;
    }

    public String getProjectBuildPath() {
        return projectBuildPath;
    }

    public void setProjectBuildPath(String projectBuildPath) {
        this.projectBuildPath = projectBuildPath;
    }

    public String getMiddleware() {
        return middleware;
    }

    public void setMiddleware(String middleware) {
        this.middleware = middleware;
    }

    public String getMiddlewareVersion() {
        return middlewareVersion;
    }

    public void setMiddlewareVersion(String middlewareVersion) {
        this.middlewareVersion = middlewareVersion;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getPersisPath() {
        return persisPath;
    }

    public void setPersisPath(String persisPath) {
        this.persisPath = persisPath;
    }
}