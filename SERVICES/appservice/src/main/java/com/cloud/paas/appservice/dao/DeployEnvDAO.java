package com.cloud.paas.appservice.dao;

import com.cloud.paas.appservice.model.DeployEnv;
import com.cloud.paas.appservice.qo.DeployEnvExample;

import java.util.List;

/**
 * Created by 17798 on 2018/6/3.
 */
public interface DeployEnvDAO extends BaseDAO<DeployEnv> {

    /**
     * 根据部署编号查询环境变量
     * @param deployEnvId
     * @return
     */
    List<DeployEnv> doFindByDeploymentId(Integer deployEnvId);

    /**
     * 根据key查询环境变量
     * @param deployEnvKey
     * @return
     */
    List<DeployEnv> doFindByDeployEnvKey(String deployEnvKey);

    /**
     * 根据条件查询环境变量列表
     * @param deployEnvExample
     * @return
     */
    List<DeployEnv> doFindDeployEnvList(DeployEnvExample deployEnvExample);

}
