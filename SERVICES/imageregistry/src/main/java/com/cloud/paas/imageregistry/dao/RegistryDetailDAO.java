package com.cloud.paas.imageregistry.dao;

import com.cloud.paas.imageregistry.model.RegistryDetail;
import com.cloud.paas.imageregistry.qo.ConditionQuery;
import com.cloud.paas.imageregistry.vo.registry.RegistryDetailVO;
import com.cloud.paas.exception.BusinessException;

import java.util.List;

/**
 * Created by kaiwen on 2017/11/22.
 */
public interface RegistryDetailDAO extends BaseDAO<RegistryDetail>{

    /**
     * 根据imageId查询仓库的详细信息
     * @param imageId 镜像id
     * @return 仓库的详细信息
     */
    RegistryDetail doFindRegistryByImageId(long imageId);
    /**
     * 查询所有的仓库，并返回列表
     * @return
     */
     RegistryDetailVO doFindById(Integer registryID)throws Exception;
     List<RegistryDetail> listRegistryDetail();

    /**
     * 通过中文名称查询
     */

     RegistryDetail doFindByName(String name) throws Exception;

    /**
     * 通过英文名称查询
     */

     RegistryDetail doFindByNameEn(String name) throws Exception;
    /**
     * 通过id称查询
     */
     RegistryDetail doFindByRegistryId(Integer registryId);

    /**
     * 通过多条件混合查询
     */
     List<RegistryDetailVO> doFindByMultiConditionQuery(ConditionQuery condition) throws Exception;

    /**
     * 通过ip查询仓库
     * @param registryIp
     * @return
     * @throws BusinessException
     */
    List<RegistryDetail> doFindByRegistryIp(String registryIp) throws BusinessException;
}
