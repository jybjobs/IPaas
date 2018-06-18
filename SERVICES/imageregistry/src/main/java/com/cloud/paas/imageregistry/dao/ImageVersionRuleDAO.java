package com.cloud.paas.imageregistry.dao;


import com.cloud.paas.imageregistry.model.ImageVersionRule;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.result.Result;

/**
 * @Author: 17798
 * @Description: 镜像版本规则DAO层接口
 * @Date: Create in 10:09 2018/03/22
 * @Modified by:
 */
public interface ImageVersionRuleDAO  extends BaseDAO<ImageVersionRule> {
    /**
     * 根据镜像版本编号查询镜像版本规则
     * @param imageVersionId
     * @return Result
     */
    ImageVersionRule getImageVersionRuleByImageVersionId(Integer imageVersionId);

    /**
     * 插入一条镜像版本规则
     * @param imageVersionRule
     * @return
     */
    int doInsertImageVersionRule(ImageVersionRule imageVersionRule);

    /**
     * 更新镜像版本规则
     * @param imageVersionRule
     * @return
     * @throws BusinessException
     */
    int doUpdateImageVersionRule(ImageVersionRule imageVersionRule) throws BusinessException;

    /**
     * 根据镜像版本编号删除镜像版本规则
     * @param imageVersionId
     * @return
     * @throws BusinessException
     */
    int doDeleteImageVersionRuleByImageVersionId(int imageVersionId) throws BusinessException;

}