package com.cloud.paas.appservice.service.impl.srv;

import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.appservice.dao.*;
import com.cloud.paas.appservice.model.DependencyStorage;
import com.cloud.paas.appservice.model.OperationLog;
import com.cloud.paas.appservice.model.SrvInstDetail;
import com.cloud.paas.appservice.model.SrvOperation;
import com.cloud.paas.appservice.service.srv.SrvImplementService;
import com.cloud.paas.appservice.util.impl.ConvertBeanUtil;
import com.cloud.paas.appservice.util.impl.K8sOperationUtil;
import com.cloud.paas.appservice.util.yaml.deployment.Deployment;
import com.cloud.paas.appservice.util.yaml.ingress.Ingress;
import com.cloud.paas.appservice.util.yaml.pv.PersistentVolume;
import com.cloud.paas.appservice.util.yaml.pvc.PersistentVolumeClaim;
import com.cloud.paas.appservice.vo.srv.SrvDeploymentVO;
import com.cloud.paas.appservice.vo.srv.SrvInstDetailVO;
import com.cloud.paas.configuration.PropertiesConfUtil;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.bean.BeanUtil;
import com.cloud.paas.util.ceph.CephFSUtil;
import com.cloud.paas.util.codestatus.CodeStatus;
import com.cloud.paas.util.codestatus.CodeStatusContant;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.rest.RestClient;
import com.cloud.paas.util.rest.RestConstant;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wyj
 * @desc:
 * @Date: Created in 2017-12-20 9:04
 * @Modified By:
 */
@Service
public class SrvImplementServiceImpl implements SrvImplementService {

    private static final Logger logger = LoggerFactory.getLogger(SrvImplementServiceImpl.class);
    @Autowired
    private SrvOperationDAO srvOperationDAO;
    @Autowired
    private OperationLogDAO operationLogDAO;
    @Autowired
    private SrvMngDAO srvMngDAO;
    @Autowired
    SrvInstDetailDAO srvInstDetailDAO;
    @Autowired
    DependencyStorageDAO dependencyStorageDAO;
    /**
     * 服务状态队列
     */
    public static List<Integer> srvIdList = new ArrayList<>();
    /**
     * 默认的cpu core值
     */
    private int cpuCore = 2;
    /**
     * 状态的返回值
     */
    private String statusValue = "True";
    /**
     * 状态是否可用
     */
    private String statusAvailable = "Available";
    /**
     * 删除flag
     */
    private int deleteFlag = 2;
    /**
     * 创建flag
     */
    private int createFlag = 1;
    /**
     * 环境变量key命名规则
     */
    private String regEx = "[A-Za-z_][A-Za-z0-9_]*";


