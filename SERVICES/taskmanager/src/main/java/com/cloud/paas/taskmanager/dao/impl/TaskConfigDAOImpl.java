package com.cloud.paas.taskmanager.dao.impl;

import com.cloud.paas.taskmanager.dao.TaskConfigDAO;
import com.cloud.paas.taskmanager.model.TaskConfig;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class TaskConfigDAOImpl extends BaseDAOImpl<TaskConfig> implements TaskConfigDAO {
    /**
     * 任务配置的mapper.xml的namespace
     */
    public static final String NAME_SPACE = "TaskConfigDAO";
    @Override
    public  String getNameSpace(){
        return this.NAME_SPACE;
    }

    public List<Integer> doFindAll(String tenantid, int srvId) {
        HashMap<String, Object> map = new HashMap();
        map.put("tenantId", tenantid);
        map.put("srvId", srvId);
        return sqlSessionTemplate.selectList(this.getNameSpace()+".doFindAll", map);
    }
}
