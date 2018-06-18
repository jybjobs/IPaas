package com.cloud.paas.imageregistry.qo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class ImageSCP {
    /**
     * 远端服务器的IP地址
     */
    private String ip;
    /**
     * 远端服务器的端口
     */
    private int port;
    /**
     * 远端服务器的用户名
     */

    private String user;

    /**
     * 远端服务器的密码
     */
    private String password;
    /**
     * 远端服务器选择的路径
     */
    /**
     * 远端服务器选择的路径
     */
    private String remotePath;
    /**
     * 远端服务器选择的路径文件
     */
    private String remoteFile;
    /**
     /**
     * 上传使用本地地址路径文件
     */
    private String localFile;
    /**
     * 镜像描述
     */
    private String imageRemark;
    /**
     * 镜像版本
     */
    @NotNull(message = "镜像版本不能为空")
    @NotBlank(message = "镜像版本不能为空")
    private String imageVersion; /**
     * 镜像当前所在仓库
     */
    @NotNull(message = "请选择仓库")
    private int registryId;

    /**
     * 镜像权限
     */
    private Integer imageAuth;
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getRemoteFile() {
        return remoteFile;
    }

    public void setRemoteFile(String remoteFile) {
        this.remoteFile = remoteFile;
    }

    public String getLocalFile() {
        return localFile;
    }

    public void setLocalFile(String localFile) {
        this.localFile = localFile;
    }

    public String getImageRemark() {
        return imageRemark;
    }

    public void setImageRemark(String imageRemark) {
        this.imageRemark = imageRemark;
    }
    

    public String getImageVersion() {
        return imageVersion;
    }

    public void setImageVersion(String imageVersion) {
        this.imageVersion = imageVersion;
    }

    public int getRegistryId() {
        return registryId;
    }

    public void setRegistryId(int registryId) {
        this.registryId = registryId;
    }

    public Integer getImageAuth() {
        return imageAuth;
    }

    public void setImageAuth(Integer imageAuth) {
        this.imageAuth = imageAuth;
    }

}
