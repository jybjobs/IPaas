package com.cloud.paas.taskmanager.entity;

/**
 * @Author: srf
 * @desc: ImageBuildParams对象
 * @Date: Created in 2018/5/23 12:42
 * @Modified By:
 */
public class ImageBuildParams {
    private int userId;
    private int srvId;
    private String version;
    private String workspacePath;
    private String projectCompress;
    private String imageUrl;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSrvId() {
        return srvId;
    }

    public void setSrvId(int srvId) {
        this.srvId = srvId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getWorkspacePath() {
        return workspacePath;
    }

    public void setWorkspacePath(String workspacePath) {
        this.workspacePath = workspacePath;
    }

    public String getProjectCompress() {
        return projectCompress;
    }

    public void setProjectCompress(String projectCompress) {
        this.projectCompress = projectCompress;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
