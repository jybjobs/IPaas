package com.cloud.paas.appservice.service.impl.srv;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.appservice.dao.*;
import com.cloud.paas.appservice.model.*;
import com.cloud.paas.appservice.qo.*;
import com.cloud.paas.appservice.service.impl.BaseServiceImpl;
import com.cloud.paas.appservice.util.Config;
import com.cloud.paas.appservice.vo.app.AppDetailVO;
import com.cloud.paas.appservice.vo.srv.SrvDetailVO;
import com.cloud.paas.appservice.vo.srv.SrvInstDetailVO;
import com.cloud.paas.appservice.vo.srv.SrvVersionDetailVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cloud.paas.appservice.service.srv.SrvMngService;
import com.cloud.paas.appservice.util.impl.K8sOperationUtil;
import com.cloud.paas.configuration.PropertiesConfUtil;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.bean.BeanUtil;
import com.cloud.paas.util.codestatus.CodeStatusContant;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.date.DateUtil;
import com.cloud.paas.util.fileupload.UploadFile;
import com.cloud.paas.util.page.PageUtil;
import com.cloud.paas.util.rest.RestClient;
import com.cloud.paas.util.rest.RestConstant;
import com.cloud.paas.util.result.Result;
import com.cloud.paas.util.zip.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Transactional
@Service
public class SrvMngServiceImpl extends BaseServiceImpl<SrvDetail> implements SrvMngService {
    private static final Logger logger = LoggerFactory.getLogger(SrvMngServiceImpl.class);
    private static final String SERVICE_BUILDING_DESC = "服务创建中";
    SrvDetail srvDetail = new SrvDetail();
    SrvVersionDetail srvVersionDetail = new SrvVersionDetail();
    SrvInstDetail srvInstDetail = new SrvInstDetail();
    List<SrvEnvRel> srvEnvRels;
    SrvScaleRel srvScaleRel;
    @Autowired
    SrvMngDAO srvMngDao;
    @Autowired
    SrvOperationDAO srvOperationDAO;
    @Autowired
    CtnDetailInfoDAO ctnDetailInfoDAO;
    @Autowired
    AppSrvRelDAO appSrvRelDAO;
    @Autowired
    SrvScaleRuleDAO srvScaleRuleDAO;
    @Autowired
    SrvScaleRelDAO srvScaleRelDAO;
    @Autowired
    SrvStorageRelDAO srvStorageRelDAO;
    @Autowired
    OperationLogDAO operationLogDAO;
    @Autowired
    AppMngDAO appMngDAO;
    @Autowired
    SrvEnvRelDAO srvEnvRelDAO;
    @Autowired
    private SrvImplementServiceImpl srvImplementService;
    @Autowired
    SrvPersistentRelDAO srvPersistentRelDAO;
    @Autowired
    SrvRealtimeStateDAO srvRealtimeStateDAO;
    @Autowired
    SrvVersionDetailDAO srvVersionDetailDAO;
    @Autowired
    SrvInstDetailDAO srvInstDetailDAO;
    @Autowired
    UseK8s useK8s;

    @Autowired
    VerifySrv verifySrv;
    DateUtil dateUtil = new DateUtil();

    @Override
    public BaseDAO<SrvDetail> getBaseDAO() {
        return srvMngDao;
    }

    /**
     * 创建服务
     *
     * @param srvDetailVO
     * @return result
     * @throws BusinessException
     */
    public Result doInsertBySrvDetail(SrvDetailVO srvDetailVO) throws BusinessException {
        Result result;
        logger.info("服务创建");
        //1.服务创建信息校验
        logger.info("服务创建信息校验");
        verifySrv.createSrvVerify(srvDetailVO);
        //2.校验成功
        logger.info("服务创建信息校验成功");
        srvDetailVO.setCreator("yh");
        //3.创建服务操作信息
        logger.info("创建服务操作信息");
        SrvOperation srvOperation = new SrvOperation();
        //4.开始创建服务
        int srvId = insertBySrvDetail(srvDetailVO);
        result = verifySrv.verifySrvId(srvId);
        logger.debug("服务id------------------"+ srvId);
        srvDetailVO.setSrvId(srvId);
        srvOperation.setSrvId(srvId);
        srvOperation.setCreator("yh");
        logger.info(SERVICE_BUILDING_DESC);
        srvOperation.setOperationDesc(SERVICE_BUILDING_DESC);
        srvOperation.setState(CodeStatusUtil.getInstance().getStatusByCodeEn(CodeStatusContant.SERVICE_BUILDING).getCode().longValue());
        logger.info("插入服务操作信息");
        srvOperationDAO.doInsertByBean(srvOperation);
        return result;
    }
    /**
     * @param srvDetailVO
     * @Brief:创建服务
     * @return：Result
     */
    public int insertBySrvDetail(SrvDetailVO srvDetailVO) throws BusinessException {
        //1.服务详情数据提取
        logger.info("服务详情数据提取");
        BeanUtil.copyBean2Bean(srvDetail, srvDetailVO);
        //2.插入服务详情数据
        logger.info("插入服务详情数据");
        srvDetail.setCreateTime(dateUtil.getCurrentTime());
        srvMngDao.doInsertByBean(srvDetail);
        //3.获取服务id
        logger.info("获取服务id");
        int srvId = srvDetail.getSrvId();
        logger.debug("srvId" + srvDetail.getSrvId());
        //4.创建持久化目录服务关系
        srvDetailVO.setSrvId(srvDetail.getSrvId());
        if(srvDetailVO.getSrvPersistentRelJson() != null) {
            createPersistentRel(srvDetailVO);
        }
        if (srvId != 0) {
            return srvId;
        }
        return 0;
    }

