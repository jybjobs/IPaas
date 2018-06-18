package com.cloud.paas.appservice.service.srv;

import com.cloud.paas.appservice.model.DeployEnv;
import com.cloud.paas.appservice.qo.DeployEnvExample;
import com.cloud.paas.appservice.qo.SrvDeploymentExample;
import com.cloud.paas.util.result.Result;

/**
 * Created by 17798 on 2018/5/17.
 */
public interface SrvDeploymentService {

    /**
     * devops创建镜像版本并创建dockerfile
     * @param userid
     * @param srvId
     * @return
     */
    Result createImageVersionAndDockerfile(String userid,Integer srvId,String version);

    /**
     * 创建服务版本
     * @param userid
     * @param srvId
     * @param version
     * @param imageVersionId
     * @return
     */
    Result createSrvVersion(String userid,Integer srvId,String version,Integer imageVersionId);

    /**
     * 查询部署实例信息
     * @param srvDeploymentExample
     * @return
     */
    Result listSrvDeploymentWithSrvInst(String userid, SrvDeploymentExample srvDeploymentExample);

    /**
     * 部署
     * @param userid
     * @param srvDeploymentExample
     * @return
     */
    public Result publish(String userid, SrvDeploymentExample srvDeploymentExample);

    /**
     * 升级部署
     * @param userid
     * @param srvDeploymentExample
     * @return
     */
    public Result update(String userid, SrvDeploymentExample srvDeploymentExample);

    /**
     * 部署服务实例停止
     * @param deploymentId
     * @return
     */
    public Result deploymentStop(Integer deploymentId);

    /**
     * 部署服务实例启动
     * @param deploymentId
     * @return
     */
    public Result deploymentStart(Integer deploymentId);

    /**
     * 更新服务部署
     * @param srvDeploymentExample
     * @return
     */
    public Result update(SrvDeploymentExample srvDeploymentExample);

    /**
     * 切换版本
     * @param userid
     * @param srvDeploymentExample
     * @return
     */
    public Result exchangeVersion(String userid, SrvDeploymentExample srvDeploymentExample);

    /**
     * 自动部署接口
     * @param userid
     * @param srvDeploymentExample
     */
    public void autoPublish(String userid, SrvDeploymentExample srvDeploymentExample);

    /**
     * 查询环境变量列表
     * @param userid
     * @param deployEnvExample
     */
    public Result doFindDeployEnvList(String userid, DeployEnvExample deployEnvExample);

    /**
     * 保存部署环境变量
     * @param userid
     * @param deployEnv
     * @return
     */
    public Result saveDeployEnv(String userid, DeployEnv deployEnv);

    /**
     * 删除部署环境变量
     * @param userid
     * @param deployEnvId
     * @return
     */
    public Result deleteDeployEnv(String userid, Integer deployEnvId);

    /**
     * 查询容器日志
     * @param podName
     * @param namespace
     * @return
     */
    public Result queryPodLog(String podName,String namespace);

}
