package com.cloud.paas.appservice.service.srv;

import com.cloud.paas.appservice.vo.srv.SrvDeploymentVO;
import com.cloud.paas.appservice.vo.srv.SrvInstDetailVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.result.Result;

/**
 * @Author: wyj
 * @desc: 服务操作实现接口
 * @Date: Created in 2017-12-20 8:57
 * @Modified By:
 */
public interface SrvImplementService {

    /**
     * 创建服务和启动
     * @param srvInstDetailVO 服务实例详情
     * @return 操作结果
     */
    void serviceCreator(SrvInstDetailVO srvInstDetailVO) throws Exception;

    /**
     * 更新服务和启动
     * @param srvInstDetailVO 服务实例详情
     * @return 操作结果
     */
    void serviceUpdator(SrvInstDetailVO srvInstDetailVO) throws Exception;

    /**
     * 停止服务
     * @param srvInstDetailVO 服务名称
     * @return 操作结果
     */
    void serviceDeletor(SrvInstDetailVO srvInstDetailVO) throws Exception;

    /**
     * 查询服务的详细信息
     * @param serviceName 服务名称
     * @return 查询结果
     */
    Result serviceQuery(String serviceName);

    /**
     * 创建PV
     * @param srvDeploymentVO
     * @return
     * @throws BusinessException
     */
    Result pvCreator (SrvDeploymentVO srvDeploymentVO, String type) throws BusinessException;

    /**
     * 创建PVC
     * @param srvDeploymentVO
     * @return
     * @throws BusinessException
     */
    Result pvcCreator (SrvDeploymentVO srvDeploymentVO, String type) throws BusinessException;

}
