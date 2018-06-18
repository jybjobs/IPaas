package com.cloud.paas.taskmanager.dao;

import com.cloud.paas.taskmanager.model.TaskConfig;

import java.util.List;

public interface TaskConfigDAO extends BaseDAO<TaskConfig> {
    public List<Integer> doFindAll(String tenantid, int srvId);
}