    /**
     * 创建服务和启动
     *
     * @param srvInstDetailVO 服务实例详情
     * @return 操作结果
     */
    @Override
    public void serviceCreator(SrvInstDetailVO srvInstDetailVO) throws BusinessException {
        SrvInstDetail srvInstDetail = bean2Bean(srvInstDetailVO);
        try{
            // 1.基本信息构建
            serviceBuild(srvInstDetailVO,0);
            // 2.将srvDetailVO转换成deployment
            Deployment deployment = ConvertBeanUtil.convertDeployment(srvInstDetailVO);
            // 3.将srvDetailVO转换成service
            com.cloud.paas.appservice.util.yaml.service.Service service = ConvertBeanUtil.convertService(srvInstDetailVO);
            // 4.将srvDetailVO转换成Ingress
            Ingress ingress = ConvertBeanUtil.convertIngress(srvInstDetailVO);
            // 5.k8s创建deploy和service
            createService(srvInstDetailVO, deployment, service,ingress);
            //服务实例运行中
            srvInstDetail.setSrvInstStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_RUNNING).getCode());
            srvInstDetailDAO.doUpdateByBean(srvInstDetail);
        } catch (Exception e) {
            e.printStackTrace();
            //启动失败
            srvInstDetail.setSrvInstStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE).getCode());
            srvInstDetailDAO.doUpdateByBean(srvInstDetail);
            // 回滚操作
            rollingBackOperation(srvInstDetailVO);
            // 操作数据库
            doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE, null);
        }
    }

    @Override
    public void serviceUpdator(SrvInstDetailVO srvInstDetailVO) throws BusinessException {
        SrvInstDetail srvInstDetail = bean2Bean(srvInstDetailVO);
        try{
            // 1.基本信息构建
            serviceBuild(srvInstDetailVO,1);
            // 2.将srvDetailVO转换成deployment
            Deployment deployment = ConvertBeanUtil.convertDeployment(srvInstDetailVO);
            // 3.k8s更新deploy和service
            updateService(srvInstDetailVO, deployment);
            //服务实例启动成功
            srvInstDetail.setSrvInstStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_RUNNING).getCode());
            srvInstDetailDAO.doUpdateByBean(srvInstDetail);
            doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_INSTANCE_RUNNING, null);
        } catch (Exception e) {
            e.printStackTrace();
            //启动失败
//            srvInstDetail.setHistory(1);
//            srvInstDetail.setSrvInstStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE).getCode());
//            srvInstDetailDAO.doUpdateByBean(srvInstDetail);
//            //还原被切换的服务实例
//            SrvInstDetail originSrvInstDetail = srvInstDetailDAO.doFindById(srvInstDetailVO.getCurrentSrvInstId());
//            originSrvInstDetail.setHistory(0);
//            srvInstDetailDAO.doUpdateByBean(originSrvInstDetail);
            // 回滚操作
            rollingBackOperation(srvInstDetailVO);
            // 操作数据库
            doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE, "回滚");
        }
    }

    /**
     * 基本信息构建
     * @param srvInstDetailVO
     * @param flag 0为创建 1为更新
     * @return
     */
    private SrvInstDetailVO serviceBuild(SrvInstDetailVO srvInstDetailVO,int flag){
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE);
        // 1.检查是否含有必要的参数
        logger.info("参数审查");
        if (!checkParam(srvInstDetailVO)) {
            doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_BUILD_FAILURE_PARAM, "服务更新参数不完整或参数不符合规则");
            doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE, null);
            result.setMessage("参数审查失败");
            throw new BusinessException(result);
        }
        // 2.通过imageVersionId获取镜像(当未传入服务镜像时才需调用接口获取)
        if (srvInstDetailVO.getSrvImage() == null || "".equals(srvInstDetailVO.getSrvImage())) {
            logger.info("获取远程镜像");
            String image = getRemoteImageInfo(srvInstDetailVO.getSrvVersionDetail().getSrvImageId());
            logger.debug("服务创建image信息:" + image);
            if (image == null) {
                // 获取镜像失败,更新数据库
                logger.error("获取远程镜像失败");
                doOperationDB(srvInstDetailVO, CodeStatusContant.IMAGE_QUERY_FAILURE, "获取镜像失败，image为空");
                doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_BUILD_FAILURE, null);
                result.setMessage("获取远程镜像失败");
                throw new BusinessException(result);
            }
            srvInstDetailVO.setSrvImage(image);
        }
        // 3.检测服务是否已经存在,服务名为appName+serviceName
        logger.info("服务存在校验");
        if (flag == 0 && containService(srvInstDetailVO.getSrvNameEn(), createFlag)) {
            doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_BUILD_FAILURE_CANTAIN, "服务已经存在");
            doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE, null);
            result.setMessage("服务已经存在");
            throw new BusinessException(result);
        }
        //4.获取基础镜像使用规则--暴露端口
        if (srvInstDetailVO.getSrvPort() == 0) {
            String exposeport = getImageVersionRulePort(srvInstDetailVO.getSrvDetail().getSrvImageVersionId());
            //默认先取第一个（目前只支持单个暴露的端口）
            if (exposeport == null) {
                // 获取暴露端口失败,更新数据库
                logger.error("获取暴露端口失败");
                doOperationDB(srvInstDetailVO, CodeStatusContant.IMAGE_QUERY_FAILURE, "获取镜像暴露端口失败，exposeport为空");
                doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE, null);
                result.setMessage("获取暴露端口失败");
                throw new BusinessException(result);
            } else {
                exposeport = exposeport.split(",")[0];
                if (exposeport != null && exposeport != "") {
                    srvInstDetailVO.setSrvPort(Integer.parseInt(exposeport));
                }
            }
        }
        return srvInstDetailVO;
    }

    /**
     * 回滚操作
     *
     * @param srvInstDetailVO
     */
    private void rollingBackOperation(SrvInstDetailVO srvInstDetailVO) throws BusinessException {

        String ingressResult = queryIngressStatus("ingress-rule-"+srvInstDetailVO.getSrvNameEn());
        logger.debug("ingress查询信息:"+ingressResult);
        if (ingressResult != null) {
            Result result = K8sOperationUtil.getInstance().ingressDeletor("ingress-rule-"+srvInstDetailVO.getSrvNameEn());
            logger.debug("ingress删除信息:"+result.getSuccess());
            if (result.getSuccess() == 1) {
                // 操作数据库
                doOperationDB(srvInstDetailVO, CodeStatusContant.INGRESS_DELETE_SUCCESS, null);
            }
        }
        com.cloud.paas.appservice.util.yaml.service.Service service = queryServiceStatus(srvInstDetailVO.getSrvNameEn());
        if (service != null) {
            Result result = K8sOperationUtil.getInstance().serviceDeletor(srvInstDetailVO.getSrvNameEn());
            if (result.getSuccess() == 1) {
                // 操作数据库
                doOperationDB(srvInstDetailVO, CodeStatusContant.SRV_DELETE_SUCCESS, null);
            }
        }
        String deploy = queryDeploymentStatus(srvInstDetailVO.getSrvNameEn());
        logger.debug("deploy查询信息:"+deploy);
        if (deploy != null) {
            Result result = K8sOperationUtil.getInstance().deploymentDeletor(srvInstDetailVO.getSrvNameEn());
            if (result.getSuccess() == 1) {
                // 操作数据库
                doOperationDB(srvInstDetailVO, CodeStatusContant.DEPLOY_DELETE_SUCCESS, null);
            }
        }

    }

    /**
     * deployment和service实际创建
     *
     * @param deployment deployment对象
     * @param service    service对象
     * @return
     */
    private void createService(SrvInstDetailVO srvInstDetailVO, Deployment deployment, com.cloud.paas.appservice.util.yaml.service.Service service, Ingress ingress) throws BusinessException {
        // 1.创建deployment
        logger.info("调用k8s接口创建deployment");
        Result deployResult = K8sOperationUtil.getInstance().createDeployment(deployment);
        logger.debug("deployment创建信息:"+deployResult.getSuccess()+"---------"+deployResult.getData());
        // 2.deployment成功，创建service
        if (deployResult.getSuccess() == 1) {
            // 操作数据库
            doOperationDB(srvInstDetailVO, CodeStatusContant.DEPLOY_BUILD_SUCCESS, deployResult.getData().toString());
            Result serviceResult = K8sOperationUtil.getInstance().serviceCreator(service);
            logger.debug("service创建信息:"+serviceResult.getSuccess()+"---------"+serviceResult.getData());
            // 7.service成功，返回
            if (serviceResult.getSuccess() == 1) {
                doOperationDB(srvInstDetailVO, CodeStatusContant.SRV_BUILD_SUCCESS, serviceResult.getData().toString());
                // 创建Ingress
                Result ingressResult = K8sOperationUtil.getInstance().ingressCreator(ingress);
                logger.debug("ingress创建信息:"+ingressResult.getSuccess()+"---------"+ingressResult.getData());
                if (ingressResult.getSuccess() == 1){
                    doOperationDB(srvInstDetailVO, CodeStatusContant.INGRESS_BUILD_SUCCESS, ingressResult.getData().toString());
                    // 添加到list集合中
                    srvIdList.add(srvInstDetailVO.getSrvInstId());
                    doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_INSTANCE_RUNNING, "启动成功");
                } else {
                    doOperationDB(srvInstDetailVO, CodeStatusContant.INGRESS_BUILD_FAILURE, null);
                    // 回滚操作
                    rollingBackOperation(srvInstDetailVO);
                    doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE, null);
                }
            } else {
                doOperationDB(srvInstDetailVO, CodeStatusContant.SRV_BUILD_FAILURE, null);
                // 回滚操作
                rollingBackOperation(srvInstDetailVO);
                doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE, null);
            }
        } else {
            // deployment失败，操作数据库
            doOperationDB(srvInstDetailVO, CodeStatusContant.DEPLOY_BUILD_FAILURE, "调用k8s接口创建deployment失败");
            doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE, null);
        }
    }

    /**
     * deployment和service实际更新
     *
     * @param deployment deployment对象
     * @return
     */
    private void updateService(SrvInstDetailVO srvInstDetailVO, Deployment deployment) throws BusinessException {
        // 1.创建deployment
        logger.info("调用k8s接口创建deployment");
        Result deployResult = K8sOperationUtil.getInstance().updateDeployment(deployment);
        logger.debug("deployment创建成功");
        // 2.deployment成功，创建service
        if (deployResult.getSuccess() == 1) {
            // 操作数据库
            doOperationDB(srvInstDetailVO, CodeStatusContant.DEPLOY_BUILD_SUCCESS, null);
            doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_INSTANCE_RUNNING, "启动成功");
        } else {
            // deployment失败，操作数据库
            doOperationDB(srvInstDetailVO, CodeStatusContant.DEPLOY_BUILD_FAILURE, "调用k8s接口创建deployment失败");
            doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE, null);
        }
    }

    /**
     * 检测参数是否符合创建的条件
     *
     * @param srvInstDetailVO
     * @return
     */
    private boolean checkParam(SrvInstDetailVO srvInstDetailVO) {
        SrvInstDetail srvInstDetail = bean2Bean(srvInstDetailVO);
        if (srvInstDetailVO == null) {
            doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_BUILD_FAILURE_PARAM,"服务srvDetailVO为空");
            return false;
        }
        // 判断srvNameEn是否为空
        logger.info("判断srvNameEn是否为空");
        if (srvInstDetailVO.getSrvDetail().getSrvNameEn() == null || "".equals(srvInstDetailVO.getSrvDetail().getSrvNameEn())) {
            doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_BUILD_FAILURE_PARAM,"srvNameEn为空");
            return false;
        }
        // 判断environment的key是否为空
