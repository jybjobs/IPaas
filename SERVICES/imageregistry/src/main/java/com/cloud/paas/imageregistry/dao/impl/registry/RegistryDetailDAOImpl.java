package com.cloud.paas.imageregistry.dao.impl.registry;

import com.cloud.paas.imageregistry.dao.RegistryDetailDAO;
import com.cloud.paas.imageregistry.dao.impl.BaseDAOImpl;
import com.cloud.paas.imageregistry.model.RegistryDetail;
import com.cloud.paas.imageregistry.qo.ConditionQuery;
import com.cloud.paas.imageregistry.vo.registry.RegistryDetailVO;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by kaiwen on 2017/11/22.
 */
@Repository("registryDetailDAO")
public class RegistryDetailDAOImpl extends BaseDAOImpl<RegistryDetail>  implements RegistryDetailDAO {
    public static final String NAME_SPACE = "RegistryDetailDAO";
    private static final Logger logger = LoggerFactory.getLogger(RegistryDetailDAOImpl.class);

    @Override
    public  String getNameSpace(){
        return RegistryDetailDAOImpl.NAME_SPACE;
    }

    
    /**
     * 根据镜像id查询仓库详细信息
     * @param imageId 镜像id
     * @return 仓库的详细信息
     */
    @Override
    public RegistryDetail doFindRegistryByImageId(long imageId) {
        return sqlSessionTemplate.selectOne(this.getNameSpace()+".doFindRegistryByImageId",imageId);
    }

    /**
     * 实现查询所有的仓库，并返回列表
     * @return
     */
    @Override
    public List<RegistryDetail> listRegistryDetail() {

        return sqlSessionTemplate.selectList(this.getNameSpace()+".listRegistryDetail");
    }

    /**
     * 通过名称查询
     */
    @Override
    public RegistryDetail doFindByName(String name) throws Exception {
        return sqlSessionTemplate.selectOne(this.getNameSpace()+".doFindByName",name);
    }

    /**
     * 通过英文名称查询
     */
    @Override
    public RegistryDetail doFindByNameEn(String name) throws Exception{
        return sqlSessionTemplate.selectOne(this.getNameSpace()+".doFindByNameEn",name);
    }
    @Override
    public RegistryDetail doFindByRegistryId(Integer registryId) {
        return sqlSessionTemplate.selectOne(this.getNameSpace()+".doFindByRegistryId",registryId);
    }

    public RegistryDetailVO doFindById(Integer registryID)throws Exception{
        return sqlSessionTemplate.selectOne(this.getNameSpace()+".doFindById",registryID);
    }
    /**
     * 通过多条件混合查询
     */
    @Override
    public List<RegistryDetailVO> doFindByMultiConditionQuery(ConditionQuery condition) throws Exception {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".doFindByMultiConditionQuery",condition);
    }

    @Override
    public List<RegistryDetail> doFindByRegistryIp(String registryIp) {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".doFindByRegistryIp",registryIp);
    }
}
