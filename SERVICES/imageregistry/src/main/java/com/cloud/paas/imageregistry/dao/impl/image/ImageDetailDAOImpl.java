package com.cloud.paas.imageregistry.dao.impl.image;

import com.cloud.paas.imageregistry.dao.ImageDetailDAO;
import com.cloud.paas.imageregistry.dao.impl.BaseDAOImpl;
import com.cloud.paas.imageregistry.model.ImageDetail;
import com.cloud.paas.imageregistry.vo.image.ImageListVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: wyj
 * @desc: 镜像详情DAO层实现类
 * @Date: Created in 2017-11-22 12:44
 * @Modified By:
 */
@Repository("imageDetailDAO")
public class ImageDetailDAOImpl extends BaseDAOImpl<ImageDetail> implements ImageDetailDAO {

    /**
     * 当前类的详细包名和类名的常量
     *
     * NAME_SPACE: 该类实现的接口的路径 ？？？
     */
    public static final String NAME_SPACE = "ImageDetailDAO";


    /**
     * 获取当前类的详细包名和类名
     *
     * @return 类的详细名称
     */
    @Override
    public String getNameSpace() {
        return ImageDetailDAOImpl.NAME_SPACE;
    }

    /**
     * 获取全景镜像列表
     *
     * @return 镜像列表集合
     */
    @Override
    public List<ImageDetail> listAllImages() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".selectAllImages");
    }


    /**
     * 根据条件查询用户所有镜像
     *
     * @param imageDetail 镜像详细信息（包括镜像名称和镜像类型）
     * @return 镜像详情列表
     */
    @Override
    public List<ImageDetail> listAllImagesByCondition(ImageDetail imageDetail) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".selectAllImagesByCondition", imageDetail);
    }


    /**
     * 获取镜像仓库下的镜像列表
     *
     * @param registryId 仓库id
     * @return 镜像仓库下的镜像列表
     */
    @Override
    public List<ImageListVO> listImageByRegistryId(int registryId) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".selectImageList", registryId);
    }

    @Override
    public ImageDetail findImageNameEn(String imageNameEn) {
        return sqlSessionTemplate.selectOne (this.getNameSpace ()+".selectEnNameForVerify",imageNameEn);

    }

    @Override
    public ImageDetail findImageNameZh(String imageNameZh) {
        return sqlSessionTemplate.selectOne (this.getNameSpace ()+".selectZhNameForVerify",imageNameZh);
    }
}
