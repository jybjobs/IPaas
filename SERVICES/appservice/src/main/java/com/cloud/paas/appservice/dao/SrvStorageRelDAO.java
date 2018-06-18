package com.cloud.paas.appservice.dao;

import com.cloud.paas.appservice.model.SrvStorageRel;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author
 * @create 2017-12-18 18:40
 **/
public interface SrvStorageRelDAO extends BaseDAO<SrvStorageRel>{
    /**
     * 查询存储详情
     * @param srvId
     * @return
     */
    List<SrvStorageRel> doFindBySrvId(int srvId);
}
