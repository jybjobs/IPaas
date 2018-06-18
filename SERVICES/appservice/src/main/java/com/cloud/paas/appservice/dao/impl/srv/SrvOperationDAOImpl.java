package com.cloud.paas.appservice.dao.impl.srv;

import com.cloud.paas.appservice.dao.SrvOperationDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;
import com.cloud.paas.appservice.model.SrvOperation;
import com.cloud.paas.appservice.qo.AppExample;
import com.cloud.paas.appservice.qo.SrvCondition;
import com.cloud.paas.appservice.vo.srv.SrvDetailOrderVO;
import com.cloud.paas.exception.BusinessException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: xujia
 * @Description:
 * @Date: Create in 17:46 2017/11/2
 * @Modified by:kaiwen
 */
@Repository
public class SrvOperationDAOImpl extends BaseDAOImpl<SrvOperation> implements SrvOperationDAO {

	/**
	 * 服务操作的mapper.xml的namespace
	 */
	public static final String NAME_SPACE = "SrvOperationDAO";

	@Override
	public  String getNameSpace(){
		return this.NAME_SPACE;
	}

    /**
     * 根据查询条件查询服务状态、启动时间、停止时间
     * @param appExample
     * @return
     */
	public List<SrvDetailOrderVO> listByExample(AppExample appExample) {
		return sqlSessionTemplate.selectList(this.getNameSpace()+".listByExample",appExample);
	}

    /**
     * 根据一组应用id查询服务状态、启动时间、停止时间
     * @param appIds
     * @return
     */
    @Override
    public List<SrvDetailOrderVO> listByIds(Integer[] appIds) {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".listByIds",appIds);
    }

    /**
	 * 改变操作信息表的状态
	 * @param srvCondition
	 * @return
	 */
	public Integer doSetState(SrvCondition srvCondition){
		return sqlSessionTemplate.update(this.getNameSpace()+".doSetState",srvCondition);
	}

	/**
	 * 根据srvId查询服务最新的状态
	 * @param srvId
	 * @return
	 */
	public long getSrvOperationState(Integer srvId)throws BusinessException {
		return sqlSessionTemplate.selectOne(this.getNameSpace()+".getSrvOperationState",srvId);
	}

	/**
	 * 通过一组id 得到相应的对象
	 * @param list
	 * @return
	 */
	public List<SrvOperation> getGroupOfSrvMng(List<Integer> list) throws BusinessException{
		return sqlSessionTemplate.selectList(this.getNameSpace ()+".getGroupOfSrvMng",list);
	}

	/**
	 * 根据状态码查询最新的服务id集合
	 * @param state
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Integer> getSrvIdByCodeStatus(Integer state) throws Exception {
		return sqlSessionTemplate.selectList(this.getNameSpace ()+".getSrvIdByCodeStatus",state);
	}
}
