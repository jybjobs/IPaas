package com.cloud.paas.appservice.dao;

import com.cloud.paas.appservice.model.SrvEnvRel;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author YHT
 * @create 2017-12-14 16:16
 **/
public interface SrvEnvRelDAO extends BaseDAO<SrvEnvRel>{
    /**
     * 获取环境变量
     * @param srvId
     * @return
     */
    List<SrvEnvRel> doFindBySrvId(int srvId);
}
