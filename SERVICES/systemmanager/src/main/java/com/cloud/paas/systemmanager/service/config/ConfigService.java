package com.cloud.paas.systemmanager.service.config;

import com.cloud.paas.systemmanager.model.SysConfig;
import com.cloud.paas.systemmanager.qo.config.ConfigCondition;
import com.cloud.paas.systemmanager.service.BaseService;
import com.cloud.paas.util.result.Result;

import java.util.Map;

public interface ConfigService extends BaseService<SysConfig> {
    /**
     * 通过中文名字来查找名单
     * @param configChName
     * @return
     */
     Result selectByChNameSrv(String configChName);
    /**
     * 通过英文名字来查找配置
     * @param configEnName
     * @return
     */
     Result selectByEnNameSrv(String configEnName);
    /**
     * 得到所有配置列表(分页条件查询）
     * @param
     * @return
     */
     Result getAllConfig(ConfigCondition configCondition);

    /**
     * 获取所有配置列表（纯配置列表）
     * @return
     */
    Map getAllConfigList();
    /**
     * 插入配置
     * @param sysConfig
     * @return
     */
     Result insertSelectiveSrv(SysConfig sysConfig);
    /**
     * 更新配置
     * @param sysConfigId
     * @return
     */
     Result updateByPrimaryKeySrv(SysConfig sysConfigId);
    /**
     * 删除配置
     * @param sysConfigId
     * @return
     */
     Result deleteByPrimaryKeySrv(int sysConfigId);
}
