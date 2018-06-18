package com.cloud.paas.appservice.util.impl;

import com.cloud.paas.appservice.model.DependencyStorage;
import com.cloud.paas.appservice.model.DeployEnv;
import com.cloud.paas.appservice.util.Config;
import com.cloud.paas.appservice.util.yaml.deployment.Deployment;
import com.cloud.paas.appservice.util.yaml.ingress.*;
import com.cloud.paas.appservice.util.yaml.pod.PodEnv;
import com.cloud.paas.appservice.util.yaml.pv.PersistentVolume;
import com.cloud.paas.appservice.util.yaml.pvc.PersistentVolumeClaim;
import com.cloud.paas.appservice.util.yaml.service.Service;
import com.cloud.paas.appservice.vo.srv.SrvDeploymentVO;
import com.cloud.paas.appservice.vo.srv.SrvInstDetailVO;
import com.cloud.paas.configuration.PropertiesConfUtil;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.ceph.CephConstant;
import com.cloud.paas.util.ceph.CephFSUtil;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.rest.RestConstant;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wyj
 * @desc: 对象装换工具类
 * @Date: Created in 2017-12-20 9:06
 * @Modified By:
 */
public class ConvertBeanUtil {

    private static final Logger logger = LoggerFactory.getLogger(ConvertBeanUtil.class);
    private static ConvertBeanUtil convertBeanUtil = null;

    /**
     * 私有构造方法
     */
    private ConvertBeanUtil(){}

    /**
     * 单例模式
     * @return convertBeanUtil对象
     */
    public static ConvertBeanUtil getInstance() {
        if (convertBeanUtil == null) {
            synchronized (ConvertBeanUtil.class) {
                if (convertBeanUtil == null) {
                    convertBeanUtil = new ConvertBeanUtil();
                }
            }
        }
        return convertBeanUtil;
    }

    /**
     * 将srvInstDetailVO转换Deployment对象
     *
     * @param srvInstDetailVO 服务实例相关信息
     * @return
     */
    public static Deployment convertDeployment(SrvInstDetailVO srvInstDetailVO) throws BusinessException {
        // 1.将srvDetailVO中的对象映射到Deployment中
        Deployment deployment = new Deployment();
        DozerBeanMapper mapper = new DozerBeanMapper();
        List<String> mappers = new ArrayList<>();
        mappers.add("deployment.xml");
        mapper.setMappingFiles(mappers);
        // 2.转换成deployment对象
        mapper.map(srvInstDetailVO, deployment);
        // 3.设置一般属性
        logger.info("设置deployment一般属性");
        deployment.setApiVersion(PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_DEPLOY_APIVERSION));
        deployment.setKind(PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_DEPLOYMENT));
        deployment.getMetadata().setNamespace(PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_NAMESPACES));
        deployment.getSpec().setMinReadySeconds(Integer.parseInt(PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_DEPLOYMENT_MIN_READY_SECONDS)));
        // 4.转换成PodEnv对象
        List<DeployEnv> deployEnvList = srvInstDetailVO.getDeployEnvs();
        List<PodEnv> envList;
        if (deployEnvList != null) {
            envList = new ArrayList<>();
            for (DeployEnv srvEnvRel : deployEnvList) {
                PodEnv podEnv = new PodEnv();
                podEnv.setName(srvEnvRel.getDeployEnvKey());
                podEnv.setValue(srvEnvRel.getDeployEnvValue());
                envList.add(podEnv);
            }
            deployment.getSpec().getTemplate().getSpec().getContainers().get(0).setEnv(envList);
        }else{
            logger.error("服务环境变量为空");
        }
        // 5.设置默认挂载目录
