package com.cloud.paas.imageregistry.service.registry;

import com.cloud.paas.imageregistry.model.RegistryDetail;
import com.cloud.paas.imageregistry.qo.ConditionQuery;
import com.cloud.paas.imageregistry.service.BaseService;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.result.Result;

public interface RegistryService extends BaseService<RegistryDetail> {
    /**
     * 查询所有的仓库，并返回列表
     * @return
     */
     Result listRegistryDetail() throws Exception;
    /**
     * 通过中文名称查询
     */

     Result doFindByName(String name) throws Exception;

    /**
     * 通过英文名称查询
     */

     Result doFindByNameEn(String name) throws Exception;

    /**
     * 通过仓库的多条件查询
     * @return
     * @throws Exception
     */
     Result doFindByMultiConditionQuery(ConditionQuery condition) throws BusinessException;
    /**
     * 启动
     * @return
     * @throws Exception
     */
     Result startRegistry( Integer registryID)throws BusinessException;
    /**
     * 停止
     * @return
     * @throws Exception
     */
     Result stopRegistry( Integer registryID)throws BusinessException;
    /**
     * 通过ID查询
     * @return
     * @throws Exception
     */
     Result doFindById(Integer registryID)throws BusinessException;

    /**
     * 通过仓库id查询仓库中镜像版本的信息
     * @param registryDetail
     * @return
     * @throws Exception
     */
     Result listImageByRegistryId (RegistryDetail registryDetail)throws Exception;
    /**
     * 创建
     * @return
     * @throws Exception
     */
     Result doInsertByRegistry(RegistryDetail registryDetail)throws BusinessException;
    /**
     * 修改
     * @return
     * @throws Exception
     */
     Result doUpdateByRegistry(RegistryDetail registryDetail)throws BusinessException;
    /**
     * 删除
     * @return
     * @throws Exception
     */
     Result doDeleteById(Integer registryId)throws BusinessException;

    /**
     * 判断路径是否已经存在
     * @return
     * @throws Exception
     */
     Result JudageUrlExit(RegistryDetail registryDetail)throws Exception;
}
