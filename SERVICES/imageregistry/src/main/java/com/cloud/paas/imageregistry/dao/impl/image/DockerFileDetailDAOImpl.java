package com.cloud.paas.imageregistry.dao.impl.image;

import com.cloud.paas.imageregistry.dao.DockerFileDetailDAO;
import com.cloud.paas.imageregistry.dao.impl.BaseDAOImpl;
import com.cloud.paas.imageregistry.model.DockerFileDetail;
import org.springframework.stereotype.Repository;

/**
 * @Author: wyj
 * @desc: dockerFile详细信息DAO层实现类
 * @Date: Created in 2017-11-27 15:18
 * @Modified By:
 */
@Repository("dockerFileDetailDAO")
public class DockerFileDetailDAOImpl extends BaseDAOImpl<DockerFileDetail> implements DockerFileDetailDAO {

    /**
     * 当前类的详细包名和类名的常量
     */
    public static final String NAME_SPACE = "DockerFileDetailDAO";

    /**
     * 获取当前类的详细包名和类名
     * @return 类的详细名称
     */
    @Override
    public String getNameSpace() {
        return DockerFileDetailDAOImpl.NAME_SPACE;
    }

    @Override
    public Integer CountByPkgVersionId(Integer busiPkgVersionId) {
        return this.sqlSessionTemplate.selectOne(this.getNameSpace()+".countByPkgVersionId",busiPkgVersionId);
    }
}
