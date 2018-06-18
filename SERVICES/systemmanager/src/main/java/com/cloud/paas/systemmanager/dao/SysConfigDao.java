package com.cloud.paas.systemmanager.dao;

import com.cloud.paas.systemmanager.model.SysConfig;
import com.cloud.paas.systemmanager.qo.config.ConfigCondition;

import java.util.List;

public interface SysConfigDao extends BaseDAO<SysConfig> {
    /**
     * 根据中文名查找配置
     * @param configChName
     * @return
     */
    public SysConfig selectByChName(String configChName);

    public SysConfig selectByEnName(String configEnName);

    /**
     * 分页条件查询配置列表
     * @param configCondition
     * @return
     */
    public List<SysConfig> selectByAll(ConfigCondition configCondition);


    /**
     * 插入配置项
     * @param sysConfig
     * @return
     */
    public int insertSelective(SysConfig sysConfig);

    /**
     * 更新配置项
     * @param sysConfig
     * @return
     */
    public int updateByPrimaryKey(SysConfig sysConfig);

    /**
     * 删除配置项
     * @param sysConfigId
     * @return
     */
    public int deleteByPrimaryKey(int sysConfigId);
}
