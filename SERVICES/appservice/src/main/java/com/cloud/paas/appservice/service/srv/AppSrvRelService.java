package com.cloud.paas.appservice.service.srv;

import com.cloud.paas.appservice.model.AppSrvRel;
import com.cloud.paas.appservice.service.BaseService;

/**
 * Created by CSS on 2017/12/19.
 */
public interface AppSrvRelService extends BaseService<AppSrvRel> {
    int doDeleteBySrvId(int srvId);
}