//        logger.info("判断environment的key是否为空");
//        if (srvDetailVO.getSrvEnvRels() != null) {
//            List<SrvEnvRel> srvEnvRels = srvDetailVO.getSrvEnvRels();
//            for (SrvEnvRel srvEnvRel : srvEnvRels) {
//                if (srvEnvRel.getEnvKey() != null && !"".equals(srvEnvRel.getEnvKey())) {
//                    Pattern pattern = Pattern.compile(regEx);
//                    Matcher matcher = pattern.matcher(srvEnvRel.getEnvKey());
//                    if (!matcher.find()) {
//                        doOperationDB(srvDetail, CodeStatusContant.SERVICE_BUILD_FAILURE_PARAM,"environment的key是否为空");
//                        return false;
//                    }
//                } else {
//                    srvEnvRel.setEnvKey(null);
//                }
//            }
//        }
        // 判断镜像版本是否为空
        logger.info("判断镜像版本是否为空");
        if (srvInstDetailVO.getSrvVersionDetail().getSrvImageId() == null || "".equals(srvInstDetailVO.getSrvVersionDetail().getSrvImageId())) {
            doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_BUILD_FAILURE_PARAM,"镜像版本为空");
            return false;
        }
//        // 判断镜像端口是否为空
//        if (srvDetailVO.getSrvPort() == null || "".equals(srvDetailVO.getSrvPort())) {
//            doOperationDB(srvDetail, CodeStatusContant.SERVICE_BUILD_FAILURE_PARAM,"服务访问端口为空");
//            return false;
//        }
//        // 判断cpu是否满足条件
//        if (srvDetailVO.getCpu() == null || "".equals(srvDetailVO.getCpu())) {
//            doOperationDB(srvDetail, CodeStatusContant.SERVICE_BUILD_FAILURE_PARAM,"cpu设置为空");
//            return false;
//        }
//        else{
//            if (srvDetailVO.getCpu() >= cpuCore) {
//                doOperationDB(srvDetail, CodeStatusContant.SERVICE_BUILD_FAILURE_PARAM,"cpu设置有误，cpuCore最多为2");
//                return false;
//            }
//        }
        return true;
    }
    /**
     * 检测服务是否已经存在
     *
     * @param serviceName
     * @return
     */
    private boolean containService(String serviceName, int flag) {
        String deployResult = null;
        com.cloud.paas.appservice.util.yaml.service.Service service = null;
        String ingressResult = null;
        try {
            ingressResult = queryIngressStatus("ingress-rule-"+serviceName);
            deployResult = queryDeploymentStatus(serviceName);
            service = queryServiceStatus(serviceName);
            switch (flag) {
                case 1:
                    if (deployResult != null || service != null || ingressResult != null) {
                        return true;
                    }

                    break;
                case 2:
                    if (deployResult != null && service != null && ingressResult != null) {
                        return true;
                    }

                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("containService:"+e.getMessage());
            insertOperationLog("检测服务"+serviceName+",containService:"+e.getMessage());
            return true;
        }
        return false;
    }

    /**
     * 停止服务
     *
     * @param srvInstDetailVO 服务实例信息
     * @return 操作结果
     */
    @Override
    public void serviceDeletor(SrvInstDetailVO srvInstDetailVO) throws BusinessException {
        if (srvInstDetailVO != null) {
            SrvInstDetail srvInstDetail = bean2Bean(srvInstDetailVO);
            // 检测服务是否存在,存在再删除
            if (containService(srvInstDetailVO.getSrvNameEn(), deleteFlag)) {
                // 删除的结果
                Result ingressResult = K8sOperationUtil.getInstance().ingressDeletor("ingress-rule-"+srvInstDetailVO.getSrvNameEn());
                if (ingressResult != null && ingressResult.getSuccess() == 1) {
                    // 操作数据库
                    doOperationDB(srvInstDetailVO, CodeStatusContant.INGRESS_DELETE_SUCCESS, ingressResult.getData().toString());
                    Result result = K8sOperationUtil.getInstance().serviceDeletor(srvInstDetailVO.getSrvNameEn());
                    if (result != null && result.getSuccess() == 1) {
                        // 操作数据库
                        doOperationDB(srvInstDetailVO, CodeStatusContant.SRV_DELETE_SUCCESS, result.getData().toString());
                        //获取该deployment产生的rs名称集合
                        String namespace = K8sOperationUtil.getInstance().getDeploymentNamespace(srvInstDetailVO.getSrvNameEn());
                        List<String> rsNameList = K8sOperationUtil.getInstance().getRsName(namespace,srvInstDetailVO.getSrvNameEn());
                        //获取该deployment产生的pod的名称集合
                        List<String> podNameList = K8sOperationUtil.getInstance().getPodName(srvInstDetailVO.getSrvNameEn());
                        // 删除deploy
                        Result deployResult = K8sOperationUtil.getInstance().deploymentDeletor(srvInstDetailVO.getSrvNameEn());
                        if (deployResult != null && deployResult.getSuccess() == 1) {
                            //3.遍历rs名称集合
                            for(String rsName : rsNameList){
                                //删除rs
                                Result rsResult = K8sOperationUtil.getInstance().rsDeletor(rsName,namespace);
                                if(rsResult != null && rsResult.getSuccess() == 1) {
                                    doOperationDB(srvInstDetailVO, CodeStatusContant.RS_DELETE_SUCCESS, rsResult.getData().toString());
                                }else{
                                    doOperationDB(srvInstDetailVO, CodeStatusContant.RS_DELETE_FAILURE, rsResult.getData().toString());
                                }
                            }
                            for(String podName : podNameList){
                                //删除pod
                                Result podResult = K8sOperationUtil.getInstance().podDeletor(podName,namespace);
                                if(podResult != null && podResult.getSuccess() == 1) {
                                    doOperationDB(srvInstDetailVO, CodeStatusContant.POD_DELETE_SUCCESS, podResult.getData().toString());
                                }else{
                                    doOperationDB(srvInstDetailVO, CodeStatusContant.POD_DELETE_FAILURE, podResult.getData().toString());
                                }
                            }
                            doOperationDB(srvInstDetailVO, CodeStatusContant.DEPLOY_DELETE_SUCCESS, deployResult.getData().toString());
                            doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_DELETE_SUCCESS, result.getData().toString() + deployResult.getData().toString());
                            return;
                        } else {
                            doOperationDB(srvInstDetailVO, CodeStatusContant.DEPLOY_DELETE_FAILURE, "deploy删除失败");
                            doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_DELETE_FAILURE, "deploy删除失败");
                            return;
                        }
                    } else {
                        doOperationDB(srvInstDetailVO, CodeStatusContant.SRV_DELETE_FAILURE, "service删除失败");
                        doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_DELETE_FAILURE, null);
                        return;
                    }
                } else {
                    doOperationDB(srvInstDetailVO, CodeStatusContant.INGRESS_DELETE_FAILURE, "ingress删除失败");
                    doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_DELETE_FAILURE, null);
                    return;
                }
            } else {
                doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_BUILD_FAILURE_NOT_CANTAIN, "服务不存在！");
                doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_DELETE_FAILURE, null);
                return;
            }
        } else {
            doOperationDB(srvInstDetailVO, CodeStatusContant.SERVICE_DELETE_FAILURE, "srvDetail为空");
        }
    }

    /**
     * 查询服务的详细信息
     *
     * @param serviceName 服务名称
     * @return 查询结果
     */
    @Override
    public Result serviceQuery(String serviceName) {
        return null;
    }

    /**
     * 服务状态更新操作
     */
