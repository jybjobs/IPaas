package com.cloud.paas.systemmanager.dao.impl.tenant;

import com.cloud.paas.systemmanager.dao.ResourceInfoDAO;
import com.cloud.paas.systemmanager.dao.impl.BaseDAOImpl;
import com.cloud.paas.systemmanager.model.ResourceInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("resourceDao")
public class ResourceInfoDAOImpl extends BaseDAOImpl<ResourceInfo> implements ResourceInfoDAO{
    public static final String NAME_SPACE = "ResourceInfoDAO";

    @Override
    public  String getNameSpace(){
        return ResourceInfoDAOImpl.NAME_SPACE;
    }

    /**
     * 获取全量资源列表
     * @return
     */
    @Override
    public List<ResourceInfo> listResources() {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".listResources");
    }

    /**
     * 获取剩余资源配额
     */
    @Override
    public ResourceInfo getRestResource(){
        return  sqlSessionTemplate.selectOne(this.getNameSpace()+".getRestResource");
    }
}
