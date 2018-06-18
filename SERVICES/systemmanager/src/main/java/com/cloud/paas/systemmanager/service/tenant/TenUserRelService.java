package com.cloud.paas.systemmanager.service.tenant;

import com.cloud.paas.systemmanager.model.TenUserRel;
import com.cloud.paas.systemmanager.service.BaseService;
import com.cloud.paas.systemmanager.vo.dictory.TenUserRelVO;
import com.cloud.paas.util.result.Result;

public interface TenUserRelService extends BaseService<TenUserRel> {

    /**
     * 创建租户人员
     */
    Result doInsertByTenantid(String userid, TenUserRelVO TenUserRelList) throws Exception;

    /**
     * 修改租户人员
     * @param userid
     * @param tenUserRelVO
     * @return
     */
    Result updateTenantUsers(String userid,TenUserRelVO tenUserRelVO) throws Exception;
}
