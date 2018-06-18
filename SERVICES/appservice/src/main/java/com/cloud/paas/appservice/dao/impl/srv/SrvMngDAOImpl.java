package com.cloud.paas.appservice.dao.impl.srv;

import com.cloud.paas.appservice.dao.SrvMngDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;

import com.cloud.paas.appservice.model.SrvDetail;
import com.cloud.paas.appservice.qo.SrvCondition;
import com.cloud.paas.appservice.qo.SrvExample;
import com.cloud.paas.appservice.vo.app.AppDetailVO;
import com.cloud.paas.appservice.vo.srv.SrvDetailOrderVO;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: wangzhifeng
 * @Description:
 * @Date: Create in 17:46 2017/11/2
 * @Modified by:kaiwen
 */
@Repository
public class SrvMngDAOImpl extends BaseDAOImpl<SrvDetail> implements SrvMngDAO {

	/**
	 * 服务的mapper.xml的namespace
	 */
	public static final String NAME_SPACE = "com.cloud.paas.appservice.dao.SrvDetailDAO";

	@Override
	public  String getNameSpace(){
		return this.NAME_SPACE;
	}
	public List<SrvDetail> doQueryAllSrv() {
		return sqlSessionTemplate.selectList(this.getNameSpace()+".doQueryAllSrv");
	}

	/**
	 * 查询所有用户下的所有应用的所有服务
	 * @return
	 */
	public List<AppDetailVO> listUserAppSrvDetail(){
		return sqlSessionTemplate.selectList(this.getNameSpace()+".listUserAppSrvDetail");
	}


	/**
	 * 查询指定用户下的指定应用所有服务
	 * @param srvCondition
	 * @return
	 */
	public AppDetailVO listUserGivenAppGivenSrvDetail(SrvCondition srvCondition){
		return sqlSessionTemplate.selectOne(this.getNameSpace()+".listUserGivenAppGivenSrvDetail",srvCondition);
	}

	/**
	 * 查询指定用户下的指定应用指定服务
	 * @param srvCondition
	 * @return
	 */
	public AppDetailVO userGivenAppGivenSrvDetailGiven(SrvCondition srvCondition){
		return sqlSessionTemplate.selectOne(this.getNameSpace()+".userGivenAppGivenSrvDetailGiven",srvCondition);
	}

	/**
	 * 改变服务的状态：1：开启，2：停止，3：扩容，4：缩容
	 */
	public Integer SrvStatusChanged(SrvCondition srvCondition){
		return sqlSessionTemplate.selectOne(this.getNameSpace()+".SrvStatusChanged",srvCondition);
	}

    @Override
    public List<SrvDetailOrderVO> listAllSrvByConditions(SrvExample srvExample) {
        return sqlSessionTemplate.selectList (this.getNameSpace ()+".findSrvDetailVO",srvExample);
    }

    @Override
    public List<SrvDetail> findPageByIdList(SrvExample srvExample) {
        return sqlSessionTemplate.selectList (this.getNameSpace ()+".findSrvDetailPage",srvExample);
    }

	@Override
	public SrvDetail findNameEn(String nameEn) {
		return sqlSessionTemplate.selectOne (this.getNameSpace ()+".findNameEn",nameEn);
	}

	@Override
	public SrvDetail findNameZh(String nameZh) {
		return sqlSessionTemplate.selectOne (this.getNameSpace ()+".findNameZh",nameZh);
	}

    @Override
    public List<SrvDetail> findAll(int srvImageVersionId) {
        return sqlSessionTemplate.selectList (this.getNameSpace ()+".doFindAll",srvImageVersionId);
    }

	@Override
	public SrvDetail findNodePort(int nodePort) {
		return sqlSessionTemplate.selectOne (this.getNameSpace ()+".findNodePort",nodePort);
	}

	@Override
	public List<SrvDetail> listUserSrvDetail(SrvExample srvExample) {
		return sqlSessionTemplate.selectList(this.getNameSpace()+".listUserSrvDetail",srvExample);
	}

}
