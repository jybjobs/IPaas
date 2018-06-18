package com.cloud.paas.appservice.dao.impl.srv;

import com.cloud.paas.appservice.dao.SrvDeploymentDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;
import com.cloud.paas.appservice.model.SrvDeployment;
import com.cloud.paas.appservice.qo.SrvDeploymentExample;
import com.cloud.paas.appservice.vo.srv.SrvDeploymentVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SrvDeploymentDAOImpl  extends BaseDAOImpl<SrvDeployment> implements SrvDeploymentDAO {

    public static final String NAME_SPACE = "SrvDeploymentDAO";

    @Override
    public String getNameSpace() {
        return NAME_SPACE;
    }

    @Override
    public List<SrvDeploymentVO> listSrvDeploymentWithSrvInst(SrvDeploymentExample srvDeploymentExample) {
        return this.sqlSessionTemplate.selectList(this.getNameSpace() + ".listSrvDeploymentWithSrvInst", srvDeploymentExample);
    }

    @Override
    public List<SrvDeployment> doFindBySrvId(Integer srvId) {
        return  this.sqlSessionTemplate.selectList(this.getNameSpace() + ".doFindBySrvId", srvId);
    }
}