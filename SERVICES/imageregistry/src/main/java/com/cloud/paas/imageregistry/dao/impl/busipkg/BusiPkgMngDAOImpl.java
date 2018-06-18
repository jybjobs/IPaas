package com.cloud.paas.imageregistry.dao.impl.busipkg;

import com.cloud.paas.imageregistry.dao.BusiPkgMngDAO;
import com.cloud.paas.imageregistry.dao.impl.BaseDAOImpl;
import com.cloud.paas.imageregistry.model.BusiPkgDetail;
import com.cloud.paas.imageregistry.qo.BusiPkgExample;
import com.cloud.paas.imageregistry.vo.busipkg.BusiPkgListVO;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @Author: wangzhifeng
 * @Description: 业务包详细信息DAO
 * @Date: Create in 11:29 2017/11/2
 * @Modified by: xujia
 */
@Repository("busiPkgMngDAO")
public class BusiPkgMngDAOImpl extends BaseDAOImpl<BusiPkgDetail> implements BusiPkgMngDAO {

	public static final String NAME_SPACE = "BusiPkgMngDAO";

	@Override
	public  String getNameSpace(){
		return BusiPkgMngDAOImpl.NAME_SPACE;
	}

	/**
	 * 根据业务包名称查询业务包详细信息
	 * @param busipkgName 业务包名称
	 * @return 业务包详细信息
	 */
	@Override
	public BusiPkgDetail getBusipkgByName(String busipkgName) {
		//TODO 根据业务包名称查询业务包详细信息
		return null;
	}

	/**
	 * 查询所有业务包详细信息
	 * @return 业务包详细信息列表
	 */
	@Override
	public List<BusiPkgDetail> listBusipkg() {
		return sqlSessionTemplate.selectList(this.getNameSpace()+".doSearchList");
	}
	/**
	 * id查询所有业务包详细信息拼接
	 * @return 业务包详细信息拼接列表
	 */
	@Override
	public List<BusiPkgListVO> selectVersionVOList(int busiPkgId){
		return sqlSessionTemplate.selectList(this.getNameSpace()+".selectVersionVOList",busiPkgId);
	}
	/**
	 * 条件查询所有业务包详细信息拼接
	 * @return 业务包详细信息拼接列表
	 */
	@Override
	public List<BusiPkgListVO> selectVersionVOListByExample(BusiPkgExample busiPkgExample){
		return sqlSessionTemplate.selectList(this.getNameSpace()+".selectVersionVOListByExample",busiPkgExample);
	}

	/**
	 * 分页
	 */
	public List<BusiPkgDetail> findBusiPkgIdListByExample(BusiPkgExample busiPkgExample){
		return sqlSessionTemplate.selectList(this.getNameSpace()+".findBusiPkgIdListByExample",busiPkgExample);
	}
	/**
	 * 分页查询
	 */
//	public List<BusiPkgListVO>  findBusiPkgIdListByExampleResult(HashMap<String,Object> map){
//		return sqlSessionTemplate.selectList(this.getNameSpace()+".findBusiPkgIdListByExampleResult",map);
//
//	}
	public List<BusiPkgListVO>  findBusiPkgIdListByExampleResult(BusiPkgExample busiPkgExample){
		return sqlSessionTemplate.selectList(this.getNameSpace()+".findBusiPkgIdListByExampleResult",busiPkgExample);
	}

	//根据中文名查找
    @Override
    public int doFindByNameZh(String busiPkgNameZh) {
		System.out.println( "busiPkgNameZh=="+busiPkgNameZh );
		return sqlSessionTemplate.selectOne(this.getNameSpace()+".doFindByNameZh",busiPkgNameZh);
    }

	//根据英文名查找
	@Override
	public int doFindByNameEn(String busiPkgNameEn) {
		return sqlSessionTemplate.selectOne(this.getNameSpace()+".doFindByNameEn",busiPkgNameEn);
	}

}
