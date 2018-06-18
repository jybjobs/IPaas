package com.cloud.paas.imageregistry.vo.dockerfile;

import net.minidev.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * @Author: kaiwen
 * @desc: dockerfile的一个包装类
 * @Date: Created by kaiwen on 2017/11/28.
 * @Modified By:
 */
public class DockerFileDetailVO  {

    /**
     * dockerfile编号
     */
    private Integer dockerfileId;
    /**
     * 基础镜像名称
     */
    private String baseImageEn;
    /**
     * 基础镜像版本
     */
    private String baseImageVersion;
    /**
     * 启动命令
     */
    private String[] cmd;
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

    /**
     * 暴露端口
     */
    private List<String> port;
    /**
     * 安装路径
     */
    private JSONObject addPath;
    /**
     * 环境变量
     */
    private JSONObject env;
    /**
     * 执行命令
     */
    private String[] run;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
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
        this.baseImageEn = baseImageEn;
    }

    public String getBaseImageVersion() {
        return baseImageVersion;
    }

    public void setBaseImageVersion(String baseImageVersion) {
        this.baseImageVersion = baseImageVersion;
    }

    public String[] getCmd() {
        return cmd;
    }

    public void setCmd(String[] cmd) {
        this.cmd = cmd;
    }

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

    public List<String> getPort() {
        return port;
    }

    public void setPort(List<String> port) {
        this.port = port;
    }

    public JSONObject getAddPath() {
        return addPath;
    }

    public void setAddPath(JSONObject addPath) {
        this.addPath = addPath;
    }

    public JSONObject getEnv() {
        return env;
    }

    public void setEnv(JSONObject env) {
        this.env = env;
    }

    public String[] getRun() {
        return run;
    }

    public void setRun(String[] run) {
        this.run = run;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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
        this.content = content;
    }
}
