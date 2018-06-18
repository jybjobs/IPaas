package com.cloud.paas.appservice.dao;

import com.cloud.paas.appservice.model.SrvPersistentRel;

import java.util.*;

public interface SrvPersistentRelDAO extends BaseDAO<SrvPersistentRel> {

    /**
     * 根据服务编号查询持久化目录
     * @param srvId
     * @return
     */
    public List<SrvPersistentRel> doFindBySrvId(Integer srvId);
}