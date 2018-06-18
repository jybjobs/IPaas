package com.cloud.paas.appservice.dao.impl.srv;/**
 * @author
 * @create 2017-12-18 14:25
 **/

import com.cloud.paas.appservice.dao.SrvScaleRuleDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;
import com.cloud.paas.appservice.model.SrvScaleRule;
import org.springframework.stereotype.Repository;

/**
 *
 * @author yht
 * @create 2017-12-18 14:25
 **/
@Repository("srvScaleRuleDAO")
public class SrvScaleRuleDAOImpl extends BaseDAOImpl<SrvScaleRule> implements SrvScaleRuleDAO {
    public static final String NAME_SPACE = "SrvScaleRuleDAO";
    public String getNameSpace() {
        return NAME_SPACE;
    }

    @Override
    public SrvScaleRule doFindBySrvId(int srvId) {
        // TODO 未验证
        return sqlSessionTemplate.selectOne (this.getNameSpace ()+".doFindBySrvId",srvId);
    }

    /**
     * 根据服务实例编号获取扩缩容规则
     * @param srvInstId
     * @return
     */
    @Override
    public SrvScaleRule doFindBySrvInstId(int srvInstId) {
        return this.sqlSessionTemplate.selectOne(this.getNameSpace() + ".doFindBySrvInstId",srvInstId);
    }
}
