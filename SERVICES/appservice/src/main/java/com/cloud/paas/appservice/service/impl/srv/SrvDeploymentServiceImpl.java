package com.cloud.paas.appservice.service.impl.srv;

import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.appservice.dao.*;
import com.cloud.paas.appservice.model.DeployEnv;
import com.cloud.paas.appservice.model.SrvDeployment;
import com.cloud.paas.appservice.model.SrvDetail;
import com.cloud.paas.appservice.model.SrvInstDetail;
import com.cloud.paas.appservice.qo.DeployEnvExample;
import com.cloud.paas.appservice.qo.SrvDeploymentExample;
import com.cloud.paas.appservice.qo.SrvInstDetailExample;
import com.cloud.paas.appservice.service.srv.SrvDeploymentService;
import com.cloud.paas.appservice.vo.srv.SrvDeploymentVO;
import com.cloud.paas.appservice.vo.srv.SrvInstDetailVO;
import com.cloud.paas.appservice.vo.srv.SrvVersionDetailVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cloud.paas.appservice.service.srv.SrvMngService;
import com.cloud.paas.configuration.PropertiesConfUtil;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.bean.BeanUtil;
import com.cloud.paas.util.codestatus.CodeStatusContant;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.page.PageUtil;
import com.cloud.paas.util.rest.RestClient;
import com.cloud.paas.util.rest.RestConstant;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by 17798 on 2018/5/17.
 */
@Service
public class SrvDeploymentServiceImpl implements SrvDeploymentService {

    private static final Logger logger = LoggerFactory.getLogger(SrvDeploymentServiceImpl.class);

    @Autowired
    SrvDeploymentDAO srvDeploymentDAO;

    @Autowired
    SrvInstDetailDAO srvInstDetailDAO;

    @Autowired
    DeployEnvDAO deployEnvDAO;

    @Autowired
    DependencyStorageDAO dependencyStorageDAO;

    @Autowired
    SrvVersionDetailDAO srvVersionDetailDAO;

    @Autowired
    SrvMngService srvMngService;

    @Autowired
    UseK8s useK8s;

    @Autowired
    SrvMngDAO srvMngDAO;

    /**
     * devops创建镜像版本并创建dockerfile
     * @param userid
     * @param srvId
     * @return
     */
    @Override
    public Result createImageVersionAndDockerfile(String userid, Integer srvId,String version) {
        Result result;
        //1.devops创建镜像版本并创建dockerfile
        logger.debug("1.devops创建镜像版本并创建dockerfile");
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.OP_IMAGE) + "/"+ userid +"/createImageVersionAndDockerfile";
        result = buildImageVersionAndDockerfile(url,srvId,version);
        if (result.getSuccess() == 0) {
            result.setMessage("创建镜像版本、dockerfile失败");
            throw new BusinessException(result);
        }
        return result;
    }

    /**
     * 创建服务版本
     * @param userid
     * @param srvId
     * @param version
     * @param imageVersionId
     * @return
     */
    @Override
    public Result createSrvVersion(String userid, Integer srvId, String version,Integer imageVersionId) {
        Result result;
        //1.创建服务版本
        logger.debug("1.创建服务版本");
        SrvVersionDetailVO srvVersionDetailVO = new SrvVersionDetailVO();
        srvVersionDetailVO.setSrvVersion(version);
        srvVersionDetailVO.setSrvId(srvId);
        srvVersionDetailVO.setSrvImageId(imageVersionId);
        result = srvMngService.createSrvDef(userid,srvVersionDetailVO);
        if (result.getSuccess() == 0) {
            result.setMessage("创建服务版本失败");
            throw new BusinessException(result);
        }
        return result;
    }

    /**
     * 查询部署实例信息
     * @param srvDeploymentExample
     * @return
     */
    @Override
    public Result listSrvDeploymentWithSrvInst(String userid, SrvDeploymentExample srvDeploymentExample) {
        Result result;
        logger.debug("查询部署实例信息");
        List<SrvDeploymentVO> srvDeploymentVOS = srvDeploymentDAO.listSrvDeploymentWithSrvInst(srvDeploymentExample);
        result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.DEPLOY_QUERY_SUCCESS);
        result.setData(srvDeploymentVOS);
        return result;
    }

    /**
     * 部署
     * @param userid
     * @param srvDeploymentExample
     * @return
     */
    @Override
    public Result publish(String userid, SrvDeploymentExample srvDeploymentExample) {
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE);
        logger.debug("开始部署");
        //1.构建新服务实例
        logger.debug("1.构建新服务实例");
        int srvInstId = buildSrvInst(userid, srvDeploymentExample);
        if (srvInstId == 0) {
            result.setMessage("构建新服务实例失败");
            new BusinessException(result);
        }
        //2.构建部署实例
        logger.debug("2.构建部署实例");
        result = buildSrvDeployment(userid, srvInstId, srvDeploymentExample);
        //3.获取服务实例信息
        logger.debug("3.获取服务实例信息");
        SrvInstDetailExample srvInstDetailExample = new SrvInstDetailExample();
        srvInstDetailExample.setSrvInstId(srvInstId);
        SrvInstDetailVO srvInstDetailVO = srvInstDetailDAO.listSrvInstInfoByInstId(srvInstDetailExample);
        //4.设置属性
        logger.debug("4.设置属性");
        settingDeploymentAttribute(srvDeploymentExample, srvInstDetailVO);
        //5.创建log的pv、pvc
