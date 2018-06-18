package com.cloud.paas.systemmanager.dao;

import com.cloud.paas.systemmanager.model.ResourceInfo;

import java.util.List;

/**
 * @Author: weiwei
 * @Description:总资源dao
 * @Date: Create  2018/1/11
 * @Modified by:
 */
public interface ResourceInfoDAO extends BaseDAO<ResourceInfo>{

    /**
     * 获取全量资源列表
     * @return
     */
    List<ResourceInfo> listResources();
    ResourceInfo getRestResource();

}
