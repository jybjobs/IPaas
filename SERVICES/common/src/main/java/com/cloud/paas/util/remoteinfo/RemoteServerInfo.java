package com.cloud.paas.util.remoteinfo;

import com.cloud.paas.model.ValueObject;

/**
 * Created by CSS on 2018/1/12.
 */
public class RemoteServerInfo extends ValueObject {
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
    private String remotePath;
    /**
     * 远端服务器选择的路径文件
     */
    private String remoteFile;
    /**
     * 本地选择的路径
     */
    private String localPath;
    /**
     /**
     * 上传使用本地地址路径文件
     */
    private String localFile;

    public RemoteServerInfo() {
    }

    public RemoteServerInfo(String ip, int port, String user, String password,String remoteFile) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
        this.remoteFile = remoteFile;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getLocalFile() {
        return localFile;
    }

    public void setLocalFile(String localFile) {
        this.localFile = localFile;
    }



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

    public String getRemoteFile() {
        return remoteFile;
    }

    public void setRemoteFile(String remoteFile) {
        this.remoteFile = remoteFile;
    }

    @Override
    public String toString() {
        return "RemoteServerInfo{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", remotePath='" + remotePath + '\'' +
                ", remoteFile='" + remoteFile + '\'' +
                ", localPath='" + localPath + '\'' +
                ", localFile='" + localFile + '\'' +
                '}';
    }
}
