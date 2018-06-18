package com.cloud.paas.appservice.service.impl.app;

import com.cloud.paas.appservice.dao.*;
import com.cloud.paas.appservice.model.AppDetail;
import com.cloud.paas.appservice.qo.AppExample;
import com.cloud.paas.appservice.service.app.AppMngService;
import com.cloud.paas.appservice.service.impl.BaseServiceImpl;
import com.cloud.paas.appservice.vo.app.AppDetailVO;
import com.cloud.paas.appservice.vo.app.AppSrvListVO;
import com.cloud.paas.appservice.vo.srv.SrvDetailOrderVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cloud.paas.appservice.service.srv.SrvMngService;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatus;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.date.DateUtil;
import com.cloud.paas.util.page.PageUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Author: wangzhifeng
 * @Description:
 * @Date: Create in 10:49 2017/10/19
 * @Modified by:
 */

@Service("appMngServiceImpl")
@Transactional
public class AppMngServiceImpl extends BaseServiceImpl<AppDetail> implements AppMngService {
    private static final Logger logger = LoggerFactory.getLogger(AppMngServiceImpl.class);


    @Autowired
    AppMngDAO appDao;

    @Autowired
    SrvOperationDAO srvOperationDAO;

    @Autowired
    private AppSrvRelDAO appSrvRelDao;
    @Autowired
    private SrvMngDAO srvMngDao;
    @Autowired
    private SrvMngService srvMngService;

    @Override
    public BaseDAO<AppDetail> getBaseDAO() {
        return appDao;
    }

