package com.cloud.paas.systemmanager.dao;

import com.cloud.paas.systemmanager.model.TenantInfo;
import com.cloud.paas.systemmanager.qo.tenant.TenantCondition;
import com.cloud.paas.systemmanager.vo.dictory.TenantInfoVO;

import java.util.List;

/**
 * @Author: weiwei
 * @Description:租户管理dao
 * @Date: Create  2018/1/11
 * @Modified by:
 */
public interface TenantInfoDAO extends BaseDAO<TenantInfo>{
    /**
     * 获取全量租户列表
     * @return
     */
    List<TenantInfoVO> listTenants(TenantCondition tenantCondition);


    /**
     * 获取指定租户信息
     * @param managerUserId
     * @return
     */
    TenantInfoVO getTenantDetailInfo(Integer managerUserId);


    /**
     * 根据租户名查询租户信息
     * @param tenantName
     * @return
     */
    TenantInfo doFindByTenantName(String tenantName);
}
