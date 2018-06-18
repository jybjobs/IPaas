package com.cloud.paas.appservice.dao.impl.srv;

import com.cloud.paas.appservice.dao.SrvRealtimeStateDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;
import com.cloud.paas.appservice.model.SrvRealtimeState;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: wyj
 * @desc: 服务状态操作dao实现类
 * @Date: Created in 2017-12-20 14:22
 * @Modified By:
 */
@Repository("SrvRealtimeStateDAO")
public class SrvRealtimeStateDAOImpl extends BaseDAOImpl<SrvRealtimeState> implements SrvRealtimeStateDAO {

    /**
     * 服务实时状态的mapper.xml的namespace
     */
    public static final String NAME_SPACE = "SrvRealtimeStateDAO";

    public String getNameSpace() {
        return NAME_SPACE;
    }

    /**
     * 根据状态查询服务状态
     * @param state
     * @return
     */
    public List<SrvRealtimeState> doSearchListByState(int state) {

        return sqlSessionTemplate.selectList(this.getNameSpace()+".doSearchListByState",state);
    }

    public SrvRealtimeState getSrvRealtimeState(Integer id)throws  Exception{
        return sqlSessionTemplate.selectOne(this.getNameSpace()+".getSrvRealtimeState",id);

    }
}
