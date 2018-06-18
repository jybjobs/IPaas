package com.cloud.paas.imageregistry.dao.impl.busipkg;

import com.cloud.paas.imageregistry.dao.BusiPkgVersionMngDAO;
import com.cloud.paas.imageregistry.dao.impl.BaseDAOImpl;
import com.cloud.paas.imageregistry.model.BusiPkgVersionDetail;
import com.cloud.paas.imageregistry.qo.BusiPkgExample;
import com.cloud.paas.imageregistry.vo.busipkg.BusiPkgVersionDetailVO;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @Author: css
 * @Description:业务版本接口实现类
 * @Date: Create in 10:30 2017/11/22
 * @Modified by:
 */
@Repository
public class BusiPkgVersionMngDAOImpl extends BaseDAOImpl<BusiPkgVersionDetail> implements BusiPkgVersionMngDAO {
	/**
	 * 当前类的详细包名和类名的常量
	 */
	public static final String NAME_SPACE = "BusiPkgVersionMngDAO";

	/**
	 *
	 * @return
	 */
	@Override
	public  String getNameSpace(){
		return BusiPkgVersionMngDAOImpl.NAME_SPACE;
	}

    @Override
    public BusiPkgVersionDetail selectByPrimaryKey(int id) {
		return this.sqlSessionTemplate.selectOne(this.getNameSpace()+".selectByPrimaryKey",id);
    }

    /**获取指定业务包下的版本列表
	 * @param  busiPkgId 业务包Id
	 * @return List<BusiPkgVersionDetail>接收BusiPkgVersionDetailVO子类的版本列表
	 */
	public List<BusiPkgVersionDetail> listFindByPkgId(Integer busiPkgId){
		return this.sqlSessionTemplate.selectList(this.getNameSpace()+".listFindByPkgId",busiPkgId);
	}


	/**获取条件搜索的版本列表 版本+基本信息结构
	 * @param  busiPkgExample 条件
	 * @return List<BusiPkgVersionDetailVO>业务包查询条件搜索信息
	 */
	public List<BusiPkgVersionDetailVO> listFindByExample(BusiPkgExample busiPkgExample){
		return this.sqlSessionTemplate.selectList(this.getNameSpace()+".listFindByExample",busiPkgExample);
	}

	/**
	 * 获得指定业务包下版本数量
	 * @param busiPkgId
	 * @return
	 */
    @Override
    public int countVersion(int busiPkgId) {
		return this.sqlSessionTemplate.selectOne(this.getNameSpace()+".countVersion",busiPkgId);
    }

	/**
	 * 获得指定业务包下版本总大小
	 * @param busiPkgId
	 * @return
	 */
    @Override
    public long getBusiPkgSize(int busiPkgId) {
		return this.sqlSessionTemplate.selectOne(this.getNameSpace()+".findBusiPkgSize",busiPkgId);
    }

	@Override
	public int countByVersion(BusiPkgExample busiPkgExample) {
		return this.sqlSessionTemplate.selectOne(this.getNameSpace()+".doCountByVersion",busiPkgExample);
	}



	@Override
	public int doDeleteByPkgId(int busiPkgId) {
		return this.sqlSessionTemplate.delete(this.getNameSpace()+".doDeleteByPkgId",busiPkgId);
	}

	@Override
	public List<BusiPkgVersionDetail> getStatusByPkgIds(List<Long> pkgIds) {
		return this.sqlSessionTemplate.selectList(this.getNameSpace()+".getStatusByPkgIds",pkgIds);
	}


}
