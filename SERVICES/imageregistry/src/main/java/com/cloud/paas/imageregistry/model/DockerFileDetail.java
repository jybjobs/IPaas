package com.cloud.paas.imageregistry.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @Author: wyj
 * @desc: dockerfile详细信息实体类
 * @Date: Created in 2017/11/27 14:43
 * @Modified By:
 */
public class DockerFileDetail extends ValueObject {

    /**
     * dockerfile编号
     */
    private Integer dockerfileId;
    /**
     * 基础镜像名称
     */
    private String baseImageEn;
    /**
     * 业务包id
     */
    private Integer basePkgVersionId;
    /**
     * 镜像版本id
     */
    private Integer baseImageVersionId;
    /**
     *参数类型
     */
    private Integer configureType;

    public Integer getBasePkgVersionId() {
        return basePkgVersionId;
    }

    public void setBasePkgVersionId(Integer basePkgVersionId) {
        this.basePkgVersionId = basePkgVersionId;
    }

    public Integer getBaseImageVersionId() {
        return baseImageVersionId;
    }

    public void setBaseImageVersionId(Integer baseImageVersionId) {
        this.baseImageVersionId = baseImageVersionId;
    }

    public Integer getConfigureType() {
        return configureType;
    }

    public void setConfigureType(Integer configureType) {
        this.configureType = configureType;
    }

    /**

     * 基础镜像版本
     */
    private String baseImageVersion;
    /**
     * 启动命令
     */
    private String cmd;
    /**
     * 暴露端口
     */
    private String port;
    /**
     * 安装路径
     */
    private String addPath;
    /**
     * 环境变量
     */
    private String env;
    /**
     * 执行命令
     */
    private String run;
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
     * 更新时间
     */
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * dockerfile内容
     */
    private String content;

    public Integer getDockerfileId() {
        return dockerfileId;
    }

    public void setDockerfileId(Integer dockerfileId) {
        this.dockerfileId = dockerfileId;
    }

    public String getBaseImageEn() {
        return baseImageEn;
    }

    public void setBaseImageEn(String baseImageEn) {
        this.baseImageEn = baseImageEn == null ? null : baseImageEn.trim();
    }

    public String getBaseImageVersion() {
        return baseImageVersion;
    }

    public void setBaseImageVersion(String baseImageVersion) {
        this.baseImageVersion = baseImageVersion == null ? null : baseImageVersion.trim();
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd == null ? null : cmd.trim();
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port == null ? null : port.trim();
    }

    public String getAddPath() {
        return addPath;
    }

    public void setAddPath(String addPath) {
        this.addPath = addPath;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env == null ? null : env.trim();
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run == null ? null : run.trim();
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}