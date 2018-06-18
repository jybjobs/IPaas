package com.cloud.paas.systemmanager.service;

import com.cloud.paas.systemmanager.model.ValueObject;

import java.util.List;
import java.util.Map;

public interface BaseService<T extends ValueObject> {
    public int doInsertByBean(T bean);

    public int doBatchInsertByList(List<T> list);

    public int doInsertByMap(Map<String,Object> param);

    public int doDeleteById(int id);

    public int doDeleteByMap(Map<String,Object> param);


    public int doUpdateByBean(T bean);

    public int doBatchUpdateByList(List<T> list);

    public int doUpdateByMap(Map<String,Object> param);


    public T doFindById(int id);


    public T doFindByBean(T bean);

    public List<T> doSearchListByBean(T bean);

    public List<T> doSearchListByMap(Map<String, Object> map);

//	public PageInfo<T> doSearchPage(T vo) throws Exception;
//
//	public PageInfo<T> doSearchPage(T vo,int pageNum, int pageSize) throws Exception;
//
//	public PageInfo<T> doSearchPage(T vo,String orderStr,int pageNum, int pageSize) throws Exception;
//
//
//	public PageInfo<T> doSearchPage(Map<String, Object> map,int pageNum, int pageSize) throws Exception;
//
//	public PageInfo<T> doSearchPage(Map<String, Object> map, String orderStr,int pageNum, int pageSize) throws Exception;

    public int doDeleteById(T bean);
}
