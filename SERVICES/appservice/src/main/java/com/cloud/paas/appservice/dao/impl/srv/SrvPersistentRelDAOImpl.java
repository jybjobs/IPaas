package com.cloud.paas.appservice.dao.impl.srv;

import com.cloud.paas.appservice.dao.SrvPersistentRelDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;
import com.cloud.paas.appservice.model.SrvPersistentRel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 17798 on 2018/3/28.
 */
@Repository
public class SrvPersistentRelDAOImpl extends BaseDAOImpl<SrvPersistentRel> implements SrvPersistentRelDAO {

    /**
     * 服务操作的mapper.xml的namespace
     */
    public static final String NAME_SPACE = "SrvPersistentRelDAO";

    @Override
    public String getNameSpace() {
        return this.NAME_SPACE;
    }

    /**
     * 根据服务编号查询持久化目录
     * @param srvId
     * @return
     */
    @Override
    public List<SrvPersistentRel> doFindBySrvId(Integer srvId) {
        return this.sqlSessionTemplate.selectList(this.getNameSpace()+".doFindBySrvId",srvId);
    }
}
