package com.cloud.paas.imageregistry.dao.impl.image;

import com.cloud.paas.imageregistry.dao.ImageVersionRuleDAO;
import com.cloud.paas.imageregistry.dao.impl.BaseDAOImpl;
import com.cloud.paas.imageregistry.model.ImageVersionRule;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.result.Result;
import org.springframework.stereotype.Repository;

/**
 * @Author: 17798
 * @desc: 镜像版本规则DAO层实现类
 * @Date: Created in 2018-03-22 10:05
 * @Modified By:
 */
@Repository("imageVersionRuleDAO")
public class ImageVersionRuleDAOImpl  extends BaseDAOImpl<ImageVersionRule> implements ImageVersionRuleDAO {

    /**
     * 当前类的详细包名和类名的常量
     *
     * NAME_SPACE: 该类实现的接口的路径 ？？？
     */
    public static final String NAME_SPACE = "ImageVersionRuleDAO";

    /**
     * 获取当前类的详细包名和类名
     *
     * @return 类的详细名称
     */
    @Override
    public String getNameSpace() {
        return ImageVersionRuleDAOImpl.NAME_SPACE;
    }


    @Override
    public ImageVersionRule getImageVersionRuleByImageVersionId(Integer imageVersionId) {
        return sqlSessionTemplate.selectOne(this.getNameSpace() + ".doFindByImageVersionId", imageVersionId);
    }

    @Override
    public int doInsertImageVersionRule(ImageVersionRule imageVersionRule) {
        return sqlSessionTemplate.insert(this.getNameSpace()+".doInsertByBean",imageVersionRule);
    }

    @Override
    public int doUpdateImageVersionRule(ImageVersionRule imageVersionRule) throws BusinessException {
        return sqlSessionTemplate.update(this.getNameSpace()+".doUpdateByBean",imageVersionRule);
    }

    @Override
    public int doDeleteImageVersionRuleByImageVersionId(int imageVersionId) throws BusinessException {
        return sqlSessionTemplate.delete(this.getNameSpace()+".doDeleteById",imageVersionId);
    }
}