//    @Override
//    public boolean serviceUpdator(int srvId) throws BusinessException {
//        logger.debug ("通过服务id查询服务详情-------:"+srvId);
//        // 1.获取服务名称
//        SrvDetail srvDetail = srvMngDAO.doFindById(srvId);
//        logger.debug ("查询出来的服务详情-------:"+JSONObject.toJSONString (srvDetail));
//        String serviceName = srvDetail.getSrvNameEn();
//        logger.debug ("查询出来的服务名称-------:"+serviceName);
//        // 2.查询deployment的状态
//        String deployResult = queryDeploymentStatus(serviceName);
//        Deployment deployment = JSONObject.parseObject(deployResult, Deployment.class);
//        // 3.判断状态
//        if (deployment != null) {
//            DeploymentStatus status = deployment.getStatus();
//            if (statusValue.equals(status.getConditions().get(0).getStatus())
//                    && statusAvailable.equals(status.getConditions().get(0).getType())) {
//                if (status.getReplicas().equals(status.getAvailableReplicas())) {
//                    // 服务创建成功
//                    logger.info("服务创建成功");
//                    doOperationDB(srvDetail, CodeStatusContant.SERVICE_BUILD_SUCCESS, deployResult);
//                    return true;
//                } else {
//                    // TODO
//                    return false;
//                }
//            } else {
//                // 服务创建失败
//                logger.info("服务创建失败");
//                doOperationDB(srvDetail, CodeStatusContant.SERVICE_BUILD_FAILURE, deployResult);
//                return true;
//            }
//        }
//        // 服务创建失败
//        doOperationDB(srvDetail, CodeStatusContant.SERVICE_BUILD_FAILURE, null);
//        return true;
//    }

    /**
     * 根据某个deployment名称查询deployment状态
     *
     * @param deploymentName deployment名称
     * @return deployment对象
     */
    private static String queryDeploymentStatus(String deploymentName) throws BusinessException {
        // 1.查询deployment状态
        String result = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_DEPLOYMENT) + deploymentName + "/status");
        if (result != null) {
            return result;
        }
        return null;
    }

    /**
     * 根据某个ingress名称查询ingress状态
     *
     * @param ingressName deployment名称
     * @return ingress对象
     */
    private static String queryIngressStatus(String ingressName) throws BusinessException {
        // 1.查询deployment状态
        String result = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_INGRESS) + ingressName + "/status");
        if (result != null) {
            return result;
        }
        return null;
    }

    /**
     * 查询service状态
     *
     * @param serviceName
     * @return
     * @throws Exception
     */
    private static com.cloud.paas.appservice.util.yaml.service.Service queryServiceStatus(String serviceName) throws BusinessException {
        // 1.查询service状态
        String result = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_SERVICE) + serviceName + "/status");
        if (result != null) {
            com.cloud.paas.appservice.util.yaml.service.Service service = JSONObject.parseObject(result, com.cloud.paas.appservice.util.yaml.service.Service.class);
            return service;
        }
        return null;
    }

    /**
     * 服务操作和日志表操作
     *
     * @param srvInstDetailVO  服务定义详细信息
     * @param codeEn     状态码英文
     * @param logContent 日志内容
     * @return
     */
    @Transactional
    void doOperationDB(SrvInstDetailVO srvInstDetailVO, String codeEn, String logContent) throws BusinessException {
        // 服务操作表
        SrvOperation srvOperation = new SrvOperation();
        // 获取状态码
        CodeStatus codeStatus = CodeStatusUtil.getInstance().getStatusByCodeEn(codeEn);
        srvOperation.setSrvId(srvInstDetailVO.getSrvInstId());
        srvOperation.setState(Long.decode(String.valueOf(codeStatus.getCode())));
        srvOperation.setOperationDesc(codeStatus.getMsg());
        srvOperation.setCreator(srvInstDetailVO.getCreator());
        // 插入操作表数据库
        int result = srvOperationDAO.doInsertByBean(srvOperation);
        if (result != 0) {
            if (logContent != null) {
                // 服务日志表
                OperationLog operationLog = new OperationLog();
                operationLog.setSrvOperationId(srvOperation.getSrvOperationId());
                operationLog.setCreator(srvInstDetailVO.getCreator());
                operationLog.setLogContent(logContent);
                operationLogDAO.doInsertByBean(operationLog);
            }
        }
    }
    /**
     * 当不涉及到srv_operation表时插入operation_log表
     */
    private int insertOperationLog(String logContent){
        OperationLog operationLog = new OperationLog();
        operationLog.setCreator("admin");
        operationLog.setLogContent(logContent);
        return operationLogDAO.doInsertByBean(operationLog);
    }
    /**
     * 通过镜像版本id获取镜像
     *
     * @param imageVersionId 镜像版本
     * @return 镜像
     */
    private String getRemoteImageInfo(int imageVersionId) throws BusinessException {
        String content = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_REMOTE_IMAGE) + imageVersionId);
        if (content != null) {
            Result result = JSONObject.parseObject(content, Result.class);
            if (result.getSuccess() == 1) {
                return result.getData().toString();
            }
        }
        return null;
    }

    /**
     * 通过镜像版本id获取镜像使用规则端口
     *
     * @param imageVersionId 镜像版本
     * @return 镜像
     */
    private String getImageVersionRulePort(int imageVersionId) throws BusinessException {
        String content = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_RULE_IMAGE) + imageVersionId);
        if (content != null) {
            Result result = JSONObject.parseObject(content, Result.class);
            if (result.getSuccess() == 1) {
                return ((JSONObject)result.getData()).get("exposePort").toString();
            }
        }
        return null;
    }

    /**
     * srvInstDetailVO转srvInstDetail
     *
     * @return
     */
    private SrvInstDetail bean2Bean(SrvInstDetailVO srvInstDetailVO) throws BusinessException {
        SrvInstDetail srvInstDetail = new SrvInstDetail();
        BeanUtil.copyBean2Bean(srvInstDetail, srvInstDetailVO);
        return srvInstDetail;
    }

    /**
     * 创建PV
     * @param srvDeploymentVO
     * @return
     * @throws BusinessException
     */
    @Override
    public Result pvCreator(SrvDeploymentVO srvDeploymentVO, String type) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.PV_CREATE_FAILURE);
        String pvPath;
        String pvName;
        //1.调用Ceph接口创建文件夹
        logger.debug("1.调用Ceph接口创建文件夹");
        try {
            pvPath = CephFSUtil.getInstance().mkdir(srvDeploymentVO.getMountDir(), srvDeploymentVO.getNewDir(), CephFSUtil.READ_AND_WRITE);
        } catch (IOException e) {
            logger.debug("CephFS创建文件夹失败");
            result.setMessage("CephFS创建文件夹失败");
            throw new BusinessException(result);
        }
        //2.根据部署参数，生成dependencyStorage,并入库
        logger.debug("2.根据部署参数，生成dependencyStorage,并入库");
        if (type != null) {
            pvName = srvDeploymentVO.getDeploymentName() + "-" + type + "-pv";
        } else {
            result.setMessage("pv类型为空");
            throw new BusinessException(result);
        }
        DependencyStorage dependencyStorage = buildDependencyStoragePV(pvName, pvPath, srvDeploymentVO.getDeploymentId(), srvDeploymentVO.getStorage());
        srvDeploymentVO.setDependencyStorage(dependencyStorage);
        //3.将dependencyStorage对象转换成PersistentVolume对象
        PersistentVolume pv = ConvertBeanUtil.convertPV(dependencyStorage);
        //4.创建pv
        result = K8sOperationUtil.getInstance().createPV(pv);
        if (result.getSuccess() != 1) {
            result.setMessage("创建PV失败");
            dependencyStorage.setPvStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.PV_CREATE_FAILURE).getCode().toString());
            dependencyStorageDAO.doUpdateByBean(dependencyStorage);
            return result;
        }
        result.setData(dependencyStorage.getAcpDependencyStorageId());
        return result;
    }

    /**
     * 创建PVC
     * @param srvDeploymentVO
     * @return
     * @throws BusinessException
     */
    @Override
    public Result pvcCreator(SrvDeploymentVO srvDeploymentVO, String type) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.PVC_CREATE_FAILURE);
        String pvcName;
        //1.根据部署参数，更新dependencyStorage
        logger.debug("1.根据部署参数，更新dependencyStorage");
        if (type != null) {
            pvcName = srvDeploymentVO.getDeploymentName() + "-" + type + "pvc";
        } else {
            result.setMessage("pvc类型为空");
            throw new BusinessException(result);
        }
        DependencyStorage dependencyStorage = srvDeploymentVO.getDependencyStorage();
        dependencyStorage.setPvcName(pvcName);
        dependencyStorage.setPvStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.PVC_CREATING).getCode().toString());
        if (1 != dependencyStorageDAO.doUpdateByBean(dependencyStorage)) {
            result.setMessage("依赖资源存储更新失败");
            throw new BusinessException(result);
        }
        //2.将dependencyStorage对象转换成PersistentVolume对象
        PersistentVolumeClaim pvc = ConvertBeanUtil.convertPVC(dependencyStorage, srvDeploymentVO);
        //3.创建pvc
        result = K8sOperationUtil.getInstance().createPVC(pvc);
        if (result.getSuccess() != 1) {
            result.setMessage("创建PVC失败");
            dependencyStorage.setPvStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.PVC_CREATE_FAILURE).getCode().toString());
            dependencyStorageDAO.doUpdateByBean(dependencyStorage);
            throw new BusinessException(result);
        }
        result.setData(pvc);
        return result;
    }

    /**
     * 创建依赖资源，填充PV内容
     * @param pvName PV名称
     * @param pvPath PV的存储路径
     * @param deploymentId 部署编号
     * @return dependencyStorage
     */
    private DependencyStorage buildDependencyStoragePV (String pvName, String pvPath, Integer deploymentId, String storage) {
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.PV_CREATE_FAILURE);
        DependencyStorage dependencyStorage = new DependencyStorage();
        dependencyStorage.setDeploymentId(deploymentId);
        dependencyStorage.setCreator("ghy");
        dependencyStorage.setPvName(pvName);
        dependencyStorage.setPvPath(pvPath);
        dependencyStorage.setStorage(storage);
        dependencyStorage.setPvStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.PV_CREATING).getCode().toString());
        if (1 != dependencyStorageDAO.doInsertByBean(dependencyStorage)) {
            result.setMessage("依赖资源存储保存失败");
            throw new BusinessException(result);
        }
        return dependencyStorage;
    }
}
