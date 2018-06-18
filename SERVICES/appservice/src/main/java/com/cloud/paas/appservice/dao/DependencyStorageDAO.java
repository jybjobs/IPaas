package com.cloud.paas.appservice.dao;

import com.cloud.paas.appservice.model.DependencyStorage;
import java.util.List;

public interface DependencyStorageDAO extends BaseDAO<DependencyStorage> {

    public List<DependencyStorage> doFindByDeploymentId (Integer deploymentId);

}