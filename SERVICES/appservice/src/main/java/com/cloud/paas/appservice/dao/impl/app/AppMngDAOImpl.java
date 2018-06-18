package com.cloud.paas.appservice.dao.impl.app;

import com.cloud.paas.appservice.dao.AppMngDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;
import com.cloud.paas.appservice.model.AppDetail;
import com.cloud.paas.appservice.qo.AppExample;
import com.cloud.paas.appservice.vo.app.AppDetailVO;
import com.cloud.paas.appservice.vo.app.AppSrvListVO;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @Author: wangzhifeng
 * @Description:
 * @Date: Create in 11:29 2017/11/2
 * @Modified by:
 */
@Repository("appMngDAO")
public class AppMngDAOImpl extends BaseDAOImpl<AppDetail> implements AppMngDAO{

	public static final String NAME_SPACE = "AppMngDAO";

	@Override
	public  String getNameSpace(){
		return AppMngDAOImpl.NAME_SPACE;
	}
	public AppDetail queryAppByName(String appName){
		return sqlSessionTemplate.selectOne(this.getNameSpace()+".doFindByZhName",appName);
	}
	public AppDetail queryAppByEnName(String appName){
		return sqlSessionTemplate.selectOne(this.getNameSpace()+".doFindByEnName",appName);
	}

	/**
	 * 获取所有用户下所有应用列表
	 * @return
	 */
	public List<AppDetail> listApps() {
		return sqlSessionTemplate.selectList(this.getNameSpace()+".listApps");
	}

    /**
     * 条件查询应用列表
     * @param appExample 查询条件
     * @return
     */
    public List<AppDetail> listByCondition(AppExample appExample) {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".listByCondition",appExample);
    }

    /**
	 * @param appId
	 * @return AppDetailVO 应用+服务列表 四张表
	 */
	public List<AppDetailVO> listAppSrv(int appId){
		return sqlSessionTemplate.selectList(this.getNameSpace()+".findAppDetailVOByAppId",appId);
	}
	/**
	 * @param appId
	 * @return AppSrvListVO 应用+服务列表 两张表
	 */
	public List<AppSrvListVO> listAppSrvList(int appId){
		return sqlSessionTemplate.selectList(this.getNameSpace()+".findAppSrvListVOByAppId",appId);
	}

	/**
	 * @param appId
	 * @return AppSrvListOrderVO 应用+服务顺序列表 两张表
	 */
	public AppSrvListVO findlistAppSrvListOrderByAppId(int appId){
		return sqlSessionTemplate.selectOne(this.getNameSpace()+".findAppSrvListOrderVOByAppId",appId);
	}
	/**
	 * @param appExample
	 * @return 返回符合条件的AppDetailVO 四张表 应用+服务列表
	 */
	public 	List<AppDetailVO> listAppByCondition(AppExample appExample){
		return sqlSessionTemplate.selectList(this.getNameSpace()+".findAppDetailVOByAppExample",appExample);
	}
	/**
	 * @param appExample
	 * @return 返回符合条件的AppSrvListVO 二张表 应用+服务列表
	 */
	public List<AppSrvListVO> listAppSrvListByCondition(AppExample appExample){
		return sqlSessionTemplate.selectList(this.getNameSpace()+".findAppSrvListVOByAppExample",appExample);
	}

	/**
	 * 根据一个或多个应用Id查询
	 * @param appIds
	 * @return
	 */
	@Override
	public List<AppSrvListVO> findAppSrvListVOByIds(Integer[] appIds) {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".findAppSrvListVOByIds",appIds);
	}
	@Override
	public AppDetail doFindBySrvId(int srvId) {
		return sqlSessionTemplate.selectOne (this.getNameSpace ()+".doFindBySrvId" ,srvId);
	}

	/**
	 * 分页IdList查询
	 */
	public List<AppDetail> findAppIdList(AppExample appExample){
		return sqlSessionTemplate.selectList(this.getNameSpace()+".findAppIdList",appExample);
	}

	/**
	 * 应用name查询
	 */
	public int doFindByNameZh( String appNameZh){
		return sqlSessionTemplate.selectOne( this.getNameSpace()+".doFindByNameZh", appNameZh );
	}

	public int doFindByNameEn( String appNameEn){
		return sqlSessionTemplate.selectOne( this.getNameSpace()+".doFindByNameEn", appNameEn );
	}

}
