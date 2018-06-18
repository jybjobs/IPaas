package com.cloud.paas.systemmanager.dao.impl.config;
import com.cloud.paas.systemmanager.dao.impl.BaseDAOImpl;
import com.cloud.paas.systemmanager.dao.SysConfigDao;
import com.cloud.paas.systemmanager.model.SysConfig;
import com.cloud.paas.systemmanager.qo.config.ConfigCondition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("sysConfigDao")
public class SysConfigDaoImpl extends BaseDAOImpl<SysConfig> implements SysConfigDao {
    @Override
    public String getNameSpace() { return "SysConfigDao";}
    @Override
    public SysConfig selectByChName(String configChName) {
        return sqlSessionTemplate.selectOne(this.getNameSpace()+".selectByChName",configChName);
    }

    @Override
    public SysConfig selectByEnName(String configEnName) {
        return sqlSessionTemplate.selectOne(this.getNameSpace()+".selectByEnName",configEnName);
    }

    /**
     * 分页条件查询配置列表
     * @param configCondition
     * @return
     */
    @Override
    public List<SysConfig> selectByAll(ConfigCondition configCondition) {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".selectByAll",configCondition);
    }



    @Override
    public int insertSelective(SysConfig sysConfig) {
        return sqlSessionTemplate.insert(this.getNameSpace()+".insertSelective",sysConfig);
    }

    @Override
    public int updateByPrimaryKey(SysConfig sysConfig) {
        return sqlSessionTemplate.update(this.getNameSpace()+".updateByPrimaryKey",sysConfig);
    }

    @Override
    public int deleteByPrimaryKey(int sysConfigId) {
        return sqlSessionTemplate.delete(this.getNameSpace()+".deleteByPrimaryKey",sysConfigId) ;
    }


}