    /**
     * 获取所有用户下所有应用列表
     *
     * @return
     */
    public Result listApps() throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("APP_QUERY_FAILURE");
        List<AppDetail> appDetailList = null;
        //数据库查询
        try {
            appDetailList = appDao.listApps();
        } catch (Exception e) {
            logger.error("查询应用出错", e);
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("APP_QUERY_FAILURE"));
        }
        result = CodeStatusUtil.resultByCodeEn("APP_QUERY_SUCCESS");
        result.setData(appDetailList);
        logger.debug("message:" + result.getMessage());
        return result;
    }

    /**
     * 条件查询应用列表
     *
     * @param appExample 查询条件
     * @return
     */
    public Result listByCondition(AppExample appExample) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("APP_QUERY_FAILURE");
        int pageNum = appExample.getPageNum();
        int pageSize = appExample.getPageSize();
        logger.debug("pageNum:{},pageSize:{}", pageNum, pageSize);
        //分页查询
        Page page = PageHelper.startPage(pageNum, pageSize);
        //查询数据库
        List<AppDetail> appDetailList = appDao.listByCondition(appExample);
        PageInfo pageInfo = PageUtil.getPageInfo(page, appDetailList);
        pageInfo.setList(appDetailList);
        if (null != pageInfo) {
            result = CodeStatusUtil.resultByCodeEn("APP_QUERY_SUCCESS");
            result.setData(pageInfo);
        }
        return result;
    }

    /**
     * 根据应用id查询应用详情
     *
     * @param userId
     * @param appId
     * @return
     */
    public Result getAppById(String userId, Integer appId) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("APP_QUERY_FAILURE");
        AppDetail appDetail = null;
        try {
            appDetail = appDao.doFindById(appId);
        } catch (Exception e) {
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("APP_QUERY_FAILURE"));
        }
        result = CodeStatusUtil.resultByCodeEn("APP_QUERY_SUCCESS");
        result.setData(appDetail);
        logger.debug("message:" + result.getMessage());
        return result;
    }

    /**
     * @Brief: 根据应用的名称查询应用详情
     * @Param:
     * @Return:
     */
    public AppDetail queryAppByName(String appName) throws BusinessException {
        if (appName.trim() == null) {
            logger.error("appName can't be empty");
        }
        logger.debug("appName: {}", appName);
        return appDao.queryAppByName(appName);
    }

    public AppDetail queryAppByEnName(String appName) throws BusinessException {
        if (appName.trim() == null) {
            logger.error("appName can't be empty");
        }
        logger.debug("appName: {}", appName);
        return appDao.queryAppByEnName(appName);
    }

    /**
     * @Brief: 根据用户id查询该用户下的所有应用
     * @Param: 用户ID
     * @Return:
     */
    public List<AppDetail> getAppList(String userID) {
        //TODO: 根据用户id查询该用户下的所有应用

        return null;
    }

    /**
     * @Brief: 获取所有用户的所有应用
     * @Param: 无
     * @Return:
     */
    public List<AppDetail> getAppList() {
        //TODO: 获取所有用户的所有应用

        return null;
    }

    /**
     * @Brief: 根据应用ID查询应用信息
     * @Param:
     * @Return:
     */
    public List<AppDetailVO> listAppSrv(int appId) {
        return appDao.listAppSrv(appId);
    }

    /**
     * @Brief: 根据应用条件查询应用信息
     * @Param:
     * @Return:
     */
    public List<AppDetailVO> listAppByCondition(AppExample appExample) {
        return appDao.listAppByCondition(appExample);
    }

    /**
     * @param appId
     * @return AppSrvListVO 应用+服务列表 两张表
     */
    public List<AppSrvListVO> listAppSrvList(int appId) {
        return appDao.listAppSrvList(appId);
    }

    /**
     * 查询应用下服务列表
     *
     * @param appId
     * @return AppSrvListOrderVO 应用+服务顺序列表 两张表
     */
    public Result findlistAppSrvListOrderByAppId(int appId) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("APP_QUERY_FAILURE");
        AppSrvListVO appSrvListVO = null;
        //1.返回AppSrvListOrderVO类型的List
        try {
            appSrvListVO = appDao.findlistAppSrvListOrderByAppId(appId);
        } catch (Exception e) {
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("APP_QUERY_FAILURE"));
        }
        int count = 0;
        if (appSrvListVO != null && null != appSrvListVO.getSrvDetailOrderVOList() && appSrvListVO.getSrvDetailOrderVOList().size() > 0) {
            count = appSrvListVO.getSrvDetailOrderVOList().size();
            logger.debug("{查询到的服务个数}", count);
            //TODO: 启动时间，停止时间，状态
            //遍历应用列表
            Integer id = appSrvListVO.getAppId();
            AppExample appExample = new AppExample();
            appExample.setAppId(id);
            //根据应用ID查询应用下的服务状态列表
            List<SrvDetailOrderVO> srvDetailOrderVOList = srvOperationDAO.listByExample(appExample);
            //根据服务状态列表设置应用及服务
            setAppSrvState(appSrvListVO, srvDetailOrderVOList);
            //设置应用运行时间
            appSrvListVO.setRunningTime(getAppRunningTime(appSrvListVO.getStartTime(), appSrvListVO.getStopTime()));
            appSrvListVO.setSrvSize(count);
        }
        if (appSrvListVO != null) {
            result = CodeStatusUtil.resultByCodeEn("APP_QUERY_SUCCESS");
            result.setData(appSrvListVO);
        }
        if(appSrvListVO==null){
            result= CodeStatusUtil.resultByCodeEn("APP_NOT_FOUND");
        }
        return result;
    }

    /**
     * 分页
     */
    public Result listAppByConditionResult(String userid, AppExample appExample) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("APP_QUERY_PAGE_FAILURE");
        int pageNum = appExample.getPageNum();
        int pageSize = appExample.getPageSize();
        logger.debug("页码pageNum" + pageNum + " 页面大小pageSize:" + pageSize);
        Page page = PageHelper.startPage(pageNum, pageSize);
        logger.debug("分页成功");
        //1.获取AppIdList
        //xml 写为List的时候 pageNum 页码 pageSize 每页的记录数
        List<AppDetail> appDetailList = appDao.findAppIdList(appExample);
        List<Integer> appIdList = new ArrayList<Integer>();
        //2.将查询到的应用详情写入appIdList
        for (AppDetail appDetail : appDetailList) {
            appIdList.add(appDetail.getAppId());
        }
        if (0 == appIdList.size()) {
            result = CodeStatusUtil.resultByCodeEn("APP_QUERY_PAGE_SUCCESS");
            PageInfo pageInfo = PageUtil.getPageInfo(page, appIdList);
            result.setData(pageInfo);
            return result;
        }
        //刷新获取此时的状态
        List<AppSrvListVO> appSrvListVOS = null;
        try {
            //list转换为数组
            appSrvListVOS = listConvertArray(userid, appIdList);
        } catch (Exception e) {
            logger.error("刷新应用列表异常:{}", e.getMessage());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("APP_QUERY_FAILURE"));
        }
        //加一层状态过滤
        List<AppSrvListVO> appFilterSrvListVOList = null;
        if (appExample.getState() != null) {
            //获取满足查询条件中状态的应用列表
            appFilterSrvListVOList = listAppByAppState(appExample, appSrvListVOS);
        } else {
            appFilterSrvListVOList = appSrvListVOS;
        }
        List<Integer> appIdSecondList = new ArrayList<Integer>();
        for (AppSrvListVO appSrvListVO : appFilterSrvListVOList) {
            appIdSecondList.add(appSrvListVO.getAppId());
        }
        PageInfo pageInfo = PageUtil.getPageInfo(page, appIdSecondList);
        //3.pageInfo赋值
        try {
            if (appIdSecondList.size() > 0) {
                pageInfo.setList(listConvertArray(userid, appIdSecondList));
            }
        } catch (Exception e) {
            logger.error("获取应用信息失败:{}", e.getMessage());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("APP_QUERY_FAILURE"));
        }
        if (null != pageInfo) {
            result = CodeStatusUtil.resultByCodeEn("APP_QUERY_PAGE_SUCCESS");
            result.setData(pageInfo);
        }
        logger.debug("message:" + result.getMessage());
        return result;
    }

    /**
     * List转换数组  并刷新状态
     */
    public List listConvertArray(String userid, List<Integer> appIdList) throws BusinessException {
        int size = appIdList.size();
        Integer[] appIds = (Integer[]) appIdList.toArray(new Integer[size]);
        return (List) refreshState(userid, appIds).getData();
    }

    /**
     * @param appExample
     * @return 返回符合条件的AppSrvListVO 二张表 应用+服务列表
     */
    public Result listAppSrvListByCondition(AppExample appExample) throws BusinessException {
        List<AppSrvListVO> appSrvListVOS=null;
        try {
            appSrvListVOS = appDao.listAppSrvListByCondition(appExample);
        } catch (Exception e) {
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("APP_QUERY_FAILURE"));
        }
        Result result = CodeStatusUtil.resultByCodeEn("APP_QUERY_SUCCESS");
        result.setData(appSrvListVOS);
        return result;
    }

    /**
     * @param listAppSrvListVO
     * @return 返回符合条件的AppSrvListVOSupport 二张表 应用+服务列表处理
     */
    public Result listAppSrvListByCondition(List<AppSrvListVO> listAppSrvListVO, AppExample appExample)
            throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("APP_QUERY_FAILURE");
        if (null != listAppSrvListVO) {
            //遍历应用列表
            for (AppSrvListVO appSrvListVO : listAppSrvListVO) {
                Integer appId = appSrvListVO.getAppId();
                appExample.setAppId(appId);
                //根据应用ID查询应用下的服务状态列表
                List<SrvDetailOrderVO> srvDetailOrderVOList = srvOperationDAO.listByExample(appExample);
                //根据服务状态列表设置应用及服务
                setAppSrvState(appSrvListVO, srvDetailOrderVOList);
            }
            //根据应用状态查询应用
            List<AppSrvListVO> appSrvListVOS = listAppSrvListVO;
            if (null != appExample.getState()) {
                appSrvListVOS = listAppByAppState(appExample, listAppSrvListVO);
            }
            result = CodeStatusUtil.resultByCodeEn("APP_QUERY_SUCCESS");
            result.setData(appSrvListVOS);
        }
        logger.debug("message:" + result.getMessage());
        return result;
    }

    /**
     * 设置应用状态、服务个数、应用启停时间；服务状态、启动顺序、服务启停时间
     *
     * @param appSrvListVO
     * @param srvDetailOrderVOList
     */
    private void setAppSrvState(AppSrvListVO appSrvListVO, List<SrvDetailOrderVO> srvDetailOrderVOList) throws BusinessException {
        final Integer APP_BUILD_SUCCESS = CodeStatusUtil.getInstance().getStatusByCodeEn("APP_BUILD_SUCCESS").getCode();
        Long appState = APP_BUILD_SUCCESS.longValue();
        Date appStart = null;
        Date appStop = null;
        Integer appSize = appSrvListVO.getSrvDetailOrderVOList().size();
        logger.debug("应用个数{}", appSize);
        if (null != srvDetailOrderVOList && srvDetailOrderVOList.size() > 0) {
            List<SrvDetailOrderVO> srvDetailOrderVOS = appSrvListVO.getSrvDetailOrderVOList();
            for (int i = 0; i < srvDetailOrderVOS.size(); i++) {
                SrvDetailOrderVO srvDetail = srvDetailOrderVOS.get(i);
                SrvDetailOrderVO srvOperation = srvDetailOrderVOList.get(i);
                Long srvState = srvOperation.getState();
                Date srvStart = srvOperation.getStartTime();
                Date srvStop = srvOperation.getStopTime();

                srvDetail.setState(srvState);
                srvDetail.setStartTime(srvStart);
                //服务停止时间不小于服务启动时间
                if (srvStart != null && srvStop != null && srvStop.getTime() < srvStart.getTime()) {
                    srvStop = null;
                }
                srvDetail.setStopTime(srvStop);
                //应用启动时间
                appStart = getAppStartTime(appStart, srvStart);
                //应用停止时间
                appStop = getAppStopTime(appStop, srvStop);
            }
            //应用状态
            appState = getAppState(srvDetailOrderVOList);
        }
        appSrvListVO.setSrvSize(appSize);
        appSrvListVO.setAppState(appState);
        appSrvListVO.setStartTime(appStart);
        //应用停止时间不小于应用启动时间
        if (appStart != null && appStop != null && appStop.getTime() < appStart.getTime()) {
            appStop = null;
        }
        appSrvListVO.setStopTime(appStop);
    }

    /**
     * 根据服务状态获得应用状态
     *
     * @param srvDetailOrderVOList
     * @return
     */
    private Long getAppState(List<SrvDetailOrderVO> srvDetailOrderVOList) throws BusinessException {


        //应用状态
        final Integer APP_BUILD_SUCCESS = CodeStatusUtil.getInstance().getStatusByCodeEn("APP_BUILD_SUCCESS").getCode();
        final Integer APP_BUILDING = CodeStatusUtil.getInstance().getStatusByCodeEn("APP_BUILDING").getCode();
        final Integer APP_BUILD_FAILURE = CodeStatusUtil.getInstance().getStatusByCodeEn("APP_BUILD_FAILURE").getCode();
        final Integer APP_STARTING = CodeStatusUtil.getInstance().getStatusByCodeEn("APP_STARTING").getCode();
        final Integer APP_START_SUCCESS = CodeStatusUtil.getInstance().getStatusByCodeEn("APP_START_SUCCESS").getCode();
        final Integer APP_START_FAILURE = CodeStatusUtil.getInstance().getStatusByCodeEn("APP_START_FAILURE").getCode();
        final Integer APP_STOPING = CodeStatusUtil.getInstance().getStatusByCodeEn("APP_STOPING").getCode();
        final Integer APP_STOP_SUCCESS = CodeStatusUtil.getInstance().getStatusByCodeEn("APP_STOP_SUCCESS").getCode();
        final Integer APP_STOP_FAILURE = CodeStatusUtil.getInstance().getStatusByCodeEn("APP_STOP_FAILURE").getCode();

        //服务状态
        final Integer SERVICE_BUILDING = CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_BUILDING").getCode();
        final Integer SERVICE_BUILD_SUCCESS = CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_BUILD_SUCCESS").getCode();
        final Integer SERVICE_BUILD_FAILURE = CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_BUILD_FAILURE").getCode();
        final Integer SERVICE_STARTING = CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_STARTING").getCode();
        final Integer SERVICE_START_SUCCESS = CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_START_SUCCESS").getCode();
        final Integer SERVICE_START_FAILURE = CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_START_FAILURE").getCode();
        final Integer SERVICE_STOPING = CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_STOPING").getCode();
        final Integer SERVICE_STOP_SUCCESS = CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_STOP_SUCCESS").getCode();
        final Integer SERVICE_STOP_FAILURE = CodeStatusUtil.getInstance().getStatusByCodeEn("SERVICE_STOP_FAILURE").getCode();

        //deploy状态
        final Integer DEPLOY_BUILDING = CodeStatusUtil.getInstance().getStatusByCodeEn("DEPLOY_BUILDING").getCode();
        final Integer DEPLOY_BUILD_SUCCESS = CodeStatusUtil.getInstance().getStatusByCodeEn("DEPLOY_BUILDING").getCode();
        final Integer DEPLOY_BUILD_FAILURE = CodeStatusUtil.getInstance().getStatusByCodeEn("DEPLOY_BUILD_FAILURE").getCode();
        final Integer DEPLOY_DELETING = CodeStatusUtil.getInstance().getStatusByCodeEn("DEPLOY_DELETING").getCode();
        final Integer DEPLOY_DELETE_SUCCESS = CodeStatusUtil.getInstance().getStatusByCodeEn("DEPLOY_DELETE_SUCCESS").getCode();
        final Integer DEPLOY_DELETE_FAILURE = CodeStatusUtil.getInstance().getStatusByCodeEn("DEPLOY_DELETE_FAILURE").getCode();

        //service状态
        final Integer SRV_BUILDING = CodeStatusUtil.getInstance().getStatusByCodeEn("SRV_BUILDING").getCode();
        final Integer SRV_BUILD_SUCCESS = CodeStatusUtil.getInstance().getStatusByCodeEn("SRV_BUILD_SUCCESS").getCode();
        final Integer SRV_BUILD_FAILURE = CodeStatusUtil.getInstance().getStatusByCodeEn("SRV_BUILD_FAILURE").getCode();
        final Integer SRV_DELETING = CodeStatusUtil.getInstance().getStatusByCodeEn("SRV_DELETING").getCode();
        final Integer SRV_DELETE_SUCCESS = CodeStatusUtil.getInstance().getStatusByCodeEn("SRV_DELETE_SUCCESS").getCode();
        final Integer SRV_DELETE_FAILURE = CodeStatusUtil.getInstance().getStatusByCodeEn("SRV_DELETE_FAILURE").getCode();

        //应用状态,设置默认状态
        Integer appState = APP_BUILD_SUCCESS;

        //服务状态个数

        int srvDeployStartSuccessCount = 0;
        int srvDeployStartFailureCount = 0;
        int srvDeployDeleteSuccessCount = 0;
        int srvDeployDeleteFailureCount = 0;


        int srvServiceStartSuccessCount = 0;
        int srvServiceStartFailureCount = 0;
        int srvServiceDeleteSuccessCount = 0;
        int srvServiceDeleteFailureCount = 0;


        int srvStartingCount = 0;
        int srvStartSuccessCount = 0;
        int srvStartFailureCount = 0;

        int srvStopingCount = 0;
        int srvStopSuccessCount = 0;
        int srvStopFailureCount = 0;

        //服务个数
        int srvCount = srvDetailOrderVOList.size();
        for (SrvDetailOrderVO srvDetailOrderVO : srvDetailOrderVOList) {
            Long srvStateLong = srvDetailOrderVO.getState();
            if (null != srvStateLong) {
                Integer srvState = srvDetailOrderVO.getState().intValue();
                if (srvState != null) {
                    //1.服务启动中
                    if (SERVICE_STARTING.equals(srvState)) {
                        //服务启动中个数
                        srvStartingCount += 1;
                    } else if (DEPLOY_BUILD_SUCCESS.equals(srvState)) {
                        //服务deploy创建成功个数
                        srvDeployStartSuccessCount += 1;
                    } else if (DEPLOY_BUILD_FAILURE.equals(srvState)) {
                        //服务deploy创建失败个数 导致服务启动失败
                        srvDeployStartFailureCount += 1;
                        break;
                    } else if (SRV_BUILD_SUCCESS.equals(srvState)) {
                        //服务service创建成功个数
                        srvServiceStartSuccessCount += 1;
                    } else if (SRV_BUILD_FAILURE.equals(srvState)) {
                        //服务service创建失败个数 导致服务启动失败
                        srvServiceStartFailureCount += 1;
                        break;
                    } else if (SERVICE_BUILD_SUCCESS.equals(srvState)) {
                        //服务创建成功个数
                        srvStartSuccessCount += 1;
                    } else if (SERVICE_BUILD_FAILURE.equals(srvState)) {
                        //服务创建失败个数
                        srvStartFailureCount += 1;
                    }
                    //2.服务停止中
                    else if (SERVICE_STOPING.equals(srvState)) {
                        //服务停止中个数
                        srvStopingCount += 1;
                    } else if (SRV_DELETE_SUCCESS.equals(srvState)) {
                        //服务service删除成功个数
                        srvServiceDeleteSuccessCount += 1;
                    } else if (SRV_DELETE_FAILURE.equals(srvState)) {
                        //服务service删除失败个数 导致服务停止失败
                        srvServiceDeleteFailureCount += 1;
                        break;
                    } else if (DEPLOY_DELETE_SUCCESS.equals(srvState)) {
                        //服务deploy删除成功个数
                        srvDeployDeleteSuccessCount += 1;
                    } else if (DEPLOY_DELETE_FAILURE.equals(srvState)) {
                        //服务deploy创建失败个数 导致服务启动失败
                        srvDeployDeleteFailureCount += 1;
                        break;
                    } else if (SERVICE_STOP_SUCCESS.equals(srvState)) {
                        //服务删除成功个数
                        srvStopSuccessCount += 1;
                    } else if (SERVICE_STOP_FAILURE.equals(srvState)) {
                        //服务删除失败个数
                        srvStopFailureCount += 1;
                    }
                }
            }
        }
//        logger.debug("启动中服务个数：{}",srvStartingCount);
//        logger.debug("停止中的服务个数：{}",srvStopingCount);
        //根据服务状态的个数来判断应用的状态
        //1.deploy创建失败 service创建失败
        if (srvDeployStartFailureCount > 0 || srvServiceStartFailureCount > 0 || srvStartFailureCount > 0) {
            appState = APP_START_FAILURE;
            logger.debug("应用状态：{}", appState);
            return appState.longValue();
        }
        //2.deploy删除失败 service删除失败
        if (srvDeployDeleteFailureCount > 0 || srvServiceDeleteFailureCount > 0 || srvStopFailureCount > 0) {
            appState = APP_STOP_FAILURE;
            logger.debug("应用状态：{}", appState);
            return appState.longValue();
        }
        //3.应用启动中 (至少一个启动中 +deploy创建成功 + service创建成功 +服务创建成功)
        if (srvStartingCount > 0 && (srvStartingCount + srvDeployStartSuccessCount + srvServiceStartSuccessCount + srvStartSuccessCount) == srvCount) {
            appState = APP_STARTING;
            logger.debug("应用状态：{}", appState);
            return appState.longValue();
        }
        //4.应用停止中状态(至少一个停止中 + service删除成功 +deploy删除成功 +服务删除成功)
        if (srvStopingCount > 0 && (srvStopingCount + srvDeployDeleteSuccessCount + srvServiceDeleteSuccessCount + srvStopSuccessCount == srvCount)) {
            appState = APP_STOPING;
            logger.debug("应用状态：{}", appState);
            return appState.longValue();
        }
        //5.应用创建成功
        if (srvStartSuccessCount == srvCount) {
            appState = APP_START_SUCCESS;
            logger.debug("应用状态：{}", appState);
            return appState.longValue();
        }
        //6.应用停止成功
        if (srvStopSuccessCount == srvCount) {
            appState = APP_STOP_SUCCESS;
            logger.debug("应用状态：{}", appState);
            return appState.longValue();
        }

        logger.debug("应用状态：{}", appState);
        return appState.longValue();

    }

    /**
     * 获取满足查询条件中状态的应用列表
     *
     * @param appExample
     * @param listAppSrvListVO
     * @return
     */
    private List<AppSrvListVO> listAppByAppState(AppExample appExample, List<AppSrvListVO> listAppSrvListVO) throws BusinessException{
        Long appState = appExample.getState();
        List<AppSrvListVO> appSrvListVOS = new ArrayList<AppSrvListVO>();
        for (AppSrvListVO appSrvListVO : listAppSrvListVO) {
            if (appSrvListVO.getAppState().equals(appState)) {
                appSrvListVOS.add(appSrvListVO);
            }
        }
        return appSrvListVOS;
    }

    /**
     * 根据服务启动时间获取应用启动时间
     *
     * @param d1
     * @param d2
     * @return
     */
    private Date getAppStartTime(Date d1, Date d2) throws BusinessException{
        Date date = null;
        if (null != d1 && null != d2) {
            if (d1.getTime() >= d2.getTime()) {
                date = d1;
            } else {
                date = d2;
            }
        } else if (null == d1 && null != d2) {
            date = d2;
        } else if (null != d1 && null == d2) {
            date = d1;
        }
        return date;
    }

    /**
     * 根据服务启动时间获取应用启动时间
     *
     * @param d1
     * @param d2
     * @return
     */
    private Date getAppStopTime(Date d1, Date d2) throws BusinessException{
        Date date = null;
        if (null != d1 && null != d2) {
            if (d1.getTime() >= d2.getTime()) {
                date = d1;
            } else {
                date = d2;
            }
        } else if (null == d1 && null != d2) {
            date = d2;
        } else if (null != d1 && null == d2) {
            date = d1;
        }
        return date;
    }

    /**
     * 获取应用运行时间
     *
     * @param startTIme
     * @param stopTime
     * @return
     */
    private String getAppRunningTime(Date startTIme, Date stopTime) throws BusinessException{
        String runningTime = null;
        if (null != startTIme && null != stopTime) {
            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;
            long ns = 1000;
            long day, hour, min, sec;
            long diff;
            if (stopTime.getTime() >= startTIme.getTime()) {
                // 获得两个时间的毫秒时间差异
                diff = stopTime.getTime() - startTIme.getTime();
                // 计算差多少天
                day = diff / nd;
                // 计算差多少小时
                hour = diff % nd / nh;
                // 计算差多少分钟
                min = diff % nd % nh / nm;
                // 计算差多少秒//输出结果
                sec = diff % nd % nh % nm / ns;
                runningTime = day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
            }
        }
        return runningTime;
    }

    /**
     * 创建应用
     *
     * @param userId
     * @param appDetail
     * @return
     */
    public Result createApp(String userId, AppDetail appDetail) throws BusinessException {
        //1.应用创建初始化
        logger.info("应用创建初始化");
        Result result = CodeStatusUtil.resultByCodeEn("APP_BUILD_FAILURE");
        //2.应用创建信息校验
        logger.info("应用创建信息校验");
        Result resultVerify = createAppVerify(appDetail);
        //3.校验失败
        if (resultVerify.getSuccess() == 0) {
            logger.info("应用创建信息校验失败");
            return resultVerify;
        } else {
            //3.应用创建信息校验成功
            logger.info("应用创建信息校验成功");
            //TODO: 创建者需要调用用户管理模块设置
            appDetail.setCreator("admin");
            DateUtil dateUtil = new DateUtil();
            Date currentTime = dateUtil.getCurrentTime();
            appDetail.setCreateTime(currentTime);
            appDetail.setUpdateTime(currentTime);
            int line = appDao.doInsertByBean(appDetail);
            if (line != 0) {
                logger.info("插入数据库成功，应用名称:{}", appDetail.getAppNameZh());
                result = CodeStatusUtil.resultByCodeEn("APP_BUILD_SUCCESS");
                result.setData(appDetail);
            } else {
                logger.error("插入数据库失败，应用名称:{}", appDetail.getAppNameZh());
            }
            logger.debug("message:" + result.getMessage());
            return result;
        }
    }


    /**
     * 应用创建信息验证
     *
     * @param appDetail
     * @return
     */
    public Result createAppVerify(AppDetail appDetail) throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn("APP_BUILD_SUCCESS");
        // 1.应用英文名是否有中文校验
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(appDetail.getAppNameEn());
        if (m.find()) {
            result=CodeStatusUtil.resultByCodeEn("APP_ENNAME_CH");
            return result;
        }
        //2.应用中文名唯一性校验
        AppDetail app = appDao.queryAppByName(appDetail.getAppNameZh());
        //3.应用英文名唯一性校验
        AppDetail appEn = appDao.queryAppByEnName(appDetail.getAppNameEn());

        if (app != null || appEn != null) {
            result=CodeStatusUtil.resultByCodeEn("APP_NAME_EXIST");
            return result;
        }
        return result;
    }

    /**
     * 应用修改信息验证
     *
     * @param appDetail
     * @return
     */
    public Result updateAppVerify(AppDetail appDetail) throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn("APP_MODIFY_SUCCESS");
        if (appDetail.getAppNameZh() != null && appDetail.getAppNameEn() != null) {
            // 1.应用英文名是否有中文校验
            String regEx = "[\\u4e00-\\u9fa5]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(appDetail.getAppNameEn());
            if (m.find()) {
                result=CodeStatusUtil.resultByCodeEn("APP_ENNAME_CH");
                return result;
            }
            //2.应用中文名唯一性校验
            AppDetail app = appDao.queryAppByName(appDetail.getAppNameZh());
            //3.应用英文名唯一性校验
            AppDetail appEn = appDao.queryAppByEnName(appDetail.getAppNameEn());

            if (app != null || appEn != null) {
                result=CodeStatusUtil.resultByCodeEn("APP_NAME_EXIST");
                return result;
            }
        }
        return result;
    }


    /**
     * 根据应用id删除应用
     *
     * @param userId
     * @param appId
     * @return
     */
    public Result deleteAppById(String userId, Integer appId) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("APP_DELETE_FAILURE");
        int line = 0;
        AppSrvListVO appSrvListVO = null;
        List<SrvDetailOrderVO> listSrvDetailOrder = null;
        /**
         * 1.查询appId的服务列表
         */
        appSrvListVO = (AppSrvListVO) this.findlistAppSrvListOrderByAppId(appId).getData();
        if (appSrvListVO == null){
            //listSrvDetailOrder = appSrvListVO.getSrvDetailOrderVOList();
            logger.debug("应用不存在或者已经删除，应用Id:{}", appId);
            result.setMessage("应用不存在或者已经删除");
            return result;
        }
        /**
         * 查找appState状态
         */
        Long appState = 0L;
        try {
            appState = (Long) getAppStatusByUserIdAppId(userId, appId).getData();
            //应用创建成功 即可立即删除(不存在服务列表)
            if (appState == CodeStatusUtil.getInstance().getStatusByCodeEn("APP_BUILD_SUCCESS").getCode().longValue() && listSrvDetailOrder == null) {
                line = appDao.doDeleteById(appId);
                if (line != 0) {
                    logger.info("删除创建成功应用，应用名Id：{}", appId);
                    result = CodeStatusUtil.resultByCodeEn("APP_DELETE_SUCCESS");
                } else {
                    logger.error("删除应用失败，应用名Id：{}", appId);
                }
                logger.debug("message:" + result.getMessage());
                return result;
            }
            if (appState != CodeStatusUtil.getInstance().getStatusByCodeEn("APP_STOP_SUCCESS").getCode().longValue()) {
                //改变应用状态  停止应用
                logger.info("停止应用,应用Id:{}", appId);
                result = changeApp(userId, appId, 2);
                appState = Long.parseLong(result.getCode());
            }
        } catch (Exception e) {
            logger.error("查找应用状态失败，应用Id:{},应用名称:{},错误信息:{}", appId, appSrvListVO.getAppNameZh(), e.getMessage());
            throw new BusinessException(result);
        }

        /**
         *
         * 2.遍历服务列表，
         *     2.1删除应用-服务关系表记录
         *     2.2删除服务
         *
         *    2.3 直接调用服务封装的方法 删除表记录 替换之前的方法
         */
        //2.1当应用停止成功状态
        listSrvDetailOrder=appSrvListVO.getSrvDetailOrderVOList();
        try {
            if (appState == CodeStatusUtil.getInstance().getStatusByCodeEn("APP_STOP_SUCCESS").getCode().longValue()) {
                //2.1.1 删除服务
                for (SrvDetailOrderVO srvDetailOrderVO : listSrvDetailOrder) {
                    try {
//                        srvMngService.doDeleteSrvDetailById(srvDetailOrderVO.getSrvId());
                    } catch (Exception e) {
                        logger.error("删除服务失败，服务Id:{},错误信息:{}", srvDetailOrderVO.getSrvId(), e.getMessage());
                        throw new BusinessException(CodeStatusUtil.resultByCodeEn("SERVICE_DELETE_FAILURE"));
                    }
                }
                /*
                 * 3.删除应用
                 */
                line = appDao.doDeleteById(appId);
            }
            else{
                Result result2 = CodeStatusUtil.resultByCodeEn("APP_DELETE_FAILURE");
                result2.setMessage("删除失败，请先停止应用！");
                throw new BusinessException(result2);
            }
        } catch (Exception e) {
            logger.error("应用状态码获取异常，应用Id:{},错误信息:{}", appId, e.getMessage());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("GET_CODE_ERROR"));
        }
        if (line != 0) {
            logger.info("删除应用成功，应用名Id：{}", appId);
            result = CodeStatusUtil.resultByCodeEn("APP_DELETE_SUCCESS");
        } else {
            logger.error("删除应用失败，应用名Id：{}", appId);
            result = CodeStatusUtil.resultByCodeEn("APP_DELETE_FAILURE");
        }
        logger.debug("message:" + result.getMessage());
        return result;
    }

    /**
     * 修改应用
     *
     * @param userId
     * @param appDetail
     * @return
     */
    public Result updateApp(String userId, AppDetail appDetail) throws BusinessException {
        //1.应用修改初始化
        logger.info("应用修改初始化");
        Result result = CodeStatusUtil.resultByCodeEn("APP_MODIFY_FAILURE");
        //2.应用修改信息校验
        logger.info("应用修改信息校验");
        Result resultVerify = updateAppVerify(appDetail);
        //3.应用修改信息校验失败
        if (resultVerify.getSuccess() == 0) {
            logger.info("应用修改信息校验失败");
            return resultVerify;
        } else {
            //4.应用修改信息校验成功
            logger.info("应用修改信息校验成功");
            DateUtil dateUtil = new DateUtil();
            Date currentTime = dateUtil.getCurrentTime();
            appDetail.setUpdateTime(currentTime);
            int line = appDao.doUpdateByBean(appDetail);
            if (line != 0) {
                result = CodeStatusUtil.resultByCodeEn("APP_MODIFY_SUCCESS");
            }
            logger.debug("message:" + result.getMessage());
            return result;
        }
    }

    /**
     * 启动停止应用
     *
     * @param userId
     * @param appId
     * @param flag
     * @return
     */
    @Override
    public Result changeApp(String userId, Integer appId, Integer flag) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("APP_START_FAILURE");
        //TODO: 调用启动服务
        //1.查找 获取此时应用状态
        if (getAppStatusByUserIdAppId(userId, appId).getData() != null) {
            Long appState = (Long) getAppStatusByUserIdAppId(userId, appId).getData();
            AppSrvListVO appSrvListVO = (AppSrvListVO) this.findlistAppSrvListOrderByAppId(appId).getData();
            //判断应用是否存在
            List<SrvDetailOrderVO> listSrvDetailOrder = appSrvListVO.getSrvDetailOrderVOList();
            String appname = appSrvListVO.getAppNameZh();
            Integer reuserId = Integer.parseInt(userId);
            Long reflag = new Long((long) flag);

            if (flag == 1) {
                //应用启动
                //2 非停止状态 即 停止成功 应用创建成功
                if (appState != CodeStatusUtil.getInstance().getStatusByCodeEn("APP_STOP_SUCCESS").getCode().longValue() && appState != CodeStatusUtil.getInstance().getStatusByCodeEn("APP_BUILD_SUCCESS").getCode().longValue()) {
                    result = CodeStatusUtil.resultByCodeEn("APP_START_FAILURE");
                    return result;
                } else {
                    //2.1启动应用
                    for (SrvDetailOrderVO srvDetailOrderVO : listSrvDetailOrder) {
                        Result srvResult = srvMngService.SrvStatusChanged(reuserId, appname, srvDetailOrderVO.getSrvNameCh(), reflag);
                        if (srvResult.getSuccess() == 0) {
                            result = CodeStatusUtil.resultByCodeEn("APP_START_FAILURE");
                            result.setData(srvResult.getData());
                            logger.debug("message:" + result.getMessage());
                            return result;
                        }
                    }
                }
            } else {
                //应用停止
                //3 停止状态
                if (appState == CodeStatusUtil.getInstance().getStatusByCodeEn("APP_STOP_SUCCESS").getCode().longValue()) {
                    result = CodeStatusUtil.resultByCodeEn("APP_STOP_SUCCESS");
                    return result;
                } else {
                    //停止应用
                    Collections.reverse(listSrvDetailOrder);
                    for (SrvDetailOrderVO srvDetailOrderVO : listSrvDetailOrder) {
                        Result srvResult = srvMngService.SrvStatusChanged(reuserId, appname, srvDetailOrderVO.getSrvNameCh(), reflag);
                        if (srvResult.getSuccess() == 0) {
                            result = CodeStatusUtil.resultByCodeEn("APP_STOP_FAILURE");
                            result.setData(srvResult.getData());
                            logger.debug("message:" + result.getMessage());
                            return result;
                        }
                    }
                }

            }


            //启动成功
            if (flag == 1) {
                result = CodeStatusUtil.resultByCodeEn("APP_START_SUCCESS");
                logger.info("返回操作code：{}", result.getCode());
            } else {
                result = CodeStatusUtil.resultByCodeEn("APP_STOP_SUCCESS");
                logger.info("返回操作code：{}", result.getCode());
            }
        }
        //判断应用是否存在
        String message=getAppStatusByUserIdAppId(userId, appId).getMessage();
        if(message.equals("应用不存在")) {
            result.setMessage(getAppStatusByUserIdAppId(userId, appId).getMessage());
        }
        logger.debug("message:" + result.getMessage());
        return result;
    }

    /**
     * 定时刷新应用状态
     *
     * @param userId
     * @param appIds
     * @return
     */
    @Override
    public Result refreshState(String userId, Integer[] appIds) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("APP_QUERY_FAILURE");
        List<AppSrvListVO> appSrvListVOS = appDao.findAppSrvListVOByIds(appIds);
        //遍历应用列表
        for (AppSrvListVO appSrvListVO : appSrvListVOS) {
            Integer appId = appSrvListVO.getAppId();
            AppExample appExample = new AppExample();
            appExample.setAppId(appId);
            //根据应用ID查询应用下的服务状态列表
            List<SrvDetailOrderVO> srvDetailOrderVOList = srvOperationDAO.listByExample(appExample);
            //根据服务状态列表设置应用及服务
            setAppSrvState(appSrvListVO, srvDetailOrderVOList);
        }
        if (appSrvListVOS != null) {
            result = CodeStatusUtil.resultByCodeEn("APP_QUERY_SUCCESS");
            result.setData(appSrvListVOS);
        }
        return result;
    }

    /**
     * 获取单个应用状态
     * * @param userId
     *
     * @param appId
     */
    public Result getAppStatusByUserIdAppId(String userId, Integer appId) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("APP_QUERY_FAILURE");
        /**
         * appId得到应用服务列表
         */
        AppSrvListVO appSrvListVO = (AppSrvListVO) findlistAppSrvListOrderByAppId(appId).getData();
        //当服务列表存在服务时，进行判断状态
        if (appSrvListVO != null) {
            if (appSrvListVO.getSrvDetailOrderVOList() != null && appSrvListVO.getSrvDetailOrderVOList().size() > 0) {
                AppExample appExample = new AppExample();
                appExample.setAppId(appId);
                //根据应用ID查询应用下的服务状态列表
                List<SrvDetailOrderVO> srvDetailOrderVOList = srvOperationDAO.listByExample(appExample);
                appSrvListVO.setAppState(getAppState(srvDetailOrderVOList));
            } else {
                // 设置应用状态为 应用创建成功
                appSrvListVO.setAppState(CodeStatusUtil.getInstance().getStatusByCodeEn("APP_BUILD_SUCCESS").getCode().longValue());
            }

            result = CodeStatusUtil.resultByCodeEn("APP_QUERY_SUCCESS");
            result.setData(appSrvListVO.getAppState());
        }
        else{
            result=CodeStatusUtil.resultByCodeEn("APP_NOT_FOUND");
        }
        return result;
    }

    /**
     * 所有用户的所有应用状态
     *
     * @param userId
     */
    public Result getAppStatus(String userId) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("APP_QUERY_FAILURE");
        List<AppDetail> appDetailList = appDao.listApps();
        Map<Integer, Byte> map = new LinkedHashMap();
        //遍历列表
        for (AppDetail appDetail : appDetailList) {
            Byte state = (Byte) this.getAppStatusByUserIdAppId(userId, appDetail.getAppId()).getData();
            map.put(appDetail.getAppId(), state);
        }
        if (map != null) {
            result = CodeStatusUtil.resultByCodeEn("APP_QUERY_SUCCESS");
            result.setData(map);
        }
        return result;
    }

    /**
     * 检查应用中文名是否重复
     *
     * @param appNameZh
     * @return Result
     */
    @Override
    public Result checkNameZh(String appNameZh) throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn("APP_CH_NAME_USABLE");
        //将中文名中的\"替换
        if (appNameZh.contains("\"")) {
            appNameZh = appNameZh.replace("\"", "");
        }
        int resultCount = appDao.doFindByNameZh(appNameZh);
        if (resultCount >= 1) {
            result=CodeStatusUtil.resultByCodeEn("APP_CH_NAME_EXIST");
        }
        return result;
    }

    /**
     * 检查应用英文名是否重复
     *
     * @param appNameEn
     * @return Result
     */
    @Override
    public Result checkNameEn(String appNameEn) throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn("APP_EN_NAME_USABLE");
        if (appNameEn.contains("\"")) {
            appNameEn = appNameEn.replace("\"", "");
        }
        int resultCount = appDao.doFindByNameEn(appNameEn);
        if (resultCount >= 1) {
            result=CodeStatusUtil.resultByCodeEn("APP_EN_NAME_EXIST");
        }
        return result;
    }

}