    /**
     * @param
     * @Brief:创建持久化目录服务关系表
     * @return：int
     */
    private int createPersistentRel(SrvDetailVO srvDetailVO) {
        int persistentRel = 0;
        if (srvDetailVO.getSrvPersistentRelJson() != null) {
            JSONObject jsonObject = srvDetailVO.getSrvPersistentRelJson();
            try {
                JSONArray path = jsonObject.getJSONArray("persistent_path");
                JSONArray descr = jsonObject.getJSONArray("descr");
                if (path.size() != descr.size()) {
                    logger.error("插入持久化目录失败");
                    throw new BusinessException(CodeStatusUtil.resultByCodeEn("SERVICE_BUILD_FAILURE"));
                }
                for (int i = 0; i < path.size(); i++) {
                    SrvPersistentRel srvPersistentRel = new SrvPersistentRel();
                    srvPersistentRel.setPersistentPath(path.getString(i));
                    srvPersistentRel.setDescr(descr.getString(i));
                    srvPersistentRel.setCreator(srvDetailVO.getCreator());
                    srvPersistentRel.setSrvId(srvDetailVO.getSrvId());
                    srvPersistentRel.setCreateTime(dateUtil.getCurrentTime());
                    srvPersistentRel.setUpdateTime(dateUtil.getCurrentTime());
                    persistentRel = srvPersistentRelDAO.doInsertByBean(srvPersistentRel);
                }
            } catch (JSONException e) {
                String path = jsonObject.getString("persistent_path");
                String descr = jsonObject.getString("descr");
                SrvPersistentRel srvPersistentRel = new SrvPersistentRel();
                srvPersistentRel.setPersistentPath(path);
                srvPersistentRel.setDescr(descr);
                srvPersistentRel.setCreator(srvDetailVO.getCreator());
                srvPersistentRel.setSrvId(srvDetailVO.getSrvId());
                srvPersistentRel.setCreateTime(dateUtil.getCurrentTime());
                srvPersistentRel.setUpdateTime(dateUtil.getCurrentTime());
                logger.error("单条持久化存储目录");
                persistentRel = srvPersistentRelDAO.doInsertByBean(srvPersistentRel);
            }
            if (persistentRel == 0) {
                logger.error("插入持久化目录关系失败");
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("SERVICE_BUILD_FAILURE"));
            }
        }
        return persistentRel;
    }



    /**
     * @param
     * @Brief:创建应用服务关系表
     * @return：int
     */
    public int createAppSrvRel(AppDetail appDetail) {
        AppSrvRel appSrvRel = new AppSrvRel();
        appSrvRel.setAppId(appDetail.getAppId());
        appSrvRel.setSrvId(srvDetail.getSrvId());
        appSrvRel.setCreateTime(dateUtil.getCurrentTime());
        appSrvRel.setUpdateTime(dateUtil.getCurrentTime());
        int appSrv = appSrvRelDAO.doInsertByBean(appSrvRel);
        if (appSrv == 0) {
            logger.error("插入应用关系失败");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("SERVICE_BUILD_FAILURE"));
        }
        return 1;
    }

    /**
     * @param srvEnvRels
     * @Brief:创建环境变量
     * @return：Result
     */
    public int createEnv(List<SrvEnvRel> srvEnvRels) {
        int result = 0;
        for (SrvEnvRel srvEnvRel : srvEnvRels) {
            srvEnvRel.setSrvId(srvInstDetail.getSrvInstId());
            logger.info("插入环境变量");
            result = srvEnvRelDAO.doInsertByBean(srvEnvRel);
            if (result == 0) {
                logger.error("插入环境变量失败");
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("SERVICE_BUILD_FAILURE"));
            }
        }
        return 1;
    }

    /**
     * @param srvStorageRels
     * @Brief:服务存储创建
     * @return：int
     */
    public int createStorage(List<SrvStorageRel> srvStorageRels) {
        int result = 0;
        if(null == srvStorageRels) {
            srvStorageRels = new ArrayList<SrvStorageRel>();
            SrvStorageRel srvStorageRel = new SrvStorageRel();
            srvStorageRels.add(srvStorageRel);
        }
        for(SrvStorageRel srvStorageRel : srvStorageRels) {
//            srvStorageRel.setCreateTime(dateUtil.getCurrentTime());
//            srvStorageRel.setUpdateTime(dateUtil.getCurrentTime());
//            srvStorageRel.setSrvId(srvDetail.getSrvId());
//            srvStorageRel.setCatalog(srvDetail.getStorageRootPath());
//            result = srvStorageRelDAO.doInsertByBean(srvStorageRel);
//            if(result == 0) {
//                logger.error("插入服务存储失败");
//                throw new BusinessException(CodeStatusUtil.resultByCodeEn("SERVICE_BUILD_FAILURE"));
//            }
        }
        return 1;
    }

    /**
     * 更新服务
     *
     * @param srvDetailVO
     * @return：Result
     */
    public Result doUpdateBySrvDetail(SrvDetailVO srvDetailVO) throws  BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_MODIFY_SUCCESS);
        BeanUtil.copyBean2Bean(srvDetail,srvDetailVO);
        //1.服务验证
        logger.info("验证服务");
        verifySrv.updateSrvVerify(srvDetail);
        //2.更新服务持久化目录,先删后新增
        if(srvDetailVO.getSrvPersistentRelJson() != null) {
            srvPersistentRelDAO.doDeleteById(srvDetail.getSrvId());
            createPersistentRel(srvDetailVO);
        }
        //3.更新服务
        logger.info("更新服务");
        if (srvMngDao.doUpdateByBean(srvDetail) == 0) {
            throw new BusinessException(CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_MODIFY_FAILURE));
        }
        return result;
    }

    /**
     * 更新服务 devops
     *
     * @param srvDetailVO
     * @return
     */
    public Result doUpdateProject(SrvDetailVO srvDetailVO) throws  BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_MODIFY_SUCCESS);
        BeanUtil.copyBean2Bean(srvDetail,srvDetailVO);
        //1.更新服务
        logger.info("更新服务");
        if (srvMngDao.doUpdateByBean(srvDetail) == 0) {
            throw new BusinessException(CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_MODIFY_FAILURE));
        }
        return result;
    }

    /**
     * 更新服务启动顺序
     *
     * @param appSrvRels
     * @return：Result
     */
    public Result doUpdateByAppSrvRel(List<AppSrvRel> appSrvRels) throws  BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn ("SRV_START_ORDER_FAIL");
        for (AppSrvRel appSrvRel : appSrvRels) {
            int a = appSrvRelDAO.doUpdateByBean(appSrvRel);
            logger.debug("--------------------------------------------" + a);
            if (a == 0) {
                return result;
            }
        }
        result = CodeStatusUtil.resultByCodeEn ("SRV_START_ORDER_SUCCESS");
        return result;
    }

    /**
     * 通过ID删除服务
     *
     * @param srvId
     * @return：Result
     */
    public Result doDeleteSrvDetailById(Integer srvId) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("SERVICE_DELETE_FAILURE");
        //1.获取当前服务下服务定义
        logger.info("1.获取当前服务下服务定义");
        SrvVersionDetailExample srvVersionDetailExample = new SrvVersionDetailExample();
        srvVersionDetailExample.setSrvId(srvId);
        List<SrvVersionDetailVO> list = srvVersionDetailDAO.listSrvVersionDetail(srvVersionDetailExample);
        //2.校验并删除服务
        if (list != null && list.size() > 0) {
            result.setMessage ("当前服务存在服务定义！不能删除");
        }else if (deleteSrvDetailById(srvId)!=0){
            result = CodeStatusUtil.resultByCodeEn ("SERVICE_DELETE_SUCCESS");
        }
        return result;
    }

    /**
     * 通过ID删除服务
     *
     * @param srvId
     * @return：Result
     */
    public int deleteSrvDetailById(Integer srvId) {
        //1.删除持久化目录
        logger.debug("1.删除持久化目录");
        srvPersistentRelDAO.doDeleteById(srvId);
        //2.删除服务
        logger.debug("2.删除服务");
        return srvMngDao.doDeleteById(srvId);
    }
    /**
     * 通过服务ID查询
     *
     * @param srvId
     * @return ：Result
     */
    public Result doFindSrvDetailById(Integer srvId) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn (CodeStatusContant.SERVICE_QUERY_FAILURE);
        //传给界面的VO
        SrvDetailVO srvDetailVO = new SrvDetailVO();
        //获取服务的详情
        SrvDetail srvDetail = srvMngDao.doFindById(srvId);
        if(srvDetail == null){
            result.setMessage("服务详情获取失败");
            throw new BusinessException(result);
        }
        List<SrvPersistentRel> srvPersistentRels = srvPersistentRelDAO.doFindBySrvId(srvDetail.getSrvId());
        BeanUtil.copyBean2Bean(srvDetailVO, srvDetail);
        srvDetailVO.setSrvPersistentRels(srvPersistentRels);
        //获取服务图片
        srvDetailVO.setBase64Img(srvDetail.getSrvDescImgPath());
        result = CodeStatusUtil.resultByCodeEn (CodeStatusContant.SERVICE_QUERY_SUCCESS);
        result.setData(srvDetailVO);
        return result;
    }

    /**
     * @Brief: 获取所有用户下所有应用的所有服务列表
     * @Param:
     * @Return: Result
     */
    public Result listUserAppSrvDetail() throws  BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn (CodeStatusContant.SERVICE_QUERY_FAILURE);
        List<AppDetailVO> list = srvMngDao.listUserAppSrvDetail();
        if (list != null && list.size() > 0) {
            result = CodeStatusUtil.resultByCodeEn (CodeStatusContant.SERVICE_QUERY_SUCCESS);
            result.setData(list);
            return result;
        }
        return result;
    }
    /**
     * 获取指定用户下指定应用的所有服务列表
     *
     * @param : userId, appname
     * @return
     */
    public Result listUserGivenAppGivenSrvDetail(Integer userId, String appname) throws BusinessException {
        Result result = null;
        String resultMsg = "";
        /* 判断appname 是否合法 */
        logger.info("判断appname是否合法");
        if (appname.equals("")) {
            resultMsg = "启动用户的userid: 下的应用名不能为空";
            result = CodeStatusUtil.resultByCodeEn("SERVICE_QUERY_FAILURE");
            result.setMessage(resultMsg);
            return result;
        }
        SrvCondition srvCondition = new SrvCondition(userId, appname);
        AppDetailVO listSrvDetail = srvMngDao.listUserGivenAppGivenSrvDetail(srvCondition);
        if (listSrvDetail != null) {
            result = CodeStatusUtil.resultByCodeEn("SERVICE_QUERY_SUCCESS");
            result.setData(listSrvDetail);
            return result;
        }
        result = CodeStatusUtil.resultByCodeEn("SERVICE_QUERY_FAILURE");
        return result;
    }

    /**
     * 指定用户的指定应用下的指定服务列表
     *
     * @param :srvCondition
     * @return:Result
     */
    public Result userGivenAppGivenSrvDetailGiven(Integer userId, String appname, String srvname) throws BusinessException {
        Result result = null;
        /*判断appname,srvname的输入是否合法*/
        logger.info("判断appname,srvname的输入是否合法");
        typeOfJudgment(appname, srvname);
        SrvCondition srvCondition = new SrvCondition(userId, appname, srvname);
        AppDetailVO srvDetail = srvMngDao.userGivenAppGivenSrvDetailGiven(srvCondition);
        if (srvDetail != null) {
            result = CodeStatusUtil.resultByCodeEn("SERVICE_QUERY_SUCCESS");
            result.setData(srvDetail);
            return result;
        }
        result = CodeStatusUtil.resultByCodeEn("SERVICE_QUERY_FAILURE");
        return result;
    }

    /**
     * 判断输入的appName, srvName是否合法
     *
     * @param appName
     * @param srvName
     * @return
     */
    public void typeOfJudgment(String appName, String srvName)throws BusinessException {
        Result result = null;
        String resultMsg = "";
        /* 判断appname 是否合法 */
        logger.info("判断appname 是否合法");
        if (appName.equals("")) {
            resultMsg = "启动用户的userid: 下的应用名不能为空";
            logger.error(resultMsg);
            result = CodeStatusUtil.resultByCodeEn("SERVICE_QUERY_FAILURE");
            result.setMessage(resultMsg);
            throw new BusinessException(result);
        }
        /* 判断srvname 是否合法 */
        logger.info("判断srvname 是否合法");
        if (srvName.equals("")) {
            resultMsg = "启动用户的userid:应用：" + appName + "，下的服务名不能为空";
            logger.error(resultMsg);
            result = CodeStatusUtil.resultByCodeEn("SERVICE_QUERY_FAILURE");
            result.setMessage(resultMsg);
            throw new BusinessException(result);
        }
    }

    /**
     * 改变服务的状态：1：启动，2：停止，3：扩容，4：缩容
     *
     * @param userId
     * @param appName
     * @param srvName
     * @param flag
     * @return
     */
    public Result SrvStatusChanged(Integer userId, String appName, String srvName, Long flag) throws BusinessException {
        Result result = null;
//        long state;
//        /* 判断appname ,srvName是否合法 */
//        typeOfJudgment(appName, srvName);
//        /* 获取服务相关信息 */
//        SrvCondition srvCondition = new SrvCondition(userId, appName, srvName);
//        AppDetailVO appDetailVO = srvMngDao.userGivenAppGivenSrvDetailGiven(srvCondition);
//        if (null == appDetailVO) {
//            logger.error("根据userId: {}, 应用名称： {}, 服务名称: {} 不能找到appDetailVO，服务的状态无法改变", userId, appName, srvName);
//            result = CodeStatusUtil.resultByCodeEn("SERVICE_QUERY_FAILURE");
//            result.setMessage("不能找到appDetailVO，服务的状态无法改变");
//            insertOperationLog(result.getMessage());
//            return result;
//        }
//        /* TODO:判断用户是否有相应的操作权限 */
//        //flag：操作动作标记（1：启动，2：停止）
//        SrvDetailVO srvDetailVO = appDetailVO.getSrvDetailVO();
//        SrvOperation srvOperation = new SrvOperation();
//        switch (flag.intValue()) {
//            case 1:
//                //TODO:判断服务是否满足启动条件
//                // 1. 处于停止状态的服务可以启动
//                // 2. 处于启动失败状态的服务可以启动
//                logger.info("判断服务是否满足启动条件");
//                state = srvOperationDAO.getSrvOperationState(srvDetailVO.getSrvId());
//                //useK8s.creatByK8S(srvDetailVO);
//                if (state == CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_START_FAILURE").getCode().longValue()//启动失败
//                        || state == CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_STOP_SUCCESS").getCode().longValue()//服务停止成功
//                        || state == CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_BUILD_SUCCESS").getCode().longValue()//服务构建成功
//                        || state == CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_BUILD_FAILURE").getCode().longValue()//服务构建失败
//                        || state == CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_DELETE_SUCCESS").getCode().longValue()//服务删除失败
//                        || state == CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_DELETE_FAILURE").getCode().longValue()//服务删除成功
//                        ) {
//                    srvOperation.setSrvId(srvDetailVO.getSrvId());
//                    srvOperation.setState(CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_STARTING").getCode().longValue());
//                    srvOperation.setOperationDesc("服务启动中");
//                    //向服务操作表中插入一条记录，显示服务启动中
//                    logger.info("向服务操作表中插入一条记录");
//                    srvOperationDAO.doInsertByBean(srvOperation);
//                    result = CodeStatusUtil.resultByCodeEn("SERVICE_BUILDING");
//                    result.setData("启动用户的userid: " + userId + ",应用：" + appName + "，下的服务：" + srvName + "处于启动中");
//                    //flag==1表示启动
//                    List<SrvEnvRel>  srvEnvRels2 = srvEnvRelDAO.doFindBySrvId(srvDetailVO.getSrvId());
//                    srvDetailVO.setSrvEnvRels(srvEnvRels2);
//                    useK8s.creatByK8S(srvDetailVO); //重新起一个线程调用K8S开启服务
//                } else {
//                    result = CodeStatusUtil.resultByCodeEn("SERVICE_START_FAILURE");
//                    result.setMessage("查询不到服务");
//                    result.setData("启动用户的userid: " + userId + ",应用：" + appName + "，下的服务：" + srvName + "启动失败,因为该服务现在处于启动状态");
//                    insertOperationLog(JSONObject.toJSONString(result));
//                }
//                break;
//            case 2:
//                SrvDetail srvDetail = new SrvDetail();
//                BeanUtil.copyBean2Bean(srvDetail, srvDetailVO);
//                //TODO:判断服务是否满足停止条件
//                // 1. 处于启动状态的服务可以停止
//                // 2. 处于异常状态的服务可以停止
//                logger.info("判断服务是否满足停止条件");
//                state = srvOperationDAO.getSrvOperationState(srvDetailVO.getSrvId());
//                // useK8s.deleteByK8s(srvDetail);
//                if (state == CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_START_SUCCESS").getCode().longValue()//服务启动成功
//                        || state == CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_BUILD_SUCCESS").getCode().longValue()//服务构建成功
//                        || state == CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_BUILD_FAILURE").getCode().longValue()//服务构建失败
//                        || state == CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_DELETE_SUCCESS").getCode().longValue()//服务删除失败
//                        || state == CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_DELETE_FAILURE").getCode().longValue()//服务删除成功
//                        || state == CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_STOP_FAILURE").getCode().longValue()) {//服务停止失败
//                    result = CodeStatusUtil.resultByCodeEn("SERVICE_STOPING");
//                    result.setData("停止用户的userid: " + userId + ",应用：" + appName + "，下的服务：" + srvName + "正在停止");
//                    logger.info("停止 userId: {}, 应用名称： {}, 服务名称: {}，的服务，正在停止中", userId, appName, srvName);
//                    //flag=1表示停止
//                    useK8s.deleteByK8s(srvDetail, 1);
//                } else {
//                    result = CodeStatusUtil.resultByCodeEn("SERVICE_STOP_FAILURE");
//                    result.setData("停止用户的userid: " + userId + ",应用：" + appName + "，下的服务：" + srvName + "服务无法停止，服务停止失败");
//                    logger.info("停止 userId: {}, 应用名称： {}, 服务名称: {}，的服务，无法停止，不是处于启动或异常状态", userId, appName, srvName);
//                    insertOperationLog(JSONObject.toJSONString(result));
//                }
//                break;
//            default:
//                //TODO: 没有匹配操作的处理逻辑
//                result.setData("用户的userid: " + userId + ",应用：" + appName + "，下的服务：" + srvName + "操作失败，无匹配操作");
//                logger.error("flag : {}, 无法识别的操作类型");
//                insertOperationLog(JSONObject.toJSONString(result));
//        }
        return result;
    }

    /**
     * 查询当前用户的服务
     * @param userId
     * @param srvExample
     * @return Result
     */
    @Override
    public Result listUserSrvDetail(int userId,SrvExample srvExample) throws BusinessException{
        Result result;
        int pageNum = srvExample.getPageNum();
        int pageSize = srvExample.getPageSize();
        logger.debug("pageNum" + pageNum + " pageSize:" + pageSize);
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<SrvDetail> srvDetails = srvMngDao.listUserSrvDetail(srvExample);
        PageInfo pageInfo = PageUtil.getPageInfo(page, srvDetails);
        result = CodeStatusUtil.resultByCodeEn ("SERVICE_QUERY_SUCCESS");
        pageInfo.setList(srvDetails);
        result.setData(pageInfo);
        return result;
    }


    /**
     * 条件查询
     *
     * @param srvExample
     * @return srvDetailVOS
     */
    @Override
    public Result listQueryConditions(SrvExample srvExample) throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn (CodeStatusContant.SERVICE_QUERY_FAILURE);
