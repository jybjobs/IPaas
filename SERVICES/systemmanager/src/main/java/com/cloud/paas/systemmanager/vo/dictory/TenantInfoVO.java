package com.cloud.paas.systemmanager.vo.dictory;

import com.cloud.paas.systemmanager.model.TenantInfo;
import com.cloud.paas.systemmanager.model.User;

/**
 * 租户信息继承类
 */
public class TenantInfoVO extends TenantInfo {


    /**
     * 租户人员列表
     */
    private User tenantusers;

    public User getTenantusers() {
        return tenantusers;
    }

    public void setTenantusers(User tenantusers) {
        this.tenantusers = tenantusers;
    }
}
