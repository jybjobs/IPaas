package com.cloud.paas.appservice.dao;

import com.cloud.paas.appservice.model.SrvRealtimeState;

import java.util.List;

/**
 * @Author: wyj
 * @desc:
 * @Date: Created in 2017-12-20 14:16
 * @Modified By:
 */
public interface SrvRealtimeStateDAO extends  BaseDAO<SrvRealtimeState>{

    /**
     * 根据服务状态，查询详细信息
     * @param state
     * @return
     */
    List<SrvRealtimeState> doSearchListByState(int state);
    public SrvRealtimeState getSrvRealtimeState(Integer id)throws  Exception;
}
