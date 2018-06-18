package com.cloud.paas.appservice.util.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.appservice.dao.OperationLogDAO;
import com.cloud.paas.appservice.model.OperationLog;
import com.cloud.paas.appservice.util.yaml.common.ObjectLabel;
import com.cloud.paas.appservice.util.yaml.common.Status;
import com.cloud.paas.appservice.util.yaml.deployment.Deployment;
import com.cloud.paas.appservice.util.yaml.ingress.Ingress;
import com.cloud.paas.appservice.util.yaml.pod.Pod;
import com.cloud.paas.appservice.util.yaml.pod.PodList;
import com.cloud.paas.appservice.util.yaml.pv.PersistentVolume;
import com.cloud.paas.appservice.util.yaml.pvc.PersistentVolumeClaim;
import com.cloud.paas.appservice.util.yaml.rs.ReplicaSet;
import com.cloud.paas.appservice.util.yaml.rs.ReplicaSetList;
import com.cloud.paas.appservice.util.yaml.scale.Scale;
import com.cloud.paas.appservice.util.yaml.service.Service;
import com.cloud.paas.configuration.PropertiesConfUtil;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatus;
import com.cloud.paas.util.codestatus.CodeStatusContant;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.rest.RestClient;
import com.cloud.paas.util.rest.RestConstant;
import com.cloud.paas.util.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.*;

/**
 * @Author: wyj
 * @desc: k8s操作工具类
 * @Date: Created in 2017-12-20 9:36
 * @Modified By:
 */
public class K8sOperationUtil {

    @Autowired
    private OperationLogDAO operationLogDAO;
    private static K8sOperationUtil k8sOperationUtil = null;
    private static Integer HTTP_OK = 200;
    private static Integer HTTP_CREATE = 201;
    private static String SUCCESS = "Success";
    private static String FAILURE = "Failure";


    private K8sOperationUtil() {
    }

    public static K8sOperationUtil getInstance() {
        if (k8sOperationUtil == null) {
            synchronized (K8sOperationUtil.class) {
                if (k8sOperationUtil == null) {
                    k8sOperationUtil = new K8sOperationUtil();
                }
            }
        }
        return k8sOperationUtil;
    }

