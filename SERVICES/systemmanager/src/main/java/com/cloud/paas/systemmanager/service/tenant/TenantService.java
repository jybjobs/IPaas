package com.cloud.paas.systemmanager.service.tenant;

import com.cloud.paas.systemmanager.model.TenantInfo;
import com.cloud.paas.systemmanager.qo.tenant.TenantCondition;
import com.cloud.paas.systemmanager.service.BaseService;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.result.Result;

/**
 * 租户service
 */
public interface TenantService extends BaseService<TenantInfo> {

    /**
     * 创建租户
     * @param userid
     * @param tenantInfo
     * @return
     * @throws Exception
     */
    Result doInsertByTenantInfo(String userid,TenantInfo tenantInfo) throws Exception;

    /**
     * 获取全量租户列表
     * @return
     * @throws Exception
     */
    Result listTenants(TenantCondition tenantCondition) throws BusinessException;

    /**
     * 修改租户
     * @param userid
     * @param tenantInfo
     * @return
     */
    Result updateTenant(String userid,TenantInfo tenantInfo) throws Exception;


    /**
     * 获取指定租户信息
     * @param managerUserId
     * @return
     * @throws Exception
     */
    Result getTenantDetailInfo(Integer managerUserId) throws Exception;


    /**
     * 根据租户名查询租户信息
     * @param userid
     * @param tenantName
     * @return
     * @throws Exception
     */
    Result getTenantInfobyTenantName(String userid,String tenantName)throws Exception;

    /**
     * 删除租户
     * @param userId
     * @param tenantId
     * @return
     * @throws Exception
     */
    Result deleteTenantById(String userId, Integer tenantId) throws Exception;


}
