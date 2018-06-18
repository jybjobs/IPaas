package com.cloud.paas.appservice.dao.impl.srv;

import com.cloud.paas.appservice.dao.DeployEnvDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;
import com.cloud.paas.appservice.model.DeployEnv;
import com.cloud.paas.appservice.qo.DeployEnvExample;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created by 17798 on 2018/6/3.
 */
@Repository("deployEnvDAO")
public class DeployEnvDAOImpl extends BaseDAOImpl<DeployEnv> implements DeployEnvDAO {

    public static final String NAME_SPACE = "DeployEnvDAO";

    @Override
    public String getNameSpace() {
        return this.NAME_SPACE;
    }

    @Override
    public List<DeployEnv> doFindByDeploymentId(Integer deployEnvId) {
        return this.sqlSessionTemplate.selectList(this.getNameSpace() + ".doFindByDeploymentId", deployEnvId);
    }

    @Override
    public List<DeployEnv> doFindByDeployEnvKey(String deployEnvKey) {
        return this.sqlSessionTemplate.selectList(this.getNameSpace() + ".doFindByDeployEnvKey", deployEnvKey);
    }

    @Override
    public List<DeployEnv> doFindDeployEnvList(DeployEnvExample deployEnvExample) {
        return  this.sqlSessionTemplate.selectList(this.getNameSpace() + ".doFindDeployEnvList", deployEnvExample);
    }

}
