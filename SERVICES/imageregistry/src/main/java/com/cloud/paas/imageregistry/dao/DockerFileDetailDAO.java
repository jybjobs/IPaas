package com.cloud.paas.imageregistry.dao;

import com.cloud.paas.imageregistry.model.DockerFileDetail;

/**
 * @Author: wyj
 * @desc: dockerfile信息详情DAO层接口
 * @Date: Created in 2017-11-27 15:16
 * @Modified By:
 */
public interface DockerFileDetailDAO extends BaseDAO<DockerFileDetail> {

    /**
     * 引用到业务包的个数
     * @param busiPkgVersionId
     * @return
     */
    Integer CountByPkgVersionId(Integer busiPkgVersionId);
}
