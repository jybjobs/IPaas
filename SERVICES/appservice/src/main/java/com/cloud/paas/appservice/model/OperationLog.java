package com.cloud.paas.appservice.model;

import java.util.Date;

public class OperationLog extends ValueObject{
    /**
     *编号
     */
    private Integer logId;

    /**
     *操作id
     */
    private Integer srvOperationId;

    /**
     *文件名
     */
    private String fileName;

    /**
     *大小
     */
    private Integer fileSize;

    /**
     *日志
     */
    private String logContent;
    /**
     *创建时间
     */
    private Date createTime;
    /**
     *修改时间
     */
    private Date updateTime;
    /**
     *创建人
     */
    private String creator;

    public Integer getLogId() {
        return logId;
    }
    public void setLogId(Integer logId) {
        this.logId = logId;
    }
    public Integer getSrvOperationId() {
        return srvOperationId;
    }
    public void setSrvOperationId(Integer srvOperationId) {
        this.srvOperationId = srvOperationId;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }
    public Integer getFileSize() {
        return fileSize;
    }
    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
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
    public String getLogContent() {
        return logContent;
    }
    public void setLogContent(String logContent) {
        this.logContent = logContent == null ? null : logContent.trim();
    }
}