package com.cloud.paas.systemmanager.service.tenant;

import com.cloud.paas.systemmanager.model.ResourceInfo;
import com.cloud.paas.systemmanager.service.BaseService;
import com.cloud.paas.util.result.Result;

/**
 * 资源service
 */
public interface ResourceService extends BaseService<ResourceInfo> {
    /**
     * 创建资源
     */
    Result doInsertByResourceInfo(String userid,ResourceInfo resourceInfo) throws Exception;

    /**
     * 获取全量资源列表
     * @return
     * @throws Exception
     */
    Result listResources() throws Exception;

    /**
     * 修改资源
     * @param userid
     * @param resourceInfo
     * @return
     */
    Result updateResource(String userid,ResourceInfo resourceInfo) throws Exception;

    /**
     * 获取剩余资源配额
     * @return
     * @throws Exception
     */
    Result getRestResource(String userid)throws Exception;


}
