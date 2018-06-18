package com.cloud.paas.systemmanager.dao.impl.tenant;

import com.cloud.paas.systemmanager.dao.TenantInfoDAO;
import com.cloud.paas.systemmanager.dao.impl.BaseDAOImpl;
import com.cloud.paas.systemmanager.model.TenantInfo;
import com.cloud.paas.systemmanager.qo.tenant.TenantCondition;
import com.cloud.paas.systemmanager.vo.dictory.TenantInfoVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("tenantDao")
public class TenantInfoDAOImpl extends BaseDAOImpl<TenantInfo> implements TenantInfoDAO {
    public static final String NAME_SPACE = "TenantInfoDAO";

    @Override
    public  String getNameSpace(){
        return TenantInfoDAOImpl.NAME_SPACE;
    }

    /**
     * 获取全量租户列表
     * @return
     */
    @Override
    public List<TenantInfoVO> listTenants(TenantCondition tenantCondition) {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".listTenants",tenantCondition);
    }


    /**
     * 获取指定租户信息
     * @param managerUserId
     * @return
     */
    public TenantInfoVO getTenantDetailInfo(Integer managerUserId){
        return sqlSessionTemplate.selectOne(this.getNameSpace()+".getTenantDetailInfo",managerUserId);
    }


    /**
     * 根据租户名查询租户信息
     * @param tenantName
     * @return
     */
    @Override
    public TenantInfo doFindByTenantName(String tenantName) {
        return sqlSessionTemplate.selectOne(this.getNameSpace()+".doFindByTenantName",tenantName);
    }
}
