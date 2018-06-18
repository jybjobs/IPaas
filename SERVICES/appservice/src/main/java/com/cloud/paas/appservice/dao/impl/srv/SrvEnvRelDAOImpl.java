package com.cloud.paas.appservice.dao.impl.srv;/**
 * @author
 * @create 2017-12-14 16:19
 **/

import com.cloud.paas.appservice.dao.SrvEnvRelDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;
import com.cloud.paas.appservice.model.SrvEnvRel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author yht
 * @create 2017-12-14 16:19
 **/
@Repository("srvEnvRelDAO")
public class SrvEnvRelDAOImpl extends BaseDAOImpl<SrvEnvRel> implements SrvEnvRelDAO {
    public static final String NAME_SPACE = "SrvEnvRelDAO";
    public String getNameSpace() {
        return NAME_SPACE;
    }

    @Override
    public List<SrvEnvRel> doFindBySrvId(int srvId) {
        return sqlSessionTemplate.selectList (this.getNameSpace ()+".doFindBySrvId",srvId);
    }
}
