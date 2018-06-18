package com.cloud.paas.appservice.service.app;

import com.cloud.paas.appservice.model.AppDetail;
import com.cloud.paas.appservice.qo.AppExample;
import com.cloud.paas.appservice.service.BaseService;
import com.cloud.paas.appservice.vo.app.AppDetailVO;
import com.cloud.paas.appservice.vo.app.AppSrvListVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.result.Result;

import java.util.List;

/**
 * @Author: wangzhifeng
 * @Description:
 * @Date: Create in 10:49 2017/10/19
 * @Modified by:
 */
public interface AppMngService extends BaseService<AppDetail> {

    /**
     * 获取所有用户下所有应用列表
     * @return
     */
	Result listApps() throws BusinessException;

    /**
     * 条件查询应用列表
     * @param appExample 查询条件
     * @return
     */
	Result listByCondition(AppExample appExample) throws BusinessException;
    /**
     * 根据应用id查询应用详情
     * @param userId
     * @param appId
     * @return
     */
	Result getAppById(String userId, Integer appId) throws BusinessException;
	/**
	 * @Brief: 根据应用名称查询应用信息
	 * @Param:
	 * @Return:
	 */
	AppDetail queryAppByName(String appName);
	AppDetail queryAppByEnName(String appName);

	/**
	 * @Brief: 根据应用ID查询应用信息 四表
	 * @Param:
	 * @Return:
	 */
	List<AppDetailVO> listAppSrv(int appId);
	/**
	 * @Brief: 根据应用条件查询应用信息 四表
	 * @Param:
	 * @Return:
	 */
	List<AppDetailVO> listAppByCondition(AppExample appExample);
	/**
	 * @param appId
	 * @return AppSrvListVO 应用+服务列表 两张表
	 */
    List<AppSrvListVO> listAppSrvList(int appId);
	/**
	 * @param appId
	 * @return AppSrvListOrderVOSupport 应用+服务顺序列表 两张表
	 */
	Result findlistAppSrvListOrderByAppId(int appId) throws BusinessException;
	/**
	 * @param appExample
	 * @return 返回符合条件的AppSrvListVO 二张表 应用+服务列表
	 */
	 Result listAppSrvListByCondition(AppExample appExample) throws BusinessException;
	/**
	 * @param listAppSrvListVO
	 * @return 返回符合条件的AppSrvListVOSupport 二张表 应用+服务列表处理
	 */
	Result listAppSrvListByCondition(List<AppSrvListVO> listAppSrvListVO, AppExample appExample) throws BusinessException;

    /**
     * 创建应用
     * @param userId
     * @param appDetail
     * @return
     */
	 Result createApp(String userId, AppDetail appDetail) throws BusinessException;

    /**
     * 根据应用id删除应用
     * @param userId
	 * @param appId
     * @return
     */
	 Result deleteAppById(String userId, Integer appId) throws BusinessException;

    /**
     * 修改应用
     * @param userId
     * @param appDetail
     * @return
     */
    Result updateApp(String userId, AppDetail appDetail) throws BusinessException;

    /**
     * 启动停止应用
     * @param userId
     * @param appId
     * @param flag
     * @return
     */
    Result changeApp(String userId, Integer appId, Integer flag)  throws BusinessException;

    /**
     * 定时刷新应用状态
     * @param userId
     * @param appIds
     * @return
     */
    Result refreshState(String userId, Integer[] appIds) throws BusinessException;

	/**
	 *获取单个应用状态
	 * * @param userId
	 * @param appId
	 */
	Result getAppStatusByUserIdAppId(String userId, Integer appId) throws BusinessException;

	/**
	 * 所有用户的所有应用状态
	 * @param userId
	 */
	Result getAppStatus(String userId) throws BusinessException;
	/**
	 * 分页
	 */
	Result listAppByConditionResult(String userid,AppExample appExample) throws BusinessException;

	Result checkNameZh( String appNameZh );

	Result checkNameEn( String appNameZh );
}
