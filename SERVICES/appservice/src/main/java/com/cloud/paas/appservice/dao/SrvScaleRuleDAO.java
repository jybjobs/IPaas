package com.cloud.paas.appservice.dao;

import com.cloud.paas.appservice.model.SrvScaleRule;

/**
 *
 * @author yht
 * @create 2017-12-18 14:25
 **/
public interface SrvScaleRuleDAO extends BaseDAO<SrvScaleRule> {
    /**
     * 获取扩缩容规则
     * @param srvId
     * @return
     */
    SrvScaleRule doFindBySrvId(int srvId);

    /**
     * 根据服务实例编号获取扩缩容规则
     * @param srvInstId
     * @return
     */
    SrvScaleRule doFindBySrvInstId(int srvInstId);
}
