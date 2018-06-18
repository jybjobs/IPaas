package com.cloud.paas.monitor.service.impl;


import com.cloud.paas.monitor.dao.BaseDAO;
import com.cloud.paas.monitor.model.ValueObject;
import com.cloud.paas.monitor.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @Author: wangzhifeng
 * @Description:
 * @Date: Create in 17:28 2017/11/2
 * @Modified by:
 */
public abstract class BaseServiceImpl<T extends ValueObject> implements BaseService<T> {

    public abstract BaseDAO<T> getBaseDAO();

    public int doInsertByBean(T bean) {
        return getBaseDAO().doInsertByBean(bean);
    }

    public int doBatchInsertByList(List<T> list) {
        return getBaseDAO().doBatchInsertByList(list);
    }

    public int doInsertByMap(Map<String, Object> param) {
        return getBaseDAO().doInsertByMap(param);
    }

    public int doDeleteById(int id) {
        return getBaseDAO().doDeleteById(id);
    }

    public int doDeleteById(T bean) {
        return getBaseDAO().doDeleteById(bean);
    }

    public int doDeleteByMap(Map<String, Object> param) {
        return getBaseDAO().doDeleteByMap(param);
    }

    public int doUpdateByBean(T bean) {
        return getBaseDAO().doUpdateByBean(bean);
    }

    public int doBatchUpdateByList(List<T> list) {
        return getBaseDAO().doBatchUpdateByList(list);
    }

    public int doUpdateByMap(Map<String, Object> param) {
        return getBaseDAO().doUpdateByMap(param);
    }

    public T doFindById(int id) {
        return getBaseDAO().doFindById(id);
    }

    public T doFindByBean(T bean) {
        return getBaseDAO().doFindByBean(bean);
    }

    public List<T> doSearchListByBean(T bean) {
        return getBaseDAO().doSearchListByBean(bean);
    }

    public List<T> doSearchListByMap(Map<String, Object> map) {
        return getBaseDAO().doSearchListByMap(map);
    }


}
