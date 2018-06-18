package com.cloud.paas.appservice.service.impl.srv;

import com.cloud.paas.appservice.dao.AppSrvRelDAO;
import com.cloud.paas.appservice.dao.BaseDAO;
import com.cloud.paas.appservice.model.AppSrvRel;
import com.cloud.paas.appservice.service.impl.BaseServiceImpl;
import com.cloud.paas.appservice.service.srv.AppSrvRelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by CSS on 2017/12/19.
 */
@Service("appSrvRelServiceImpl")
public class AppSrvRelServiceImpl extends BaseServiceImpl<AppSrvRel> implements AppSrvRelService {
    private static final Logger logger = LoggerFactory.getLogger(AppSrvRelServiceImpl.class);

    @Autowired
    AppSrvRelDAO appSrvRelDao;

    @Override
    public BaseDAO<AppSrvRel> getBaseDAO() {
        return appSrvRelDao;
    }
    public int doDeleteBySrvId(int srvId){
        return appSrvRelDao.doDeleteBySrvId(srvId);
    }
}