//        if (null != srvDetailVO.getStorageRootPath()) {
//            PodVolumeMount podVolumeMount = new PodVolumeMount();
//            podVolumeMount.setName(srvDetailVO.getSrvNameEn());
//            podVolumeMount.setMountPath(srvDetailVO.getStorageRootPath());
//            List<PodVolumeMount> podVolumeMountList = new ArrayList<>();
//            podVolumeMountList.add(podVolumeMount);
//            deployment.getSpec().getTemplate().getSpec().getContainers().get(0).setVolumeMounts(podVolumeMountList);
//        }
//        else{
//            logger.error("服务挂载路径为空");
//            throw new BusinessException(CodeStatusUtil.resultByCodeEn("DEPLOY_BUILD_FAILURE"));
//        }
        return deployment;
    }

    /**
     * 将srvInstDetailVO转换成Service对象
     * @param srvInstDetailVO 服务实例信息对象
     * @return service对象
     * @throws Exception
     */
    public static Service convertService(SrvInstDetailVO srvInstDetailVO) throws BusinessException {
        Service service = new Service();
        DozerBeanMapper mapper = new DozerBeanMapper();
        List<String> mappers = new ArrayList<String>();
        mappers.add("service.xml");
        mapper.setMappingFiles(mappers);
        // 1.转换成service对象
        mapper.map(srvInstDetailVO, service);
        // 2.设置基础属性
        logger.info("设置service基础属性");
        service.setKind(PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_SERVICE));
        service.setApiVersion(PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_SERVICE_APIVERSION));
        service.getMetadata().setNamespace(PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_NAMESPACES));
        service.getSpec().setSessionAffinity(PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_SERVICE_SESSION_AFFINITY));
        service.getSpec().setType("NodePort");
        return service;
    }

    /**
     * 将srvInstDetailVO转换成Ingress对象
     *
     * @param srvInstDetailVO 服务实例信息对象
     * @return 转换的ingress对象
     * @throws Exception
     */
    public static Ingress convertIngress(SrvInstDetailVO srvInstDetailVO) throws BusinessException {
        // 创建ingress对象
        Ingress ingress = new Ingress();
        logger.info("设置ingress基本属性");
        // 设置kind和api
        ingress.setKind(PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_INGRESS));
        ingress.setApiVersion(PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_INGRESS_APIVERSION));
        // 设置metadata
        IngressMeta ingressMeta = new IngressMeta();
        ingressMeta.setName("ingress-rule-" + srvInstDetailVO.getSrvNameEn());
        ingressMeta.setNamespace(PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_NAMESPACES));
        ingress.setMetadata(ingressMeta);
        // 设置ingressBackend
        IngressBackend ingressBackend = new IngressBackend();
        ingressBackend.setServiceName(srvInstDetailVO.getSrvNameEn());
        ingressBackend.setServicePort(srvInstDetailVO.getSrvPort());
        // 设置ingresspaths
        IngressPaths ingressPaths = new IngressPaths();
        ingressPaths.setBackend(ingressBackend);
        ingressPaths.setPath("/");
        List<IngressPaths> ingressPathsList = new ArrayList<>();
        ingressPathsList.add(ingressPaths);
        // 设置ingressHttp
        IngressHttp ingressHttp = new IngressHttp();
        ingressHttp.setPaths(ingressPathsList);
        // 设置rurls
        IngressRules ingressRules = new IngressRules();
        ingressRules.setHost(srvInstDetailVO.getSrvNameEn()+"."+PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_HOST));
        ingressRules.setHttp(ingressHttp);
        List<IngressRules> ingressRulesList = new ArrayList<>();
        ingressRulesList.add(ingressRules);
        // 设置ingress详细描述
        IngressSpec ingressSpec = new IngressSpec();
        ingressSpec.setRules(ingressRulesList);
        ingress.setSpec(ingressSpec);
        return ingress;
    }

    /**
     * 将dependencyStorage转换PersistentVolume对象
     * @param dependencyStorage 服务实例相关信息
     * @return
     */
    public static PersistentVolume convertPV(DependencyStorage dependencyStorage) throws BusinessException {
        PersistentVolume pv = new PersistentVolume();
        //设置version和kind
        pv.setApiVersion(PropertiesConfUtil.getInstance().getProperty(RestConstant.PV_API_VERSION));
        pv.setKind(PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_PV));
        //设置pvName和容量以及目录模式
        pv.getMetadata().setName(dependencyStorage.getPvName());
        Map<String, String> map = new HashMap<>();
        map.put("storage", dependencyStorage.getStorage());
        pv.getSpec().setCapacity(map);
        pv.getSpec().setAccessModes(new String[]{Config.READ_WRITE_MANY});
        //设置cephfs相关内容
        pv.getSpec().getCephfs().setMonitors(new String[]{PropertiesConfUtil.getInstance().getProperty(CephConstant.CEPH_MON_HOST)+":6789"});
        pv.getSpec().getCephfs().setPath(dependencyStorage.getPvPath());
        pv.getSpec().getCephfs().setUser(PropertiesConfUtil.getInstance().getProperty(CephConstant.CEPH_USER));
        pv.getSpec().getCephfs().setReadOnly(false);
        pv.getSpec().getCephfs().getSecretRef().setName(PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_CEPH_SECRET_NAME));
        pv.getSpec().getCephfs().getSecretRef().setNamespace(PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_CEPH_SECRET_NAMESPACE));
        pv.getSpec().setPersistentVolumeReclaimPolicy(PersistentVolume.PERSISTENT_VOLUME_RECLAIM_POLICY);
        return pv;
    }

    /**
     * 将dependencyStorage转换PersistentVolumeClaim对象
     * @param dependencyStorage 服务实例相关信息
     * @return
     */
    public static PersistentVolumeClaim convertPVC(DependencyStorage dependencyStorage, SrvDeploymentVO srvDeploymentVO) throws BusinessException {
        PersistentVolumeClaim pvc = new PersistentVolumeClaim();
        //设置version和kind
        pvc.setApiVersion(PropertiesConfUtil.getInstance().getProperty(RestConstant.PVC_API_VERSION));
        pvc.setKind(PropertiesConfUtil.getInstance().getProperty(RestConstant.K8S_PVC));
        //设置pvcName和namespace以及目录模式
        pvc.getMetadata().setName(dependencyStorage.getPvcName());
        pvc.getMetadata().setNamespace(srvDeploymentVO.getDeploymentName());
        pvc.getSpec().setAccessModes(new String[]{Config.READ_WRITE_MANY});
        //设置关联pv名称、资源
        pvc.getSpec().setVolumeName(dependencyStorage.getPvName());
        Map<String, String> map = new HashMap<>();
        map.put("storage", dependencyStorage.getStorage());
        pvc.getSpec().getResources().setRequests(map);
        return pvc;
    }
}
