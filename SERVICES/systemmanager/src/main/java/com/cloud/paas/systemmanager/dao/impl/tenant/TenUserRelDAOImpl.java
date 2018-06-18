package com.cloud.paas.systemmanager.dao.impl.tenant;

import com.cloud.paas.systemmanager.dao.TenUserRelDAO;
import com.cloud.paas.systemmanager.dao.impl.BaseDAOImpl;
import com.cloud.paas.systemmanager.model.TenUserRel;
import org.springframework.stereotype.Service;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
@Service("tenUserRelDAO")
public class TenUserRelDAOImpl extends BaseDAOImpl<TenUserRel> implements TenUserRelDAO{
    public static final String NAME_SPACE = "TenUserRelDAO";

    @Override
    public  String getNameSpace(){
        return TenUserRelDAOImpl.NAME_SPACE;
    }
}
