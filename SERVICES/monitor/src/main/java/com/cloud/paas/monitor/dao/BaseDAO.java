package com.cloud.paas.monitor.dao;


import com.cloud.paas.monitor.model.ValueObject;

import java.util.List;
import java.util.Map;

/**
 * @Author: wangzhifeng
 * @Description:
 * @Date: Create in 11:24 2017/11/2
 * @Modified by:
 */
public interface BaseDAO<T extends ValueObject> {
	public List<T> doSearchListByBean(T bean);

	public int doInsertByBean(T bean);

	public int doBatchInsertByList(List<T> list);

	public int doInsertByMap(Map<String, Object> param);

	public int doDeleteById(int id);

	public int doDeleteByMap(Map<String, Object> param);

	public int doUpdateByBean(T bean);

	public int doBatchUpdateByList(List<T> list);

	public int doUpdateByMap(Map<String, Object> param);

	public T doFindById(int id);

	public T doFindByBean(T bean);

	public List<T> doSearchListByMap(Map<String, Object> param);

	public int doDeleteById(T bean);

}
