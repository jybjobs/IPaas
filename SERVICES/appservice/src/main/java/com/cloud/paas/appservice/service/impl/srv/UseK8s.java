package com.cloud.paas.appservice.service.impl.srv;/**
 * 异步调用方法
 *
 * @author
 * @create 2017-12-27 15:42
 **/

import com.cloud.paas.appservice.dao.DependencyStorageDAO;
import com.cloud.paas.appservice.dao.SrvDeploymentDAO;
import com.cloud.paas.appservice.dao.SrvMngDAO;
import com.cloud.paas.appservice.dao.SrvOperationDAO;
import com.cloud.paas.appservice.model.DependencyStorage;
import com.cloud.paas.appservice.model.SrvDeployment;
import com.cloud.paas.appservice.vo.srv.SrvDeploymentVO;
import com.cloud.paas.appservice.vo.srv.SrvInstDetailVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusContant;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步调用方法
 * @author yht
 * @create 2017-12-27 15:42
 **/
@Service
public class UseK8s {
    private static final Logger logger = LoggerFactory.getLogger(UseK8s.class);
    @Autowired
    SrvMngDAO srvMngDao;
    @Autowired
    private SrvImplementServiceImpl srvImplementService;
    @Autowired
    SrvOperationDAO srvOperationDAO;
    @Autowired
    SrvDeploymentDAO srvDeploymentDAO;
    @Autowired
    DependencyStorageDAO dependencyStorageDAO;

    /**
     * 调用k8s创建、启动服务
     * @param srvInstDetailVO
     * @return
     * @throws Exception
     */
    @Async
    public void creatByK8S(SrvInstDetailVO srvInstDetailVO) throws BusinessException {
        //创建服务
        logger.info("调用k8s创建服务");
        logger.info("服务启动中");
        srvImplementService.doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_INSTANCE_STARTING,"服务启动中");
        srvImplementService.serviceCreator(srvInstDetailVO);
    }
    @Async
    public void updateByK8s(SrvInstDetailVO srvInstDetailVO) throws BusinessException {
        logger.info("调用k8s更新服务");
        logger.info("服务启动中");
        srvImplementService.doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_INSTANCE_STARTING,"服务启动中");
        srvImplementService.serviceUpdator(srvInstDetailVO);
    }

    @Async
    public void deleteByK8s(SrvInstDetailVO srvInstDetailVO) throws BusinessException{
        logger.info("调用k8s删除服务");
        logger.info("服务删除中");
        srvImplementService.doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_INSTANCE_STOPING,"服务删除中");
        srvImplementService.serviceDeletor(srvInstDetailVO);
    }

    @Async
    public void updateByK8s(SrvDeployment srvDeployment, SrvInstDetailVO srvInstDetailVO) throws BusinessException {
        logger.info("调用k8s更新服务");
        logger.info("服务启动中");
        srvImplementService.doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_INSTANCE_STARTING,"服务启动中");
        srvImplementService.serviceUpdator(srvInstDetailVO);
        srvDeployment.setNewInstId(null);
        srvDeployment.setCurInstId(srvInstDetailVO.getSrvInstId());
        if (0 == srvDeploymentDAO.doUpdateByBean(srvDeployment)) {
            throw new BusinessException(CodeStatusUtil.resultByCodeEn(CodeStatusContant.DEPLOY_PUBLISH_FAILURE));
        }
    }

    @Async
    public void createVolumes(SrvDeploymentVO srvDeploymentVO, String type) throws BusinessException {
        DependencyStorage dependencyStorage;
        logger.info("创建存储");
        //1.创建pv
        Result result = srvImplementService.pvCreator(srvDeploymentVO, type);
        //2.pv状态维护
        if (result.getData() != null) {
            dependencyStorage = dependencyStorageDAO.doFindById(Integer.parseInt(result.getData().toString()));
            dependencyStorage.setPvStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.PV_CREATE_SUCCESS).getCode().toString());
            dependencyStorageDAO.doUpdateByBean(dependencyStorage);
        }
        //3.创建pvc
        srvImplementService.pvcCreator(srvDeploymentVO, type);
        //4.pvc状态维护
        srvDeploymentVO.getDependencyStorage().setPvStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.PVC_CREATE_SUCCESS).getCode().toString());
        dependencyStorageDAO.doUpdateByBean(srvDeploymentVO.getDependencyStorage());
    }

}
