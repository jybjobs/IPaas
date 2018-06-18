package com.cloud.paas.appservice.service.impl.srv.ctn;/**
 * @author
 * @create 2017-12-15 14:00
 **/

import com.cloud.paas.appservice.dao.BaseDAO;
import com.cloud.paas.appservice.dao.CtnDetailInfoDAO;
import com.cloud.paas.appservice.model.CtnDetailInfo;
import com.cloud.paas.appservice.qo.SrvCondition;
import com.cloud.paas.appservice.service.impl.BaseServiceImpl;
import com.cloud.paas.appservice.service.srv.ctn.CtnMngService;
import com.cloud.paas.appservice.util.impl.K8sOperationUtil;
import com.cloud.paas.appservice.util.yaml.deployment.Deployment;
import com.cloud.paas.appservice.util.yaml.pod.Pod;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2017-12-15 14:00
 **/
@Service("ctnMngServiceImpl")
public class CtnMngServiceImpl extends BaseServiceImpl<CtnDetailInfo> implements CtnMngService {

    @Autowired
    private CtnDetailInfoDAO ctnDetailInfoDAO;

    public BaseDAO<CtnDetailInfo> getBaseDAO() {
        return ctnDetailInfoDAO;
    }

    /**
     * 查询所有容器信息
     *
     * @return
     */
    public Result listAllCtn() {
        Result result = CodeStatusUtil.resultByCodeEn("CONTAINER_QUERY_FAILURE");
        List<CtnDetailInfo> listCtn = ctnDetailInfoDAO.listAllCtn();
        if (listCtn != null && listCtn.size() > 0) {
            result = CodeStatusUtil.resultByCodeEn("CONTAINER_QUERY_SUCCESS");
            result.setData(listCtn);
            return result;
        }
        return result;
    }

    /**
     * 查询指定用户下所有应用所有服务下所有容器
     */
    public Result listUserGivenAppSrvDetailCnts(Integer userId) {
        Result result = CodeStatusUtil.resultByCodeEn("CONTAINER_QUERY_FAILURE");
        List<CtnDetailInfo> listCtn = ctnDetailInfoDAO.listUserGivenAppSrvDetailCnts(userId);
        if (listCtn != null && listCtn.size() > 0) {
            result = CodeStatusUtil.resultByCodeEn("CONTAINER_QUERY_SUCCESS");
            result.setData(listCtn);
            return result;
        }
        return result;
    }

    /**
     * 查询指定用户下指定应用所有服务下所有容器
     * 待实现
     */
    public Result listCtnUserGivenAppGivenSrv(Integer userId, String appname) {
        Result result = CodeStatusUtil.resultByCodeEn("CONTAINER_QUERY_FAILURE");
        SrvCondition srvCondition = new SrvCondition(userId, appname);
        List<CtnDetailInfo> listCtn = ctnDetailInfoDAO.listCtnUserGivenAppGivenSrv(srvCondition);
        if (listCtn != null && listCtn.size() > 0) {
            result = CodeStatusUtil.resultByCodeEn("CONTAINER_QUERY_SUCCESS");
            result.setData(listCtn);
            return result;
        }
        return result;
    }

    /**
     * 查询指定用户下指定应用指定服务下所有容器
     */
    public Result userGivenAppGivenSrvDetailGivenCnts(Integer userId, String appname, String srvname) {
        Result result = CodeStatusUtil.resultByCodeEn("CONTAINER_QUERY_FAILURE");
        SrvCondition srvCondition = new SrvCondition(userId, appname, srvname);
        List<CtnDetailInfo> listCtn = ctnDetailInfoDAO.userGivenAppGivenSrvDetailGivenCnts(srvCondition);
        if (listCtn != null && listCtn.size() > 0) {
            result = CodeStatusUtil.resultByCodeEn("CONTAINER_QUERY_SUCCESS");
            result.setData(listCtn);
            return result;
        }
        return result;
    }

    /**
     * @Author: srf
     * 根据app查询指定用户下指定应用的指定服务的所有容器信息
     */
    public Result listCtnByApp(String app) {
        Result result;
        List<Map<String, String>> podName = K8sOperationUtil.getInstance().getPodInfo(app);
        if (podName.size() > 0) {
            result = CodeStatusUtil.resultByCodeEn("POD_QUERY_SUCCESS");
            result.setData(podName);
        } else {
            result = CodeStatusUtil.resultByCodeEn("POD_QUERY_FAILURE");
        }
        return result;
    }

    /**
     * @Author: srf
     * 根据Deployment名称查询limits
     */
    public Result getCtnLimitsByDeployName(String name) {
        Result result = CodeStatusUtil.resultByCodeEn("LIMITS_QUERY_SUCCESS");
        Result deployResult = K8sOperationUtil.getInstance().getDeployByName(name);
        if (deployResult.getSuccess() == 1) {
            Deployment deployment = (Deployment) deployResult.getData();
            try {
                result.setData(deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getResources().getLimits());
            } catch (Exception e) {
                result = CodeStatusUtil.resultByCodeEn("LIMITS_QUERY_FAILURE");
            }
        } else {
            result = deployResult;
        }
        return result;
    }

    /**
     * @Author: srf
     * 根据容器名称查询容器
     */
    public Result getCtnByCtnName(String name) {
        return K8sOperationUtil.getInstance().getPodByName(name);
    }

    /**
     * @Author: srf
     * 根据容器名称查询limits
     */
    public Result getCtnLimitsByCtnName(String name) {
        Result result = CodeStatusUtil.resultByCodeEn("LIMITS_QUERY_SUCCESS");
        Result podResult = K8sOperationUtil.getInstance().getPodByName(name);
        if (podResult.getSuccess() == 1) {
            Pod pod = (Pod) podResult.getData();
            result.setData(pod.getSpec().getContainers().get(0).getResources().getLimits());
        } else {
            result = CodeStatusUtil.resultByCodeEn("LIMITS_QUERY_FAILURE");
        }
        return result;
    }
}
