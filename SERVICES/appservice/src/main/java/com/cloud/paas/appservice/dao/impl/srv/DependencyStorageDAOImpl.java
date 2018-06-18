package com.cloud.paas.appservice.dao.impl.srv;

import com.cloud.paas.appservice.dao.DependencyStorageDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;
import com.cloud.paas.appservice.model.DependencyStorage;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 17798 on 2018/6/7.
 */
@Repository("dependencyStorageDAO")
public class DependencyStorageDAOImpl extends BaseDAOImpl<DependencyStorage> implements DependencyStorageDAO {

    public static final String NAME_SPACE = "DependencyStorageDAO";

    @Override
    public String getNameSpace() {
        return this.NAME_SPACE;
    }


    @Override
    public List<DependencyStorage> doFindByDeploymentId(Integer deploymentId) {
        return this.sqlSessionTemplate.selectList(this.getNameSpace() + ".doFindByDeploymentId", deploymentId);
    }
}