//        int pageNum = srvExample.getPageNum();
//        int pageSize = srvExample.getPageSize();
//        logger.debug("pageNum" + pageNum + " pageSize:" + pageSize);
//        Page page = PageHelper.startPage(pageNum, pageSize);
//        //基本信息，不包含状态，启动停止时间
//        List<SrvDetailOrderVO> srvDetailOrderVOS = srvMngDao.listAllSrvByConditions(srvExample);
//        if(srvDetailOrderVOS.size() == 0){
//            List<SrvDetailOrderVO> srvDetailOrderVOS1FAILURE = new ArrayList<SrvDetailOrderVO>();
//            PageInfo pageInfoF = PageUtil.getPageInfo(page, srvDetailOrderVOS1FAILURE);
//            result = CodeStatusUtil.resultByCodeEn (CodeStatusContant.SERVICE_QUERY_SUCCESS);
//            result.setData(pageInfoF);
//            return  result;
//        }
//        List<SrvDetailOrderVO> srvDetailOrderStateVO = srvOperationDAO.listByExample(getAppExample(srvExample));
//        for (SrvDetailOrderVO srvDetailOrderVO : srvDetailOrderVOS) {
//            for (SrvDetailOrderVO srvDetailOrderVO1 : srvDetailOrderStateVO) {
//                if (srvDetailOrderVO.getSrvId().equals (srvDetailOrderVO1.getSrvId()) ){
//                    srvDetailOrderVO.setStartTime(srvDetailOrderVO1.getStartTime());
//                    srvDetailOrderVO.setStopTime(srvDetailOrderVO1.getStopTime());
//                    srvDetailOrderVO.setState(srvDetailOrderVO1.getState());
//                }
//            }
//        }
//         /*状态校验*/
//        logger.info("状态校验");
//        List<SrvDetailOrderVO> srvDetailOrderVOS1 = new ArrayList<SrvDetailOrderVO>();
//        if(srvExample.getState ()!=null){
//            for (SrvDetailOrderVO srvDetailOrderVO : srvDetailOrderVOS){
//                if (srvDetailOrderVO.getState ().equals (srvExample.getState ())){
//                    srvDetailOrderVOS1.add (srvDetailOrderVO);
//                }
//            }
//        }else srvDetailOrderVOS1 = srvDetailOrderVOS;
//        PageInfo pageInfo = PageUtil.getPageInfo(page, srvDetailOrderVOS1);
//        if (srvDetailOrderVOS != null && srvDetailOrderVOS.size() > 0) {
//            result = CodeStatusUtil.resultByCodeEn (CodeStatusContant.SERVICE_QUERY_SUCCESS);
//            pageInfo.setList(srvDetailOrderVOS1);
//            result.setData(pageInfo);
//        }
        return result;
    }
    /**
     * 查询所有的服务
     * @return
     */
    @Override
    public Result findAll(int srvImageVersionId) {
        Result result = CodeStatusUtil.resultByCodeEn("SERVICE_QUERY_SUCCESS");
        List<SrvDetail> srvDetails = srvMngDao.findAll (srvImageVersionId);
        if (srvDetails.size () == 0){
            result.setMessage ("查询成功，镜像未被引用");
        }else {
            result.setData (srvDetails);
        }
        return result;
    }

    /**
     * 上传服务图片
     * @param file   服务图片
     * @return
     * @throws BusinessException
     */
    @Override
    public Result uploadFile(MultipartFile file) throws BusinessException {
        Result result;
        logger.debug("------进入文件上传service-------");
        //1.获取文件名
        String fileName = file.getOriginalFilename();
        logger.debug("文件名:{}", fileName);
        //2.获取后缀名
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        logger.debug("文件后缀名:{}", suffix);
        //3.判断是否是zip包
        if (!"jpg".equals(suffix) && !"jpeg".equals(suffix) && !"png".equals(suffix) && !"gif".equals(suffix) && !"bmp".equals(suffix)) {
            logger.error("上传文件非图片文件！上传失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("SRV_IMG_UPLOAD_SUCCESS"));
        }
        //4.非空校验
        if (file.isEmpty()) {
            logger.error("上传文件为空！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("SRV_IMG_UPLOAD_FAILURE"));
        }
        //5.文件上传
        UploadFile uploadFile = new UploadFile();
        String filePath = Config.UPLOAD_DIRECTORY_SRV;
        Result srvPathResult = uploadFile.uploadFile(file, filePath);
        //6.获取文件名称
        String uploadName = uploadFile.getOriginalFileName(file);
        //7.数据打包
        result = boxData(uploadName, srvPathResult);
        return result;
    }

    /**
     * 查询服务定义信息
     * @param list
     * @return
     * @throws BusinessException
     */
    @Override
    public Result listSrvVersionDetailByIds(List<Integer> list) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn ("SERVICE_QUERY_FAILURE");
        List srvVersionDetails = srvVersionDetailDAO.listSrvVersionDetailByIds(list);
        if(srvVersionDetails != null && srvVersionDetails.size() > 0){
            result = CodeStatusUtil.resultByCodeEn ("SERVICE_QUERY_SUCCESS");
            result.setData(srvVersionDetails);
        }
        return result;
    }

    private Result boxData(String uploadName, Result busiPkgPathResult) {
        Result result;
        //获取返回值里面数据
        Map data = (HashMap) busiPkgPathResult.getData();
        Map map = new HashMap();
        map.put("path", Config.UPLOAD_DIRECTORY_SRV + data.get("path"));
        result = CodeStatusUtil.resultByCodeEn("SRV_IMG_UPLOAD_SUCCESS");
        result.setData(map);
        return result;
    }

    /**
     * 条件查询
     *
     * @param srvExample
     * @return appExample
     */
    public AppExample getAppExample(SrvExample srvExample) {
        AppExample appExample = new AppExample();
        appExample.setState(srvExample.getState());
        return appExample;
    }

    /**
     * 插入操作日志表
     * @param logContent
     * @return
     */
    private int insertOperationLog(String logContent){
        OperationLog operationLog = new OperationLog();
        operationLog.setCreator("admin");
        operationLog.setLogContent(logContent);
        return operationLogDAO.doInsertByBean(operationLog);
    }

    /**
     * 查询服务定义信息
     * @param userid
     * @param srvVersionDetailExample
     * @return
     * @throws BusinessException
     */
    @Override
    public Result listSrvVersionDetail(String userid, SrvVersionDetailExample srvVersionDetailExample) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn ("SERVICE_QUERY_FAILURE");
        int pageNum = srvVersionDetailExample.getPageNum();
        int pageSize = srvVersionDetailExample.getPageSize();
        logger.debug("pageNum" + pageNum + " pageSize:" + pageSize);
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<SrvVersionDetailVO> list = srvVersionDetailDAO.listSrvVersionDetail(srvVersionDetailExample);
        PageInfo pageInfo = PageUtil.getPageInfo(page, list);
        if(list != null){
            result = CodeStatusUtil.resultByCodeEn ("SERVICE_QUERY_SUCCESS");
            pageInfo.setList(list);
            result.setData(pageInfo);
        }
        return result;
    }

    /**
     * 创建服务定义
     * @param userid
     * @param srvVersionDetailVO
     * @return
     * @throws BusinessException
     */
    @Override
    public Result createSrvDef(String userid, SrvVersionDetailVO srvVersionDetailVO) throws BusinessException {
        Result result;
        logger.debug("------开始创建服务定义-------");
        //1、服务定义数据提取
        logger.info("服务定义数据提取");
        BeanUtil.copyBean2Bean(srvVersionDetail, srvVersionDetailVO);
        //2、插入服务定义数据
        logger.info("插入服务定义数据");
        srvVersionDetail.setCreateTime(dateUtil.getCurrentTime());
        srvVersionDetail.setCreator("ghy");
        //初始化服务镜像状态 1052400为尚未构建
        srvVersionDetail.setSrvVersionStatus("1052400");
        srvVersionDetailDAO.doInsertByBean(srvVersionDetail);
        //3、验证id是否合法
        result = verifySrv.verifySrvId(srvVersionDetail.getSrvVersionId());
        result.setData(srvVersionDetail.getSrvVersionId());
        result.setSuccess(1);
        return result;
    }

    /**
     * 创建并构建镜像并推入仓库
     * @param userid
     * @param srvVersionDetailVO
     * @return 是否成功
     */
    @Override
    public Result createAndBuild(String userid, SrvVersionDetailVO srvVersionDetailVO) {
        //创建dockerfile和镜像
        Result result = createSrvDef (userid,srvVersionDetailVO);
        if (1==result.getSuccess ()){
            //构建并推入仓库
            result = buildSrvImage(userid,(int)result.getData());
        }
        return result;
    }

    /**
     * 构建服务镜像并推入仓库
     * @param userid
     * @param srvVersionId
     * @return
     */
    @Override
    public Result buildSrvImage(String userid, Integer srvVersionId){
        //构建并推入仓库
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.OP_IMAGE) + "/"+ userid +"/createAndBuildSrvImage";
        return createAndBuildSrvImage(userid,srvVersionId,url);
    }

    /**
     * 根据服务实例编号查询服务实例详情
     * @param srvInstId
     * @return
     * @throws BusinessException
     */
    @Override
    public Result findSrvInst(Integer srvInstId) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_QUERY_FAILURE);
        SrvInstDetailVO srvInstDetailVO = new SrvInstDetailVO();
        //1.根据服务实例编号查询服务实例信息
        SrvInstDetail srvInstDetail = srvInstDetailDAO.doFindById(srvInstId);
        if(srvInstDetail == null){
            result.setMessage("无效的服务实例编号");
            throw new BusinessException(result);
        }
        BeanUtil.copyBean2Bean(srvInstDetailVO,srvInstDetail);
        //2.查询服务版本
        SrvVersionDetail srvVersionDetail = srvVersionDetailDAO.doFindById(srvInstDetail.getSrvVersionId());
        srvInstDetailVO.setSrvVersionDetail(srvVersionDetail);
        //3.查询关联的环境变量
        List<SrvEnvRel> srvEnvRels = srvEnvRelDAO.doFindBySrvId(srvInstId);
        if(srvEnvRels != null && srvEnvRels.size() > 0){
            srvInstDetailVO.setSrvEnvRels(srvEnvRels);
        }
        //4.查询关联的扩缩容规则
        SrvScaleRule srvScaleRule = srvScaleRuleDAO.doFindBySrvInstId(srvInstId);
        if(srvScaleRule != null){
            srvInstDetailVO.setSrvScaleRule(srvScaleRule);
        }
        result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_QUERY_SUCCESS);
        result.setData(srvInstDetailVO);
        return result;
    }

    /**
     * 调用rest接口，构建镜像并且推入仓库
     */
    @Override
    public Result createAndBuildSrvImage(String userid,int srvVersionId,String url){
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_IMAGE_BUILD_FAILURE);
        //1.构建srvImageVO信息
        logger.debug("构建srvImageVO信息");
        SrvVersionDetail srvVersionDetail = srvVersionDetailDAO.doFindById(srvVersionId);
        SrvDetail srvDetail = srvMngDao.doFindById(srvVersionDetail.getSrvId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("srvNameEn",srvDetail.getSrvNameEn());
        jsonObject.put("srvNameZh",srvDetail.getSrvNameCh());
        jsonObject.put("srvVersion",srvVersionDetail.getSrvVersion());
        jsonObject.put("busiPkgVersionId",srvVersionDetail.getBusiPkgVersionId());
        jsonObject.put("srvImageVersionId",srvDetail.getSrvImageVersionId());
        if(null != srvVersionDetail.getSrvImageId() && !"".equals(srvVersionDetail.getSrvImageId())) {
            jsonObject.put("srvImageId", srvVersionDetail.getSrvImageId());
        }
        String body = jsonObject.toJSONString();
        logger.debug("srvImageVO信息:{}",body);
        //2.调用rest接口
        logger.debug("调用rest接口");
        ResponseEntity<String> responseEntity = RestClient.doPostForEntity(url,body);
        logger.debug("调用自动构建服务镜像接口返回:{}",responseEntity);
        if(responseEntity != null) {
            //3.获取到构建中的镜像版本编号
            Integer buildImageVersionId = ((JSONObject) JSONObject.parse(responseEntity.getBody())).getInteger("data");
            srvVersionDetail.setSrvImageId(buildImageVersionId);
            //4.1052100为镜像正在构建中
            srvVersionDetail.setSrvVersionStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.IMAGE_BUILDING).getCode().toString());
            logger.debug("更新服务定义状态");
            srvVersionDetailDAO.doUpdateByBean(srvVersionDetail);
        }else{
            logger.debug("服务镜像构建失败");
            result.setMessage("服务镜像构建失败");
            throw new BusinessException(result);
        }
        logger.debug("服务镜像构建成功");
        result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_IMAGE_BUILD_SUCCESS);
        result.setSuccess(1);
        return result;
    }

    /**
     * 同步服务版本镜像状态
     * @return
     * @throws BusinessException
     */
    @Override
    public void syncSrvVersionImageStatus() throws BusinessException {
        //1、查询出服务定义详情,并构造请求数据
        logger.debug("同步服务版本镜像状态开始！");
        List<SrvVersionDetailVO> srvVersionDetailVOS = srvVersionDetailDAO.doFindProcessingImageVersionIds();
        JSONArray jsonArray = new JSONArray();
        for(SrvVersionDetailVO srvVersionDetailVO : srvVersionDetailVOS){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("srvVersionId",srvVersionDetailVO.getSrvVersionId());
            jsonObject.put("srvImageId",srvVersionDetailVO.getSrvImageId());
            jsonArray.add(jsonObject);
        }
        logger.debug("服务定义查询状态请求参数-------------{}",jsonArray.toJSONString());
        //2、rest请求镜像模块接口
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.OP_IMAGE) + "/srv/syncSrvVersionImageStatus";
        String body = jsonArray.toJSONString();
        ResponseEntity<String> response = RestClient.doPostForEntity(url,body);
        if(response != null) {
            logger.debug("调用接口返回:----------------{}", ((JSONObject) JSONObject.parse(response.getBody())).getString("message"));
            if (((JSONObject) JSONObject.parse(response.getBody())).getInteger("success") == 1) {
                JSONArray srvVersionJsonArray = ((JSONObject) JSONObject.parse(response.getBody())).getJSONArray("data");
                for (Object object : srvVersionJsonArray) {
                    SrvVersionDetail srvVersionDetail = new SrvVersionDetail();
                    srvVersionDetail.setSrvVersionId(((JSONObject) object).getInteger("srvVersionId"));
                    srvVersionDetail.setSrvVersionStatus(((JSONObject) object).getString("srvVersionStatus"));
                    srvVersionDetailDAO.doUpdateByBean(srvVersionDetail);
                }
            }
            logger.debug("同步服务版本镜像状态成功！");
        }
    }

    /**
     * 删除服务定义
     * @param userid
     * @param srvVersionId
     * @return
     */
    @Override
    @Transactional
    public Result deleteSrvDef(String userid, Integer srvVersionId) {
        Result result = CodeStatusUtil.resultByCodeEn("SERVICE_DELETE_FAILURE");
        //1.校验当前服务版本下是否存在服务实例
        logger.debug("1.校验当前服务版本下是否存在服务实例");
        SrvInstDetailExample srvInstDetailExample = new SrvInstDetailExample();
        srvInstDetailExample.setSrvVersionId(srvVersionId);
        List<SrvInstDetailVO> list = srvInstDetailDAO.listSrvInstByCondition(srvInstDetailExample);
        //2.获取服务镜像编号
        logger.debug("2.获取服务镜像编号");
        SrvVersionDetail srvVersionDetail = srvVersionDetailDAO.doFindById(srvVersionId);
        Integer srvImageId = srvVersionDetail.getSrvImageId();
        logger.debug("服务镜像编号-----------{}",srvImageId);
        //3.删除服务版本
        logger.debug("2.删除服务版本");
        if(list != null && list.size() > 0){
            result.setMessage ("存在该服务定义的服务实例！不能删除");
        }else if (srvVersionDetailDAO.doDeleteById(srvVersionId)!=0){
            //4.删除服务镜像
            logger.debug("4.调用rest接口删除服务镜像");
            if(srvImageId != null) {
                Boolean response = RestClient.doDelete(PropertiesConfUtil.getInstance().getProperty(RestConstant.OP_IMAGE) + "1" + srvImageId,null);
                if(!response){
                    result.setMessage("调用rest接口删除服务镜像失败");
                    throw new BusinessException(result);
                }
            }
            result = CodeStatusUtil.resultByCodeEn ("SERVICE_DELETE_SUCCESS");
        }
        return result;
    }

    /**
     * 创建服务实例
     * @param srvInstDetailVO
     * @return
     * @throws BusinessException
     */
    @Override
    public int doInsertSrvInst(SrvInstDetailVO srvInstDetailVO){
        //1.服务实例详情数据提取
        logger.info("1.服务实例详情数据提取");
        BeanUtil.copyBean2Bean(srvInstDetail, srvInstDetailVO);
        //2.生成节点端口
        logger.info("2.生成节点端口");
        srvInstDetail.setNodePort(Integer.parseInt(generateNodePort()));
        //3.插入服务实例详情数据
        logger.info("3.插入服务实例详情数据");
        //2133000创建中
        srvInstDetail.setSrvInstStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATING).getCode().intValue());
        srvInstDetail.setHistory(0);
        srvInstDetail.setCreateTime(dateUtil.getCurrentTime());
        srvInstDetailDAO.doInsertByBean(srvInstDetail);
        //4.获取服务实例id
        logger.info("4.获取服务实例id");
        int srvInstId = srvInstDetail.getSrvInstId();
        logger.debug("srvInstId" + srvInstDetail.getSrvInstId());
        //5.创建服务实例环境变量关系、扩缩容规则
        logger.info("5.创建服务环境变量关系、扩缩容、存储、应用服务关系");
        List<SrvEnvRel> srvEnvRels = srvInstDetailVO.getSrvEnvRels();
        SrvScaleRule srvScaleRule = srvInstDetailVO.getSrvScaleRule();
        //5.1创建环境变量
        if (srvInstDetailVO.getSrvEnvRels() != null) {
            int env = createEnv(srvEnvRels);
            if (env == 0) {
                logger.error("创建环境变量失败");
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("SERVICE_INSTANCE_CREATE_FAILURE"));
            }
        }
        //5.2创建扩缩容规则
        if(srvInstDetailVO.getSrvScaleRule() != null) {
            int rule = createSrvScaleRule(srvInstDetailVO);
            if (rule == 0) {
                logger.error("创建扩缩容规则失败");
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("SERVICE_INSTANCE_CREATE_FAILURE"));
            }
        }
        return srvInstId;
    }

    /**
     * @param srvInstDetailVO
     * @Brief:创建扩缩容规则
     * @return：int
     */
    public int createSrvScaleRule(SrvInstDetailVO srvInstDetailVO) {
        int result;
        SrvScaleRule srvScaleRule = srvInstDetailVO.getSrvScaleRule();
        //1.根据服务实例信息查询服务定义以及服务信息
        logger.debug("服务定义编号-----------------{}",srvInstDetailVO.getSrvVersionId());
        SrvVersionDetail srvVersionDetail = srvVersionDetailDAO.doFindById(srvInstDetailVO.getSrvVersionId());
        logger.debug("服务编号-----------------{}",srvVersionDetail.getSrvId());
        SrvDetail srvDetail = srvMngDao.doFindById(srvVersionDetail.getSrvId());
        //2.根据服务、服务定义生成唯一的规则名称 rule_{srvNameEn}_{srvVersion}_{timestamp}
        srvScaleRule.setRuleName("rule_" + srvDetail.getSrvNameEn() + "_" + srvVersionDetail.getSrvVersion() + "_" + dateUtil.getCurrentTime().getTime());
        logger.debug("生成规则名称--------------{}",srvScaleRule.getRuleName());
        //3.设置服务实例编号
        srvScaleRule.setSrvInstId(srvInstDetail.getSrvInstId());
        //4.插入扩缩容规则
        result = srvScaleRuleDAO.doInsertByBean(srvScaleRule);
        if (result == 0) {
            logger.error("插入扩缩容规则失败");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("SERVICE_BUILD_FAILURE"));
        }
        return result;
    }

    /**
     * 创建服务实例
     * @param userid
     * @param srvInstDetailVO
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional
    public Result createSrvInst(String userid, SrvInstDetailVO srvInstDetailVO) throws BusinessException {
        Result result;
        //1.服务实例创建
        logger.info("服务实例开始创建");
        //2.校验成功
        logger.info("服务创建信息校验成功");
        verifySrv.createSrvInstVerify(srvInstDetailVO);
        srvInstDetailVO.setCreator("ghy");
        srvInstDetailVO.setSrvInstStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATING).getCode());
        //3.创建服务操作信息--服务创建中
        logger.info("服务创建中");
        srvImplementService.doOperationDB(srvInstDetailVO,CodeStatusContant.SERVICE_INSTANCE_CREATING,"服务创建中");
        //4.开始创建服务
        int srvInstId = doInsertSrvInst(srvInstDetailVO);
        result = verifySrv.verifySrvId(srvInstId);
        logger.debug("服务实例id------------------"+ srvInstId);
        //5.创建服务操作信息--已创建
        logger.info("服务已创建");
        srvImplementService.doOperationDB(srvInstDetailVO,CodeStatusContant.SERVICE_INSTANCE_CREATED,"服务已创建");
        //6.调用k8s创建服务
        logger.info("调用k8s创建服务");
        srvInstDetailVO.setSrvInstId(srvInstId);
        srvInstDetailVO = srvInstDetailDAO.listSrvInstInfoByInstId(srvInstDetailVO);
        //2130000启动中
        srvInstDetail.setSrvInstStatus(CodeStatusUtil.getInstance().getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_STARTING).getCode().intValue());
        srvInstDetailDAO.doUpdateByBean(srvInstDetail);
        StringBuffer serviceName = new StringBuffer();
        serviceName.append(srvInstDetailVO.getAppDetail().getAppNameEn())
                .append("-")
                .append(srvInstDetailVO.getSrvDetail().getSrvNameEn());
        srvInstDetailVO.setSrvNameEn(serviceName.toString());
        useK8s.creatByK8S(srvInstDetailVO);
        return result;
    }

    /**
     * 根据条件查询服务实例（列表）
     * @param srvInstDetailExample
     * @param userid
     * @return
     */
    @Override
    public Result listSrvInstByCondition(String userid,SrvInstDetailExample srvInstDetailExample) {
        Result result;
        int pageNum = srvInstDetailExample.getPageNum();
        int pageSize = srvInstDetailExample.getPageSize();
        logger.debug("pageNum" + pageNum + " pageSize:" + pageSize);
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<SrvInstDetailVO> srvInstDetailVOS = srvInstDetailDAO.listSrvInstByCondition(srvInstDetailExample);
        PageInfo pageInfo = PageUtil.getPageInfo(page, srvInstDetailVOS);
        result = CodeStatusUtil.resultByCodeEn ("SERVICE_QUERY_SUCCESS");
        pageInfo.setList(srvInstDetailVOS);
        result.setData(pageInfo);
        return result;
    }

    /**
     * 通过服务实例编号获取deployment信息
     * @param srvInstId
     * @return
     */
    @Override
    public Result doFindDeploymentInfo(Integer srvInstId) {
        Result result = CodeStatusUtil.resultByCodeEn ("SERVICE_QUERY_FAILURE");
        //1.构建查询条件
        SrvInstDetailExample srvInstDetail = new SrvInstDetailExample();
        srvInstDetail.setSrvInstId(srvInstId);
        //2.查询服务实例相关信息
        SrvInstDetailVO srvInstDetailVO = srvInstDetailDAO.listSrvInstInfoByInstId(srvInstDetail);
        if(srvInstDetailVO == null || srvInstDetailVO.getAppDetail()== null || srvInstDetailVO.getSrvDetail() == null){
            result.setMessage("查询deploy信息失败");
            result.setSuccess(0);
            return result;
        }
        //3.生成deployment名字和版本8*87787887788544588
        String deploymentName = srvInstDetailVO.getAppDetail().getAppNameEn() + "-" + srvInstDetailVO.getSrvDetail().getSrvNameEn();
        String version = srvInstDetailVO.getSrvVersionDetail().getSrvVersion();
        //4.boxData
        Map map = new HashMap();
        map.put("deploymentName",deploymentName);
        map.put("version",version);
        result = CodeStatusUtil.resultByCodeEn("SERVICE_QUERY_SUCCESS");
        result.setData(map);
        result.setSuccess(1);
        return result;
    }

    /**
     * 版本切换
     * @param userid
     * @param srvInstDetailExample
     * @return
     */
    @Override
    @Transactional
    public Result srvVersionChange(String userid, SrvInstDetailExample srvInstDetailExample) {
        Result result;
        SrvInstDetail srvInstDetail = srvInstDetailDAO.doFindById(srvInstDetailExample.getSrvInstId());
        //1.校验当前服务实例状态
        verifySrv.srvInstStatusVerify(srvInstDetailExample);
        //2.更新启动服务实例状态为启动中,更新是否是历史状态为0，即非历史
        srvInstDetail.setSrvInstStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_STARTING).getCode());
        srvInstDetail.setUpdateTime(dateUtil.getCurrentTime());
        srvInstDetail.setHistory(0);
        if(srvInstDetailDAO.doUpdateByBean(srvInstDetail) != 1){
            result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_MODIFY_FAILURE);
            result.setMessage("服务实例状态更新失败");
            throw new BusinessException(result);
        }
        SrvInstDetailVO srvInstDetailVO = srvInstDetailDAO.listSrvInstInfoByInstId(srvInstDetailExample);
        //3.调用k8s接口启动
        //记录被切换的实例
        srvInstDetailVO.setCurrentSrvInstId(srvInstDetailExample.getCurrentSrvInstId());
        StringBuffer serviceName = new StringBuffer();
        serviceName.append(srvInstDetailVO.getAppDetail().getAppNameEn())
                .append("-")
                .append(srvInstDetailVO.getSrvDetail().getSrvNameEn());
        srvInstDetailVO.setSrvNameEn(serviceName.toString());
        useK8s.updateByK8s(srvInstDetailVO);
        result = CodeStatusUtil.resultByCodeEn("SERVICE_BUILD_SUCCESS");
        return result;
    }

    /**
     * 一键升级
     * @param userid
     * @param srvInstDetailExample
     * @return
     */
    @Override
    @Transactional
    public Result srvVersionUpdate(String userid, SrvInstDetailExample srvInstDetailExample) {
        Result result;
        SrvInstDetail srvInstDetail = srvInstDetailDAO.doFindById(srvInstDetailExample.getCurrentSrvInstId());
        SrvInstDetailVO srvInstDetailVO = srvInstDetailDAO.listSrvInstInfoByInstId(srvInstDetail);
        //1.校验当前服务实例状态
        verifySrv.srvInstStatusVerify(srvInstDetailExample);
        //2.调用业务包接口自动生成新的业务包版本
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.OP_BUSIPKG) + "/1/"+ srvInstDetailVO.getSrvDetail().getBusiPkgId() +"/version/addbybusipkgid";
        String resultJson = RestClient.doGet(url);
        logger.debug("调用业务包生成接口返回:{}",resultJson);
        result = JSONObject.parseObject (resultJson,Result.class);
        if(result.getSuccess() == 1){
            //3.根据业务包版本生成服务版本
            result = autoBuildSrvVersion(userid,srvInstDetailVO,result);
            logger.debug("调用生成服务版本接口返回服务版本编号:{}",result.getData().toString());
            //4.根据上一实例版本生成新的实例
            result = autoBuildSrvInst(srvInstDetail,result);
            logger.debug("调用生成服务实例接口返回服务实例编号:{}",result.getData().toString());
        }
        return result;
    }

    /**
     * jekins调用升级接口
     * @param userid
     * @param appNameEn
     * @param srvNameEn
     * @return
     */
    @Override
    public Result jekinsVersionUpdate(String userid,String appNameEn,String srvNameEn) {
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE);
        Integer srvInstId;
        logger.debug("jekins调用升级接口");
        //1.查询应用名为:{},服务名为:{},active状态的服务实例
        logger.debug("1.查询应用名为:{},服务名为:{},active状态的服务实例编号",appNameEn,srvNameEn);
        SrvInstDetailExample srvInstDetailExample = new SrvInstDetailExample();
        srvInstDetailExample.setAppNameEn(appNameEn);
        srvInstDetailExample.setSrvNameEn(srvNameEn);
        srvInstDetailExample.setNoHistory(1);
        srvInstId = srvInstDetailDAO.querySrvInstByAppAndSrv(srvInstDetailExample);
        if(srvInstId == null ){
            result.setMessage("升级版本失败");
            throw new BusinessException(result);
        }
        //2.设置当前服务实例编号，调用升级接口
        logger.debug("1.设置当前服务实例编号:{},调用升级接口",srvInstId);
        srvInstDetailExample.setCurrentSrvInstId(srvInstId);
        return srvVersionUpdate(userid,srvInstDetailExample);
    }


    /**
     * 自动构建服务版本
     * @param userid
     * @param srvInstDetailVO
     */
    private Result autoBuildSrvVersion(String userid,SrvInstDetailVO srvInstDetailVO,Result result){
        //1.获取新生成的业务包的信息
        logger.debug("获取新生成的业务包的信息");
        int busiPkgVersionId = Integer.parseInt(((JSONObject)result.getData()).get("busiPkgVersionId").toString());
        String busiPkgVersion = ((JSONObject)result.getData()).get("busiPkgVersion").toString();
        //2.自动构建服务版本信息
        logger.debug("自动构建服务版本信息");
        SrvVersionDetailVO srvVersionDetailExample = new SrvVersionDetailVO();
        srvVersionDetailExample.setSrvId(srvInstDetailVO.getSrvDetail().getSrvId());
        //服务版本使用业务包版本
        srvVersionDetailExample.setSrvVersion(busiPkgVersion);
        srvVersionDetailExample.setBusiPkgVersion(busiPkgVersion);
        srvVersionDetailExample.setBusiPkgVersionId(busiPkgVersionId);
        result = createSrvDef (userid,srvVersionDetailExample);
        if (1==result.getSuccess ()){
            logger.debug("服务定义创建成功-------------{}",result);
        }
        return result;
    }

    /**
     * 自动构建服务实例
     * @param result
     * @param srvInstDetail
     */
    private Result autoBuildSrvInst(SrvInstDetail srvInstDetail,Result result){
        //1.判断服务定义是否创建成功
        logger.debug("1.判断服务定义是否创建成功");
        if(result.getSuccess() == 1) {
            SrvInstDetail newBean = new SrvInstDetail();
            BeanUtil.copyBean2Bean(newBean, srvInstDetail);
            newBean.setSrvInstId(null);
            newBean.setHistory(0);
            newBean.setSrvVersionId(Integer.parseInt(result.getData().toString()));
            newBean.setSrvInstStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_WAIT_PUBLISH).getCode());//2134000尚未启动
            //2.插入新的服务实例信息
            logger.debug("2.插入新的服务实例信息");
            if(srvInstDetailDAO.doInsertByBean(newBean) == 1) {
                //设置inactive
                srvInstDetail.setHistory(1);
                srvInstDetailDAO.doUpdateByBean(srvInstDetail);
                result =  CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATED);
                result.setData(newBean.getSrvInstId());
            }else{
                result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE);
                result.setMessage("服务升级失败");
                throw new BusinessException(result);
            }
        }
        return result;
    }



    /**
     * 生成节点端口
     * @return
     */
    private String generateNodePort(){
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE);
        String nodePort = srvInstDetailDAO.doFindNodePort();
        if(Integer.parseInt(nodePort) > Integer.parseInt(Config.NODE_PORT_RANGE)){
            logger.error("节点端口超出集群范围");
            throw new BusinessException(result);
        }
        return nodePort;
    }

    /**
     * 轮询业务包状态信息
     * @return
     */
    @Override
    public Result pollingBusiPkgStatus() {
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_QUERY_FAILURE);
        //1.构建业务包状态查询接口的请求
        //自动构建的服务镜像的初始状态均为1052400即尚未构建
        logger.debug("1.构建业务包状态查询接口的请求");
        SrvVersionDetailExample srvVersionDetailExample = new SrvVersionDetailExample();
        srvVersionDetailExample.setSrvVersionStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.IMAGE_UNBUILD).getCode().toString());
        List<SrvVersionDetailVO> list = srvVersionDetailDAO.listSrvVersionDetail(srvVersionDetailExample);
        logger.debug("服务版本查询成功{}",list);
        JSONArray jsonArray = new JSONArray();
        for(SrvVersionDetailVO srvVersionDetailVO : list){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(String.valueOf(srvVersionDetailVO.getSrvVersionId()),srvVersionDetailVO.getBusiPkgVersionId());
            jsonArray.add(jsonObject);
        }
        logger.debug("服务版本业务包信息{}",jsonArray.toJSONString());
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.OP_BUSIPKG) + "/version/findStatusByIdsReturnSrvVersionIds";
        //2.调用业务包状态查询接口
        logger.debug("2.调用业务包状态查询接口{}",url);
        ResponseEntity<String> response = RestClient.doPostForEntity(url,jsonArray.toJSONString());
        if(response != null) {
            return JSONObject.parseObject(response.getBody(), Result.class);
        }else{
            result.setMessage("业务包状态查询接口调用失败");
            throw new BusinessException(result);
        }
    }

    /**
     * 删除服务实例
     * @param userid
     * @param srvInstId
     * @return
     */
    @Override
    @Transactional
    public Result deleteSrvInst(String userid, Integer srvInstId) {
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_DELETE_FAILURE);
        //1.获取服务实例最新状态
        logger.info("获取服务实例最新状态");
        Long state = srvOperationDAO.getSrvOperationState(srvInstId);
        if (state != CodeStatusUtil.getInstance().getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_STARTING).getCode().longValue()
                && state != CodeStatusUtil.getInstance().getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_RUNNING).getCode().longValue()) {
            if (deleteSrvInstDetailById(srvInstId)!=0){
                SrvOperation srvOperation = new SrvOperation ();
                srvOperation.setSrvId(srvInstId);
                srvOperation.setState(CodeStatusUtil.getInstance().getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_DELETE_SUCCESS).getCode().longValue());
                srvOperation.setOperationDesc("服务实例删除成功");
                srvOperation.setCreator ("ghy");
                //向服务操作表中插入一条记录，显示服务启动中
                logger.info("新增服务操作表记录");
                srvOperationDAO.doInsertByBean (srvOperation);
                result = CodeStatusUtil.resultByCodeEn (CodeStatusContant.SERVICE_INSTANCE_DELETE_SUCCESS);
            }
        }else {
            result.setMessage ("服务实例正在运行！不能删除");
        }
        return result;
    }

    /**
     * 通过ID删除服务实例
     * @param srvInstId
     * @return：Result
     */
    private int deleteSrvInstDetailById(Integer srvInstId) {
        //1.删除关联的环境变量
        srvEnvRelDAO.doFindBySrvId(srvInstId);
        //2.删除扩缩容规则
        srvScaleRuleDAO.doDeleteById(srvInstId);
        //3.删除服务实例
        return srvInstDetailDAO.doDeleteById(srvInstId);
    }

    /**
     * 修改服务实例
     * @param userid
     * @param srvInstDetailVO
     * @return
     */
    @Override
    @Transactional
    public Result editSrvInst(String userid, SrvInstDetailVO srvInstDetailVO) {
        Result result;
        //1.服务实例修改
        logger.info("服务实例修改");
        //2.开始创建服务
        int srvInstId = doUpdateSrvInst(srvInstDetailVO);
        result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_MODIFY_SUCCESS);
        logger.debug("服务实例id------------------"+ srvInstId);
        return result;
    }

    /**
     * 创建服务实例
     * @param srvInstDetailVO
     * @return
     * @throws BusinessException
     */
    @Override
    public int doUpdateSrvInst(SrvInstDetailVO srvInstDetailVO){
        //1.服务实例详情数据提取
        logger.info("1.服务实例详情数据提取");
        BeanUtil.copyBean2Bean(srvInstDetail, srvInstDetailVO);
        //2.更新服务实例详情数据
        logger.info("2.更新服务实例详情数据");
        srvInstDetail.setUpdateTime(dateUtil.getCurrentTime());
        srvInstDetailDAO.doUpdateByBean(srvInstDetail);
        //3.获取服务实例id
        logger.info("3.获取服务实例id");
        int srvInstId = srvInstDetail.getSrvInstId();
        logger.debug("srvInstId--------------{}",srvInstDetail.getSrvInstId());
        //4.删除原有扩缩容信息
        srvEnvRelDAO.doDeleteById(srvInstDetail.getSrvInstId());
        srvScaleRuleDAO.doFindBySrvId(srvInstDetail.getSrvInstId());
        //5.创建服务实例环境变量关系、扩缩容规则
        logger.info("4.创建服务环境变量关系、扩缩容、存储、应用服务关系");
        List<SrvEnvRel> srvEnvRels = srvInstDetailVO.getSrvEnvRels();
        SrvScaleRule srvScaleRule = srvInstDetailVO.getSrvScaleRule();
        //5.1创建环境变量
        if (srvEnvRels != null) {
            int env = createEnv(srvEnvRels);
            if (env == 0) {
                logger.error("创建环境变量失败");
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("SERVICE_INSTANCE_CREATE_FAILURE"));
            }
        }
        //5.2创建扩缩容规则
        if(srvScaleRule != null) {
            int rule = createSrvScaleRule(srvInstDetailVO);
            if (rule == 0) {
                logger.error("创建扩缩容规则失败");
                throw new BusinessException(CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE));
            }
        }
        return srvInstId;
    }

    /**
     * 启动服务实例
     * @param srvInstId
     * @return
     */
    @Override
    public Result srvInstRun(Integer srvInstId) {
        logger.info("启动服务实例");
        srvInstDetail = srvInstDetailDAO.doFindById(srvInstId);
        SrvInstDetailVO srvInstDetailVO = srvInstDetailDAO.listSrvInstInfoByInstId(srvInstDetail);
        //2130000启动中
        srvInstDetail.setSrvInstStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_STARTING).getCode());
        srvInstDetailDAO.doUpdateByBean(srvInstDetail);
        //判断是否已存在
        String deploymentName = srvInstDetailVO.getAppDetail().getAppNameEn() + "-" + srvInstDetailVO.getSrvDetail().getSrvNameEn();
        String result = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_DEPLOYMENT) + deploymentName + "/status");
        if(result != null){
            logger.info("调用k8s更新服务");
            srvInstDetailVO.setSrvNameEn(deploymentName);
            useK8s.updateByK8s(srvInstDetailVO);
        } else{
            logger.info("调用k8s创建服务");
            srvInstDetailVO.setSrvNameEn(deploymentName);
            useK8s.creatByK8S(srvInstDetailVO);
        }
        return CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_RUNNING);
    }

    /**
     * 停止服务实例
     * @param srvInstId
     * @return
     */
    @Override
    public Result srvInstStop(Integer srvInstId) {
        Result result;
        logger.info("停止服务实例");
        srvInstDetail = srvInstDetailDAO.doFindById(srvInstId);
        SrvInstDetailVO srvInstDetailVO = srvInstDetailDAO.listSrvInstInfoByInstId(srvInstDetail);
        srvInstDetail.setSrvInstStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_STOPING).getCode());
        srvInstDetailDAO.doUpdateByBean(srvInstDetail);
        logger.info("调用k8s停止服务");

        useK8s.deleteByK8s(srvInstDetailVO);
        result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_STOPING);
        result.setSuccess(1);
        return result;
    }

    /**
     * 轮询容器日志
     * @param srvInstId
     * @return
     */
    @Override
    public Result rollingContainerLog(Integer srvInstId) {
        logger.debug("轮询容器日志开始");
        Result result;
        //1.根据服务实例编号查询pod的label
        logger.debug("1.根据服务实例编号查询pod的label");
        srvInstDetail = srvInstDetailDAO.doFindById(srvInstId);
        SrvInstDetailVO srvInstDetailVO = srvInstDetailDAO.listSrvInstInfoByInstId(srvInstDetail);
        String app = srvInstDetailVO.getAppDetail().getAppNameEn() + "-" + srvInstDetailVO.getSrvDetail().getSrvNameEn();
        logger.debug("app:{},version:{}",app);
        //2.根据deployment名称获取当前namespace
        logger.debug("2.根据deployment名称获取当前namespace");
        String namespace = K8sOperationUtil.getInstance().getDeploymentNamespace(app);
        logger.debug("当前namespace:{}",namespace);
        //3.查询pod的名称列表
        logger.debug("3.查询pod的名称列表");
        List<String> podNameList = K8sOperationUtil.getInstance().getPodName(app);
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_K8S_DASHBOARD_POD_LOG);
        url = url.replace("{namespace}",namespace);
        JSONObject resultData = new JSONObject();
        for (String podName : podNameList){
            url = url.replace("{podName}",podName);
            String jsonResult = RestClient.doGet(url);
            resultData.put(podName,JSONObject.parseObject(jsonResult));
        }
        result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.CONTAINER_LOG_QUERY_SUCCESS);
        result.setData(resultData);
        return result;
    }

    /**
     * 服务编排顺序
     * @param srvInstDetails
     * @return
     */
    @Override
    public Result doUpdateBySrvInstDetail(List<SrvInstDetail> srvInstDetails) {
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_MODIFY_FAILURE);
        for(SrvInstDetail srvInstDetail : srvInstDetails){
            SrvInstDetail srvInstance = srvInstDetailDAO.doFindById(srvInstDetail.getSrvInstId());
            srvInstance.setStartOrder(srvInstDetail.getStartOrder());
            if(1 != srvInstDetailDAO.doUpdateByBean(srvInstance)){
                result.setMessage("服务编排失败");
                throw new BusinessException(result);
            }
        }
        result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_MODIFY_SUCCESS);
        return result;
    }

}
