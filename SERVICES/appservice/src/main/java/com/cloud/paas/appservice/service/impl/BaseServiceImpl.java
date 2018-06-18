package com.cloud.paas.appservice.service.impl;

import com.cloud.paas.appservice.dao.BaseDAO;
import com.cloud.paas.appservice.model.ValueObject;
import com.cloud.paas.appservice.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @Author: wangzhifeng
 * @Description:
 * @Date: Create in 17:28 2017/11/2
 * @Modified by:
 */
public abstract class BaseServiceImpl<T extends ValueObject>  implements BaseService<T>{

	public abstract BaseDAO<T> getBaseDAO();

	public int doInsertByBean(T bean){
		return getBaseDAO().doInsertByBean(bean);
	}
	public int doBatchInsertByList(List<T> list){
		return getBaseDAO().doBatchInsertByList(list);
	}

	public int doInsertByMap(Map<String,Object> param){
		return getBaseDAO().doInsertByMap(param);
	}

	public int doDeleteById(int id){
		return getBaseDAO().doDeleteById(id);
	}
	public int doDeleteById(T bean){
		return getBaseDAO().doDeleteById(bean);
	}

	public int doDeleteByMap(Map<String,Object> param){
		return getBaseDAO().doDeleteByMap(param);
	}

	public int doUpdateByBean(T bean){
		return getBaseDAO().doUpdateByBean(bean);
	}

	public int doBatchUpdateByList(List<T> list){
		return getBaseDAO().doBatchUpdateByList(list);
	}

	public int doUpdateByMap(Map<String,Object> param){
		return getBaseDAO().doUpdateByMap(param);
	}

	public T doFindById(int id){
		return getBaseDAO().doFindById(id);
	}

	public T doFindByBean(T bean){
		return getBaseDAO().doFindByBean(bean);
	}

	public List<T> doSearchListByBean(T bean){
		return getBaseDAO().doSearchListByBean( bean);
	}

	public List<T> doSearchListByMap(Map<String, Object> map){
		return getBaseDAO().doSearchListByMap( map);
	}

//	@Override
//	public PageInfo<T> doSearchPage(T vo) throws Exception {
//		String orderStr = "";
//		if(!"".equals(vo.getOrderName().trim())){
//			orderStr = vo.getOrderName() +" " +vo.getOrderRule();
//		}
//		return this.doSearchPage(vo, orderStr, vo.getPageIndex(), vo.getPageSize());
//	}
//
//	@Override
//	public PageInfo<T> doSearchPage(T vo,int pageNum, int pageSize) throws Exception {
//		String orderStr = "";
//		if(!"".equals(vo.getOrderName().trim())){
//			orderStr = vo.getOrderName() +" " +vo.getOrderRule();
//		}
//		return this.doSearchPage(vo, orderStr, pageNum, pageSize);
//	}
//
//
//	@Override
//	public PageInfo<T> doSearchPage(T vo,String orderStr,int pageNum, int pageSize) throws Exception {
//		if(!"".equals(orderStr.trim())){
//			PageHelper.startPage(pageNum, pageSize,orderStr);
//		}else{
//			PageHelper.startPage(pageNum, pageSize);
//		}
//		List<T> list = this.getBaseDAO().doSearchListByBean(vo);
//		PageInfo<T> page = new PageInfo<T>(list);
//		return page;
//	}
//
//
//	@Override
//	public PageInfo<T> doSearchPage(Map<String, Object> map,int pageNum, int pageSize) throws Exception {
//		return this.doSearchPage(map, "", pageNum, pageSize);
//	}
//
//	@Override
//	public PageInfo<T> doSearchPage(Map<String, Object> map,String orderStr,int pageNum, int pageSize) throws Exception {
//		if(!"".equals(orderStr.trim())){
//			PageHelper.startPage(pageNum, pageSize,orderStr);
//		}else{
//			PageHelper.startPage(pageNum, pageSize);
//		}
//		List<T> list = this.getBaseDAO().doSearchListByMap(map);
//		PageInfo<T> page = new PageInfo<T>(list);
//		return page;
//	}

}
