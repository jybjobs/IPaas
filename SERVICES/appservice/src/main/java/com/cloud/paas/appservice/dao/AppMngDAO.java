package com.cloud.paas.appservice.dao;

import com.cloud.paas.appservice.model.AppDetail;
import com.cloud.paas.appservice.qo.AppExample;
import com.cloud.paas.appservice.vo.app.AppDetailVO;
import com.cloud.paas.appservice.vo.app.AppSrvListVO;

import java.util.List;

/**
 * @Author: wangzhifeng
 * @Description:
 * @Date: Create in 11:30 2017/11/2
 * @Modified by:
 */
public interface AppMngDAO  extends BaseDAO<AppDetail> {

	AppDetail queryAppByName(String appName);
	AppDetail queryAppByEnName(String appName);

    /**
     * 获取所有用户下所有应用列表
     * @return
     */
	List<AppDetail> listApps();

    /**
     * 条件查询应用列表
     * @param appExample 查询条件
     * @return
     */
	List<AppDetail> listByCondition(AppExample appExample);
	/**
	 * @param appId
	 * @return 返回的AppDetailVO 应用+服务列表 四张表
	 */
	List<AppDetailVO> listAppSrv(int appId);

	/**
	 * @param appId
	 * @return AppSrvListVO 应用+服务列表 两张表
	 */
	List<AppSrvListVO> listAppSrvList(int appId);

	/**
	 * @param appId
	 * @return AppSrvListOrderVO 应用+服务顺序列表 对象 两张表
	 */
	AppSrvListVO findlistAppSrvListOrderByAppId(int appId);

	/**
	 * @param appExample
	 * @return 返回符合条件的AppDetailVO 四张表 应用+服务列表
	 */
	List<AppDetailVO> listAppByCondition(AppExample appExample);
	/**
	 * @param appExample
	 * @return 返回符合条件的AppSrvListVO 二张表 应用+服务列表
	 */
	List<AppSrvListVO> listAppSrvListByCondition(AppExample appExample);

	/**
	 * 根据一个或多个应用Id查询
	 * @param appIds
	 * @return
	 */
	List<AppSrvListVO> findAppSrvListVOByIds(Integer[] appIds);
	/**
	 * 查询应用详情
	 * @param srvId
	 * @return SrvDetailVO
	 */
	AppDetail doFindBySrvId(int srvId);
	/**
	 *
	 */
	List<AppDetail> findAppIdList(AppExample appExample);

	int doFindByNameZh( String appNameZh);

	int doFindByNameEn( String appNameEn);
}
