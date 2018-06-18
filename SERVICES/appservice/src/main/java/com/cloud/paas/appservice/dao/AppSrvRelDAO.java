package com.cloud.paas.appservice.dao;

import com.cloud.paas.appservice.model.AppSrvRel;

/**
 * Created by CSS on 2017/12/19.
 */
public interface AppSrvRelDAO  extends BaseDAO<AppSrvRel>{
    /**
     * 删除SrvId对应的关系记录
     * @param srvId
     * @return
     */
    int doDeleteBySrvId(int srvId);
}
