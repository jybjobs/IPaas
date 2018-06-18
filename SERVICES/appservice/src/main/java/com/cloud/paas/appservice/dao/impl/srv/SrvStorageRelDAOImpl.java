package com.cloud.paas.appservice.dao.impl.srv;/**
 * @author
 * @create 2017-12-18 18:41
 **/

import com.cloud.paas.appservice.dao.SrvStorageRelDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;
import com.cloud.paas.appservice.model.SrvStorageRel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author yht
 * @create 2017-12-18 18:41
 **/
@Repository("srvStorageRelDAO")
public class SrvStorageRelDAOImpl extends BaseDAOImpl<SrvStorageRel> implements SrvStorageRelDAO {
    public static final String NAME_SPACE = "SrvStorageRelDAO";

    public String getNameSpace() {
        return NAME_SPACE;
    }


    @Override
    public List<SrvStorageRel> doFindBySrvId(int srvId) {
        return sqlSessionTemplate.selectList (this.getNameSpace ()+".doFindBySrvId",srvId);
    }
}