    /**
     * 调用k8s接口创建deployment
     *
     * @param deployment deployment对象
     * @return 操作结果
     */
    public Result createDeployment(Deployment deployment) throws BusinessException {
        // 1.将deployment对象转化成string
        String deployJson = JSONObject.toJSONString(deployment);
        // 2.RestTemplate调用k8s接口
        ResponseEntity<String> entity = RestClient.doPostForEntity(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_DEPLOYMENT), deployJson);
        if (entity == null) {
            insertOperationLog("调用k8s接口创建deployment失败："+deployJson);
            return codeEnToResult(CodeStatusContant.DEPLOY_BUILD_FAILURE, null);
        }
        // 3.获取
        int statusCode = entity.getStatusCodeValue();
        // deployment创建成功
        if (statusCode == HTTP_CREATE) {
            // 获取返回的结果
            String resultText = entity.getBody();
            return codeEnToResult(CodeStatusContant.DEPLOY_BUILD_SUCCESS, resultText);
        }
        insertOperationLog("调用k8s接口创建deployment失败："+deployJson);
        return codeEnToResult(CodeStatusContant.DEPLOY_BUILD_FAILURE, null);
    }

    /**
     * 调用k8s接口更新deployment
     *
     * @param deployment deployment对象
     * @return 操作结果
     */
    public Result updateDeployment(Deployment deployment) throws BusinessException {
        Result result;
        // 1.将deployment对象转化成string
        String deployJson = JSONObject.toJSONString(deployment);
        // 2.RestTemplate调用k8s接口
        Boolean isSuccess = RestClient.doPut(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_DEPLOYMENT)+deployment.getMetadata().getName(), deployJson);
        // 3.判断是否成功
        if (!isSuccess) {
            insertOperationLog("调用k8s接口创建deployment失败："+deployJson);
            result = codeEnToResult(CodeStatusContant.DEPLOY_BUILD_FAILURE, null);
            result.setSuccess(0);
        }else{
            result = codeEnToResult(CodeStatusContant.DEPLOY_BUILD_SUCCESS,null);
            result.setSuccess(1);
        }
        return result;
    }

    /**
     * 调用k8s接口创建service
     *
     * @param service service对象
     * @return 是否创建成功
     */
    public Result serviceCreator(Service service) throws BusinessException {
        // 1.将service对象转化成String
        String serviceJson = JSONObject.toJSONString(service);
        // 2.调用k8s接口创建service
        ResponseEntity<String> entity = RestClient.doPostForEntity(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_SERVICE), serviceJson);
        if (entity == null) {
            insertOperationLog("调用k8s接口创建service失败："+serviceJson);
            return codeEnToResult(CodeStatusContant.SRV_BUILD_FAILURE, null);
        }
        // 3.判断调用的状态
        int statusCode = entity.getStatusCodeValue();
        if (statusCode == HTTP_CREATE) {
            // 4.获取返回的结果
            String result = entity.getBody();
            return codeEnToResult(CodeStatusContant.SRV_BUILD_SUCCESS, result);
        }
        insertOperationLog("调用k8s接口创建service失败："+serviceJson);
        return codeEnToResult(CodeStatusContant.SRV_BUILD_FAILURE, null);
    }

    /**
     * 调用k8s接口创建ingress
     *
     * @param ingress
     * @return
     * @throws Exception
     */
    public Result ingressCreator(Ingress ingress) throws BusinessException {
        // 1.将ingress对象转化成String
        String ingressJson = JSONObject.toJSONString(ingress);
        // 2.调用k8s接口创建ingress
        ResponseEntity<String> entity = RestClient.doPostForEntity(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_INGRESS), ingressJson);
        if (entity == null) {
            insertOperationLog("调用k8s接口创建ingress失败："+ingressJson);
            return codeEnToResult(CodeStatusContant.INGRESS_BUILD_FAILURE, null);
        }
        // 3.判断调用的状态
        int statusCode = entity.getStatusCodeValue();
        if (statusCode == HTTP_CREATE) {
            // 4.获取返回的结果
            String result = entity.getBody();
            return codeEnToResult(CodeStatusContant.INGRESS_BUILD_SUCCESS, result);
        }
        insertOperationLog("调用k8s接口创建ingress失败："+ingressJson);
        return codeEnToResult(CodeStatusContant.INGRESS_BUILD_FAILURE, null);
    }

    /**
    * 调用k8s接口创建Scale
    *
    * @param scale
    * @return
    * @throws Exception
    */
    public Result scaleCreator(Scale scale) throws BusinessException{
        // 1.将scale对象转化成String
        String scaleJson = JSONObject.toJSONString(scale);
        // 2.调用k8s接口创建scale
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_SCALE);
        url = url.replace("{namespace}",scale.getMetadata().getNamespace());
        url = url.replace("{name}",scale.getMetadata().getName());
        Boolean entity = RestClient.doPut(url, scaleJson);
        if (entity == false) {
            insertOperationLog("调用k8s接口创建scale失败："+ scaleJson);
            return codeEnToResult(CodeStatusContant.SCALE_BUILD_FAILURE, null);
        }else
            return codeEnToResult(CodeStatusContant.SCALE_BUILD_SUCCESS, null);
    }

    /**
     * 根据podName,namespace删除pod
     * @param namespace namespace名字
     * @param podName pod名字
     * @return
     */
    public Result podDeletor(String podName,String namespace) throws BusinessException {
        if (null != podName && null != namespace) {
            //1.拼接url
            String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.OP_POD);
            url = url.replace("{namespace}",namespace);
            url += podName;
            //2.调用k8s删除pod接口
            String podResult = RestClient.doDeleteForEntity(url, null);
            Status pdResult = JSONObject.parseObject(podResult, Status.class);
            if (pdResult != null && pdResult.getStatus() != null) {
                return codeEnToResult(CodeStatusContant.POD_DELETE_SUCCESS, podResult);
            }else{
                insertOperationLog("调用k8s接口删除pod失败,pdResult为null或者pdResult.getStatus()不为null");
            }
        }
        else{
            insertOperationLog("调用k8s接口删除pod失败：参数为空");
        }
        return codeEnToResult(CodeStatusContant.POD_DELETE_FAILURE, null);
    }

    /**
     * 根据rsName删除rs
     * @param namespace namespace名字
     * @param rsName rs名字
     * @return
     */
    public Result rsDeletor(String rsName,String namespace) throws BusinessException {
        if (null != rsName && null != namespace) {
            //1.拼接url
            String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_RS);
            url = url.replace("{namespace}",namespace);
            url += rsName;
            //2.调用k8s删除rs接口
            String replicassetsResult = RestClient.doDeleteForEntity(url, null);
            Status rsResult = JSONObject.parseObject(replicassetsResult, Status.class);
            if (rsResult != null && rsResult.getStatus() != null) {
                return codeEnToResult(CodeStatusContant.RS_DELETE_SUCCESS, replicassetsResult);
            }else{
                insertOperationLog("调用k8s接口删除rs失败,rsResult为null或者rsResult.getStatus()不为SUCCESS");
            }
        }
        else{
            insertOperationLog("调用k8s接口删除rs失败：参数为空");
        }
        return codeEnToResult(CodeStatusContant.RS_DELETE_FAILURE, null);
    }

    /**
     * 删除deployment
     *
     * @param deploymentName deployment名字
     * @return
     */
    public Result deploymentDeletor(String deploymentName) throws BusinessException {
        if (null != deploymentName) {
            String deploymentResult = RestClient.doDeleteForEntity(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_DEPLOYMENT) + deploymentName, null);
            Status deployResult = JSONObject.parseObject(deploymentResult, Status.class);
            if (deployResult != null && deployResult.getStatus() != null) {
                return codeEnToResult(CodeStatusContant.DEPLOY_DELETE_SUCCESS, deploymentResult);
            }else{
                insertOperationLog("调用k8s接口删除deployment失败,deployResult为null或者deployResult.getStatus()不为SUCCESS");
            }
        }
        else{
            insertOperationLog("调用k8s接口删除deployment失败：deploymentName为空");
        }
        return codeEnToResult(CodeStatusContant.DEPLOY_DELETE_FAILURE, null);
    }

    /**
     * 删除service
     *
     * @param serviceName service名字
     * @return 删除结果
     */
    public Result serviceDeletor(String serviceName) throws BusinessException {
        if (null != serviceName) {
            String serviceResult = RestClient.doDeleteForEntity(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_SERVICE) + serviceName, null);
            Status serviceStatus = JSONObject.parseObject(serviceResult, Status.class);
            if (serviceStatus != null && SUCCESS.equals(serviceStatus.getStatus())) {
                return codeEnToResult(CodeStatusContant.SRV_DELETE_SUCCESS, serviceResult);
            }else{
                insertOperationLog("serviceStatus为null 或 serviceStatus.getStatus()不为SUCCESS");
            }
        }
        else{
            insertOperationLog("调用k8s接口删除service失败：serviceName为空");
        }
        return codeEnToResult(CodeStatusContant.SRV_DELETE_FAILURE, null);
    }

    /**
     * 删除ingress
     * @param ingressName ingress名称
     * @return 删除结果
     * @throws Exception
     */
    public Result ingressDeletor(String ingressName) throws BusinessException {
        if (null != ingressName){
            String ingressResult = RestClient.doDeleteForEntity(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_INGRESS) + ingressName, null);
            Status ingressStatus = JSONObject.parseObject(ingressResult, Status.class);
            if (ingressStatus != null && SUCCESS.equals(ingressStatus.getStatus())) {
                return codeEnToResult(CodeStatusContant.INGRESS_DELETE_SUCCESS, ingressResult);
            }else{
                insertOperationLog("ingressStatus为 null或 ingressStatus.getStatus()不为SUCCESS");
            }
        }else{
            insertOperationLog("调用k8s接口删除ingress失败：ingressName为空");
        }
        return codeEnToResult(CodeStatusContant.INGRESS_DELETE_FAILURE, null);
    }

    /**
     * 英文描述获取codestatus，转换成result
     *
     * @param codeEn
     * @return
     */
    public static Result codeEnToResult(String codeEn, String data) throws BusinessException {
        CodeStatus codeStatus = CodeStatusUtil.getInstance().getStatusByCodeEn(codeEn);
        if (codeStatus != null) {
            return new Result(codeStatus.getSuccess(), String.valueOf(codeStatus.getCode()), codeStatus.getMsg(), codeStatus.getLevel(), codeStatus.getType(), data);
        }
        return null;
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
     * @Author: srf
     * 根据app查询指定用户下指定应用的指定服务的所有容器名称
     */
    public List<String> getPodName(String app) {
        List<String> podName = new ArrayList<String>();
        String podResult = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_POD));
        PodList podList = JSONObject.parseObject(podResult, PodList.class);
        List<Pod> items = podList.getItems();
        for (Pod item : items) {
            ObjectLabel label = item.getMetadata().getLabels();
            if (podVerifyForDeployment(app, label.getApp())) {
                podName.add(item.getMetadata().getName());
            }
        }
        return podName;
    }

    /**
     * @Author: srf
     * 根据app查询指定用户下指定应用的指定服务的所有容器信息
     */
    public List<Map<String, String>> getPodInfo(String app) {
        List<Map<String, String>> podInfo = new ArrayList<Map<String, String>>();
        String podResult = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_POD));
        PodList podList = JSONObject.parseObject(podResult, PodList.class);
        List<Pod> items = podList.getItems();
        for (Pod item : items) {
            ObjectLabel label = item.getMetadata().getLabels();
            if (podVerifyForDeployment(app, label.getApp())) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("podName", item.getMetadata().getName());
                map.put("hostIP", item.getStatus().getHostIP());
                map.put("podIP", item.getStatus().getPodIP());
                map.put("port", item.getSpec().getContainers().get(0).getPorts().get(0).getContainerPort().toString());
                podInfo.add(map);
            }
        }
        return podInfo;
    }

    /**
     * @Author: srf
     * 验证是否为该Deployment创建的Pod
     */
    private boolean podVerifyForDeployment(String deployApp, String podApp) {
        if (deployApp.equals(podApp)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @Author: srf
     * 根据Deployment名称获取查询Deployment
     */
    public Result getDeployByName(String name) {
        Result result = CodeStatusUtil.resultByCodeEn("DEPLOY_QUERY_SUCCESS");
        String deployResult = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_DEPLOYMENT) + name);
        try {
            Deployment deployment = JSONObject.parseObject(deployResult, Deployment.class);
            result.setData(deployment);
        } catch (Exception e) {
            result = CodeStatusUtil.resultByCodeEn("DEPLOY_QUERY_FAILURE");
        }
        return result;
    }

    /**
     * @Author: srf
     * 根据Pod名称获取查询Pod
     */
    public Result getPodByName(String name) {
        Result result = CodeStatusUtil.resultByCodeEn("POD_QUERY_SUCCESS");
        String podResult = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_POD) + name);
        try {
            Pod pod = JSONObject.parseObject(podResult, Pod.class);
            if (podResult == null) {
                result = CodeStatusUtil.resultByCodeEn("POD_QUERY_FAILURE");
                return result;
            }
            result.setData(pod);
        } catch (Exception e) {
            result = CodeStatusUtil.resultByCodeEn("POD_QUERY_FAILURE");
        }
        return result;
    }

    /**
     * 通过namespace和label查询rs名字
     * @param namespace
     * @param label
     * @return
     */
    public List<String> getRsName(String namespace,String label){
        List<String> rsNameList = new ArrayList<>();
        String rsListResult = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_RS).replace("{namespace}",namespace));
        ReplicaSetList replicaSetList = JSONObject.parseObject(rsListResult, ReplicaSetList.class);
        List<ReplicaSet> items = replicaSetList.getItems();
        for (ReplicaSet item : items) {
            ObjectLabel objectLabel = item.getMetadata().getLabels();
            if (rsVerifyForLabel(label, objectLabel.getApp())) {
                rsNameList.add(item.getMetadata().getName());
            }
        }
        return rsNameList;
    }

    /**
     * 验证是否为该Deployment创建的rs
     * @param label
     * @param app
     * @return
     */
    private boolean rsVerifyForLabel(String label,String app) {
        if (label.equals(app)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据deployment获取namespace
     * @param deploymentName
     * @return
     */
    public String getDeploymentNamespace(String deploymentName){
        if(deploymentName != null) {
            String deployResult = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_DEPLOYMENT) + deploymentName);
            JSONObject deployment = JSONObject.parseObject(deployResult);
            JSONObject metadata = (JSONObject) deployment.get("metadata");
            return metadata.get("namespace").toString();
        }else{
            throw new BusinessException(CodeStatusUtil.resultByCodeEn(CodeStatusContant.DEPLOY_QUERY_FAILURE));
        }
    }

    /**
     * 调用k8s接口创建persistentVolume
     *
     * @param persistentVolume persistentVolume对象
     * @return 操作结果
     */
    public Result createPV(PersistentVolume persistentVolume) throws BusinessException {
        // 1.将persistentVolume对象转化成string
        String persistentVolumeJson = JSONObject.toJSONString(persistentVolume);
        // 2.RestTemplate调用k8s接口
        ResponseEntity<String> entity = RestClient.doPostForEntity(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_PV), persistentVolumeJson);
        if (entity == null) {
            return codeEnToResult(CodeStatusContant.PV_CREATE_FAILURE, null);
        }
        // 3.获取
        int statusCode = entity.getStatusCodeValue();
        // persistentVolume创建成功
        if (statusCode == HTTP_CREATE) {
            // 获取返回的结果
            String resultText = entity.getBody();
            return codeEnToResult(CodeStatusContant.PV_CREATE_SUCCESS, resultText);
        }
        return codeEnToResult(CodeStatusContant.PV_CREATE_FAILURE, null);
    }

    /**
     * 调用k8s接口创建persistentVolumeClaim
     *
     * @param persistentVolumeClaim persistentVolumeClaim对象
     * @return 操作结果
     */
    public Result createPVC(PersistentVolumeClaim persistentVolumeClaim) throws BusinessException {
        // 1.将persistentVolumeClaim对象转化成string
        String persistentVolumeClaimJson = JSONObject.toJSONString(persistentVolumeClaim);
        // 2.RestTemplate调用k8s接口
        ResponseEntity<String> entity = RestClient.doPostForEntity(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_PVC), persistentVolumeClaimJson);
        if (entity == null) {
            return codeEnToResult(CodeStatusContant.PVC_CREATE_FAILURE, null);
        }
        // 3.获取
        int statusCode = entity.getStatusCodeValue();
        // persistentVolume创建成功
        if (statusCode == HTTP_CREATE) {
            // 获取返回的结果
            String resultText = entity.getBody();
            return codeEnToResult(CodeStatusContant.PVC_CREATE_SUCCESS, resultText);
        }
        return codeEnToResult(CodeStatusContant.PVC_CREATE_FAILURE, null);
    }
}
