package com.cloud.paas.appservice.dao.impl.srv;

import com.cloud.paas.appservice.dao.OperationLogDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;
import com.cloud.paas.appservice.model.OperationLog;
import org.springframework.stereotype.Repository;

/**
 * Created by kaiwen on 2017/12/18.
 */
@Repository
public class OperationLogDAOImpl extends BaseDAOImpl<OperationLog> implements OperationLogDAO {
    public static final String NAME_SPACE = "com.cloud.paas.appservice.dao.OperationLogMapper";

    @Override
    public String getNameSpace() {
        return NAME_SPACE;
    }
}
