package com.cloud.paas.taskmanager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

public class TaskConfig extends ValueObject {
    /**
     *任务配置编号
     */
    private Integer taskConfigId;

    /**
     *服务编号
     */
    private Integer srvId;

    /**
     *任务配置Json串
     */
    @NotBlank(message = "{taskManager.taskConfigJson.notBlank}")
    private String taskConfigJson;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 最后修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建者
     */
    private String creator;

    public Integer getTaskConfigId() {
        return taskConfigId;
    }

    public void setTaskConfigId(Integer taskConfigId) {
        this.taskConfigId = taskConfigId;
    }

    public Integer getSrvId() {
        return srvId;
    }

    public void setSrvId(Integer srvId) {
        this.srvId = srvId;
    }

    public String getTaskConfigJson() {
        return taskConfigJson;
    }

    public void setTaskConfigJson(String taskConfigJson) {
        this.taskConfigJson = taskConfigJson;
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
}
