package com.cloud.paas.imageregistry.dao;

import com.cloud.paas.imageregistry.model.BusiPkgDetail;
import com.cloud.paas.imageregistry.qo.BusiPkgExample;
import com.cloud.paas.imageregistry.vo.busipkg.BusiPkgListVO;

import java.util.List;

/**
 * @Author: wangzhifeng
 * @Description: 业务包详细信息DAO接口
 * @Date: Create in 11:30 2017/11/2
 * @Modified by: xujia
 */

public interface BusiPkgMngDAO extends BaseDAO<BusiPkgDetail> {

	BusiPkgDetail getBusipkgByName(String busipkgName);

	List<BusiPkgDetail> listBusipkg();
	/**busiPKgId条件业务包详情查询
	 * params:busiPkgId
	 * return 	List<BusiPkgListVO>
	 */
	List<BusiPkgListVO> selectVersionVOList(int busiPkgId);
	/**条件业务包详情查询
	 * params:busiPkgExample
	 * return 	List<BusiPkgListVO>
	 */
	List<BusiPkgListVO> selectVersionVOListByExample(BusiPkgExample busiPkgExample);
	/**
	 * 分页
	 */
	List<BusiPkgDetail> findBusiPkgIdListByExample(BusiPkgExample busiPkgExample);
	/**
	 * 分页查询
	 */
	//List<BusiPkgListVO>  findBusiPkgIdListByExampleResult(HashMap<String,Object> map);
	 List<BusiPkgListVO>  findBusiPkgIdListByExampleResult(BusiPkgExample busiPkgExample);

	/**
	 * 检查业务包中文名是否重复
	 * @param busiPkgNameZh
	 * @return
	 */
    int doFindByNameZh(String busiPkgNameZh);

	/**
	 * 检查业务包英文名是否重复
	 * @param busiPkgNameEn
	 * @return
	 */
	int doFindByNameEn(String busiPkgNameEn);
}