//        logger.debug("5.创建log的pv、pvc");
//        SrvDeploymentVO srvDeploymentVO = new SrvDeploymentVO();
//        if (result.getData() != null) {
//            SrvDeployment srvDeployment = srvDeploymentDAO.doFindById(Integer.parseInt(result.getData().toString()));
//            BeanUtil.copyBean2Bean(srvDeploymentVO, srvDeployment);
//        }
//        SrvDetail srvDetail = srvMngDAO.doFindById(srvDeploymentVO.getSrvId());
//        srvDeploymentVO.setMountDir(PropertiesConfUtil.getInstance().getProperty(RestConstant.PV_MON_ROOT_DIR));
//        srvDeploymentVO.setNewDir(Config.DEPENDENCY_STORAGE_LOG + "/" + srvDeploymentVO.getTenantId() + "/" + srvDeploymentVO.getEnvId() + "/" + srvDetail.getSrvNameEn() + "/");
//        srvDeploymentVO.setStorage(PropertiesConfUtil.getInstance().getProperty(RestConstant.DEPLOY_LOG_DEFAULT_STORAGE));
//        useK8s.createVolumes(srvDeploymentVO, Config.DEPENDENCY_STORAGE_LOG);
        //6.调用k8s的api创建部署
        logger.debug("6.调用k8s的api创建部署");
        useK8s.creatByK8S(srvInstDetailVO);
        return result;
    }

    /**
     * 升级部署
     * @param userid
     * @param srvDeploymentExample
     * @return
     */
    @Override
    public Result update(String userid, SrvDeploymentExample srvDeploymentExample) {
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_MODIFY_FAILURE);
        logger.debug("升级部署");
        //1.根据部署编号获取当前部署
        SrvDeployment srvDeployment = srvDeploymentDAO.doFindById(srvDeploymentExample.getDeploymentId());
        //2.获取当前运行中的实例的信息
        SrvInstDetail srvInstDetail = srvInstDetailDAO.doFindById(srvDeployment.getCurInstId());
        if (srvDeploymentExample.getCpu() != null) {
            srvInstDetail.setCpu(srvDeploymentExample.getCpu());
        }
        if (srvDeploymentExample.getMem() != null) {
            srvInstDetail.setMem(srvDeploymentExample.getMem());
        }
        if (srvDeploymentExample.getSrvInstNum() != null) {
            srvInstDetail.setSrvInstNum(srvDeploymentExample.getSrvInstNum());
        }
        if (1 != srvInstDetailDAO.doUpdateByBean(srvInstDetail)) {
            result.setMessage("更新服务实例失败！");
            throw new BusinessException(result);
        }
        //3.更新部署资源配置
        //判断是否同步至资源配置
        if (srvDeploymentExample.getIsSync() == 1) {
            srvDeployment.setCpu(srvDeploymentExample.getCpu());
            srvDeployment.setMem(srvDeploymentExample.getMem());
            srvDeployment.setSrvInstNum(srvDeploymentExample.getSrvInstNum());
            if (1 != srvDeploymentDAO.doUpdateByBean(srvDeployment)) {
                result.setMessage("更新服务部署失败！");
                throw new BusinessException(result);
            }
        }
        //4.设置属性
        logger.debug("4.设置属性");
        SrvInstDetailVO srvInstDetailVO = srvInstDetailDAO.listSrvInstInfoByInstId(srvInstDetail);
        settingDeploymentAttribute(srvDeployment, srvInstDetailVO);
        //5.调用k8s的api创建部署
        logger.debug("5.调用k8s的api创建部署");
        useK8s.updateByK8s(srvInstDetailVO);
        result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_MODIFY_SUCCESS);
        return result;
    }

    /**
     * 部署服务实例停止
     * @param deploymentId
     * @return
     */
    @Override
    public Result deploymentStop(Integer deploymentId) {
        Result result;
        logger.info("停止服务实例");
        //1.获取部署实例
        logger.debug("1.获取部署实例");
        SrvDeployment srvDeployment = srvDeploymentDAO.doFindById(deploymentId);
        //2.构建部署实例相关的信息
        logger.debug("2.构建部署实例相关的信息");
        SrvInstDetail srvInstDetail = srvInstDetailDAO.doFindById(srvDeployment.getCurInstId());
        SrvInstDetailVO srvInstDetailVO = srvInstDetailDAO.listSrvInstInfoByInstId(srvInstDetail);
        srvInstDetailVO.setSrvNameEn(srvDeployment.getDeploymentName());
        logger.info("调用k8s停止服务");
        useK8s.deleteByK8s(srvInstDetailVO);
        //3.维护服务实例的状态信息
        logger.info("3.维护服务实例的状态信息");
        srvInstDetail.setSrvInstStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_STOPING).getCode());
        srvInstDetailDAO.doUpdateByBean(srvInstDetail);
        result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_STOPING);
        result.setSuccess(1);
        return result;
    }

    /**
     * 部署服务实例启动
     * @param deploymentId
     * @return
     */
    @Override
    public Result deploymentStart(Integer deploymentId) {
        logger.info("启动服务实例");
        //1.获取部署实例
        logger.debug("1.获取部署实例");
        SrvDeployment srvDeployment = srvDeploymentDAO.doFindById(deploymentId);
        //2.构建部署实例相关的信息
        logger.debug("2.构建部署实例相关的信息");
        SrvInstDetail srvInstDetail = srvInstDetailDAO.doFindById(srvDeployment.getCurInstId());
        SrvInstDetailVO srvInstDetailVO = srvInstDetailDAO.listSrvInstInfoByInstId(srvInstDetail);
        settingDeploymentAttribute(srvDeployment, srvInstDetailVO);
        //判断是否已存在
        String result = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_DEPLOYMENT) + srvDeployment.getDeploymentName() + "/status");
        if(result != null){
            logger.info("调用k8s更新服务");
            useK8s.updateByK8s(srvInstDetailVO);
        } else{
            logger.info("调用k8s创建服务");
            useK8s.creatByK8S(srvInstDetailVO);
        }
        //3.维护服务实例的状态信息
        logger.info("3.维护服务实例的状态信息");
        srvInstDetail.setSrvInstStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_STARTING).getCode());
        srvInstDetailDAO.doUpdateByBean(srvInstDetail);
        return CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_RUNNING);
    }

    /**
     * 更新服务部署
     * @param srvDeploymentExample
     * @return
     */
    @Override
    public Result update(SrvDeploymentExample srvDeploymentExample) {
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.DEPLOY_BUILD_FAILURE);
        logger.debug("升级部署");
        if (1 != srvDeploymentDAO.doUpdateByBean(srvDeploymentExample)) {
            result.setMessage("更新部署失败！");
            throw new BusinessException(result);
        }
        result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.DEPLOY_BUILD_SUCCESS);
        return result;
    }

    /**
     * 切换版本
     * @param userid
     * @param srvDeploymentExample
     * @return
     */
    @Override
    public Result exchangeVersion(String userid, SrvDeploymentExample srvDeploymentExample) {
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SRV_DEPLOY_VERSION_EXCHANGE_FAILURE);
        logger.debug("版本切换");
        //1.获取部署模板信息
        logger.debug("1.获取部署模板信息");
        SrvDeployment srvDeployment = srvDeploymentDAO.doFindById(srvDeploymentExample.getDeploymentId());
        BeanUtil.copyBean2Bean(srvDeploymentExample,srvDeployment);
        //2.构建新的服务实例
        logger.debug("2.构建新的服务实例");
        SrvInstDetail srvInstDetail = buildNewSrvInst(userid, srvDeploymentExample);
        //3.更新部署信息
        logger.debug("3.更新部署信息");
        srvDeployment.setNewInstId(srvInstDetail.getSrvInstId());
        if (1 != srvDeploymentDAO.doUpdateByBean(srvDeployment))
            throw new BusinessException(result);
        //4.设置属性
        logger.debug("4.设置属性");
        SrvInstDetailVO srvInstDetailVO = srvInstDetailDAO.listSrvInstInfoByInstId(srvInstDetail);
        settingDeploymentAttribute(srvDeployment, srvInstDetailVO);
        //5.调用k8s的api创建部署
        logger.debug("5.调用k8s的api创建部署");
        useK8s.updateByK8s(srvDeployment, srvInstDetailVO);
        result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_MODIFY_SUCCESS);
        return result;
    }

    /**
     * 自动部署接口
     * @param userid
     * @param srvDeploymentExample
     * @return
     */
    @Override
    public void autoPublish(String userid, SrvDeploymentExample srvDeploymentExample) {
        logger.debug("自动部署接口");
        //1.通过服务编号获取所有的部署
        List<SrvDeployment> srvDeploymentList = srvDeploymentDAO.doFindBySrvId(srvDeploymentExample.getSrvId());
        for (SrvDeployment srvDeployment : srvDeploymentList) {
            if (srvDeployment.getAutoPublish() == 1) {
                srvDeploymentExample.setDeploymentId(srvDeployment.getDeploymentId());
                exchangeVersion(userid, srvDeploymentExample);
            }
        }
    }

    /**
     * 查询环境变量列表
     * @param userid
     * @param deployEnvExample
     */
    @Override
    public Result doFindDeployEnvList(String userid, DeployEnvExample deployEnvExample) {
        Result result;
        int pageNum = deployEnvExample.getPageNum();
        int pageSize = deployEnvExample.getPageSize();
        logger.debug("pageNum" + pageNum + " pageSize:" + pageSize);
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<DeployEnv> deployEnvList = deployEnvDAO.doFindDeployEnvList(deployEnvExample);
        PageInfo pageInfo = PageUtil.getPageInfo(page, deployEnvList);
        result = CodeStatusUtil.resultByCodeEn (CodeStatusContant.SRV_DEPLOY_ENV_QUERY_SUCCESS);
        pageInfo.setList(deployEnvList);
        result.setData(pageInfo);
        return result;
    }

    /**
     * 保存部署环境变量
     * @param userid
     * @param deployEnv
     * @return
     */
    @Override
    public Result saveDeployEnv(String userid, DeployEnv deployEnv) {
        Result result;
        logger.debug("保存环境变量");
        //1.判断环境变量的编号是否存在
        //存在编号，即进行修改操作
        if (deployEnv.getDeployEnvId() != null && !"".equals(deployEnv.getDeployEnvId())) {
            result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SRV_DEPLOY_ENV_MODIFY_FAILURE);
            if (1 != deployEnvDAO.doUpdateByBean(deployEnv)) {
                result.setMessage("修改环境变量失败");
                return result;
            }
            result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SRV_DEPLOY_ENV_MODIFY_SUCCESS);
        } else {//编号不存在，进行保存操作
            result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SRV_DEPLOY_ENV_ADD_FAILURE);
            //2.key查重
            List<DeployEnv> checkList = deployEnvDAO.doFindByDeployEnvKey(deployEnv.getDeployEnvKey());
            if (checkList != null && checkList.size() != 0) {
                result.setMessage("key已经存在");
                return result;
            } else if (1 != deployEnvDAO.doInsertByBean(deployEnv)) {
                result.setMessage("保存环境变量失败");
                return result;
            }
            result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SRV_DEPLOY_ENV_ADD_SUCCESS);
        }
        return result;
    }

    /**
     * 删除部署环境变量
     * @param userid
     * @param deployEnvId
     * @return
     */
    @Override
    public Result deleteDeployEnv(String userid, Integer deployEnvId) {
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SRV_DEPLOY_ENV_DELETE_FAILURE);
        if (1 != deployEnvDAO.doDeleteById(deployEnvId)){
            result.setMessage("删除环境变量失败！");
            return result;
        }
        result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SRV_DEPLOY_ENV_DELETE_SUCCESS);
        return result;
    }

    /**
     * 查询容器日志
     * @param podName
     * @param namespace
     * @return
     */
    @Override
    public Result queryPodLog(String podName, String namespace) {
        logger.debug("查询容器日志");
        Result result;
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_K8S_DASHBOARD_POD_LOG);
        url = url.replace("{namespace}",namespace);
        url = url.replace("{podName}",podName);
        String jsonResult = RestClient.doGet(url);
        result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.CONTAINER_LOG_QUERY_SUCCESS);
        result.setData(jsonResult);
        return result;
    }

    /**
     * 构建新服务实例
     * @return 服务实例编号
     */
    private int buildSrvInst (String userid, SrvDeploymentExample srvDeploymentExample) {
        logger.debug("开始构建服务实例");
        //1.同步资源信息、镜像信息
        logger.debug("1.同步资源信息、镜像信息");
        SrvInstDetailVO srvInstDetailVO = new SrvInstDetailVO();
        srvInstDetailVO.setSrvVersionId(srvDeploymentExample.getSrvVersionId());
        srvInstDetailVO.setSrvInstNum(srvDeploymentExample.getSrvInstNum());
        srvInstDetailVO.setCpu(srvDeploymentExample.getCpu());
        srvInstDetailVO.setMem(srvDeploymentExample.getMem());
        //2.插入服务实例信息
        logger.debug("2.插入服务实例信息");
        int srvInstId = srvMngService.doInsertSrvInst(srvInstDetailVO);
        if (0 == srvInstId){
            throw new BusinessException(CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE));
        }
        return srvInstId;
    }

    /**
     * 构建新版本服务实例
     * @return 服务实例编号
     */
    private SrvInstDetail buildNewSrvInst (String userid, SrvDeploymentExample srvDeploymentExample) {
        logger.debug("开始构建新版本服务实例");
        SrvInstDetail srvInstDetail = srvInstDetailDAO.doFindById(srvDeploymentExample.getCurInstId());
        //1.同步资源信息、镜像信息
        logger.debug("1.同步资源信息、镜像信息");
        srvInstDetail.setSrvVersionId(srvDeploymentExample.getSrvVersionId());
        srvInstDetail.setSrvInstNum(srvDeploymentExample.getSrvInstNum());
        srvInstDetail.setCpu(srvDeploymentExample.getCpu());
        srvInstDetail.setMem(srvDeploymentExample.getMem());
        srvInstDetail.setSrvInstStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATING).getCode());
        srvInstDetail.setSrvInstId(null);
        //2.插入服务实例信息
        logger.debug("2.插入服务实例信息");
        if (0 == srvInstDetailDAO.doInsertByBean(srvInstDetail)){
            throw new BusinessException(CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE));
        }
        return srvInstDetail;
    }

    /**
     * 构建部署实例
     * @param userid
     * @param srvDeploymentExample
     */
    private Result buildSrvDeployment (String userid, int srvInstId, SrvDeploymentExample srvDeploymentExample) {
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE);
        logger.debug("开始构建部署实例");
        //1.获取部署实例信息
        logger.debug("1.获取部署实例信息");
        SrvDeployment srvDeployment = new SrvDeployment();
        BeanUtil.copyBean2Bean(srvDeployment, srvDeploymentExample);
        //2.设置部分参数
        logger.debug("2.设置部分参数");
        srvDeployment.setCreator("ghy");
        srvDeployment.setCurInstId(srvInstId);
        srvDeployment.setAutoPublish(0);
        //3.写入部署实例信息
        logger.debug("3.写入部署实例信息");
        if (1 != srvDeploymentDAO.doInsertByBean(srvDeployment)){
            result.setMessage("写入部署实例信息失败");
            throw new BusinessException(result);
        }
        result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATED);
        result.setSuccess(1);
        result.setData(srvDeployment.getDeploymentId());
        return result;
    }

    /**
     * 创建服务镜像、dockerfile
     * @param url
     * @param srvId
     * @return
     */
    private Result buildImageVersionAndDockerfile(String url,Integer srvId,String version){
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_IMAGE_BUILD_FAILURE);
        //1.构建srvImageVO信息
        logger.debug("构建srvImageVO信息");
        SrvDetail srvDetail = srvMngService.doFindById(srvId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("srvNameEn",srvDetail.getSrvNameEn());
        jsonObject.put("srvNameZh",srvDetail.getSrvNameCh());
        jsonObject.put("srvImageId",srvDetail.getSrvImageId());
        jsonObject.put("srvImageVersionId",srvDetail.getSrvImageVersionId());
        jsonObject.put("srvVersion",version);
        String body = jsonObject.toJSONString();
        logger.debug("srvImageVO信息:{}",body);
        //2.调用rest接口
        logger.debug("调用rest接口");
        ResponseEntity<String> responseEntity = RestClient.doPostForEntity(url,body);
        logger.debug("调用服务镜像、dockerfile创建接口返回:{}",responseEntity);
        if(responseEntity == null || responseEntity.getBody() == null) {
            logger.debug("服务镜像、dockerfile创建失败");
            result.setMessage("服务镜像、dockerfile创建失败");
            throw new BusinessException(result);
        }
        logger.debug("服务镜像、dockerfile创建成功");
        result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_IMAGE_BUILD_SUCCESS);
        result.setSuccess(1);
        result.setData(((Map)JSONObject.parse(responseEntity.getBody())).get("data"));
        return result;
    }

    /**
     * 获取基础镜像使用规则
     * @param imageVersionId
     * @return
     */
    private String getExposePort(Integer imageVersionId) {
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.OP_IMAGE) + "/getImageVersionRule/" + imageVersionId;
        String response = RestClient.doGet(url);
        if (response != null) {
            Result result = JSONObject.parseObject(response, Result.class);
            if (result.getData() != null && ((Map)result.getData()).get("exposePort") != null)
                return ((Map)result.getData()).get("exposePort").toString();
            else
                return null;
        }
        return null;
    }

    /**
     * 设置属性
     * @param srvDeployment
     * @param srvInstDetailVO
     */
    private void settingDeploymentAttribute (SrvDeployment srvDeployment, SrvInstDetailVO srvInstDetailVO) {
        srvInstDetailVO.setSrvNameEn(srvDeployment.getDeploymentName());
        //TODO 进入租户查询镜像仓库ip、端口号
        srvInstDetailVO.setSrvImage("10.1.162.171:5000" + "/" + srvDeployment.getImageUrl() + ":" + srvInstDetailVO.getSrvVersionDetail().getSrvVersion());
        String exposePort = getExposePort(srvInstDetailVO.getSrvDetail().getSrvImageVersionId());
        logger.debug("暴露端口号为：" + exposePort);
        if (exposePort != null)
            srvInstDetailVO.setSrvPort(Integer.parseInt(exposePort));
        List<DeployEnv> deployEnvs = deployEnvDAO.doFindByDeploymentId(srvDeployment.getDeploymentId());
        srvInstDetailVO.setDeployEnvs(deployEnvs);
    }
}
