package com.cloud.paas.imageregistry.dao.impl.image;

import com.cloud.paas.imageregistry.dao.ImageVersionMngDAO;
import com.cloud.paas.imageregistry.dao.impl.BaseDAOImpl;
import com.cloud.paas.imageregistry.model.ImageDetail;
import com.cloud.paas.imageregistry.model.ImageVersion;
import com.cloud.paas.imageregistry.vo.image.ImageListVO;
import com.cloud.paas.imageregistry.vo.image.ImageVersionVO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: yht
 * @desc: 镜像版本信息DAO层实现类
 * @Date: Created in 2017-11-22 12:44
 * @Modified By:
 */
@Repository("imageVersionMngDAO")
public class ImageVersionMngDAOImpl extends BaseDAOImpl<ImageVersion> implements ImageVersionMngDAO {
    /**
     * Dao的路径，用于与mapper.xml的配置
     */
    public static final String NAME_SPACE = "ImageVersionMngDAO";

    /**
     * 根据imageId查出镜像版本的信息
     * @param imageId 镜像id
     * @return 返回版本信息列表
     */
    public List<ImageVersion> listVersionByImageId(int imageId) {
        return sqlSessionTemplate.selectList(this.getNameSpace() + ".listVersionByImageId", imageId);
    }

    /**
     * 根据imageVersionVO查出镜像的信息
     * @param imageVersionVO
     * @return 返回版本信息VO类列表
     */
    @Override
    public List<ImageListVO> listImageDetail(ImageVersionVO imageVersionVO) {
        return sqlSessionTemplate.selectList(this.getNameSpace() + ".selectByConditions", imageVersionVO);
    }

    /**
     * 服务中选择镜像
     * @param imageVersionVO
     * @return
     */
    @Override
    public List<ImageVersionVO> doFindSrvPage(ImageVersionVO imageVersionVO) {
        return sqlSessionTemplate.selectList(this.getNameSpace() + ".selectVersionDetailForSrv", imageVersionVO);
    }

    /**
     * 根据imageversionId查出一条版本信息的详细内容
     * @param imageversionId 镜像版本id
     * @return 返回一条版本信息的详细内容
     */
    @Override
    public ImageVersionVO doFindDetailByImageId(int imageversionId) {
        return sqlSessionTemplate.selectOne(this.getNameSpace() + ".selectVersionInformation", imageversionId);
    }

    /**
     * 查出版本信息的详细内容
     * @param
     * @return 返回一条版本信息的详细内容
     */
    @Override
    public List<ImageVersionVO> doFindDetail(ImageVersionVO imageVersionVO) {
        return sqlSessionTemplate.selectList (this.getNameSpace ()+".selectVersionDetail",imageVersionVO);
    }

    /**
     * 根据仓库id获取镜像版本
     * @param registryId
     * @return
     */
    @Override
    public List<ImageVersion> listByRegistryId(Integer registryId) {
        return sqlSessionTemplate.selectList(this.getNameSpace() + ".listByRegistryId", registryId);
    }

    /**
     * 通过仓库的id,查询仓库中已经存在的镜像版本
     *
     * @param registryId
     * @return
     */
    @Override
    public List<ImageVersionVO> listImageByRegistryId(Integer registryId) {
        return sqlSessionTemplate.selectList(this.getNameSpace() + ".listImageByRegistryId", registryId);

    }

    @Override
    public List<ImageDetail> findPageByIdList(ImageVersionVO imageVersionVO) {
        return sqlSessionTemplate.selectList(this.getNameSpace() + ".selectPageByConditions", imageVersionVO);
    }

    @Override
    public int confirmByDelete(int imageId) {
        return sqlSessionTemplate.selectOne(this.getNameSpace() + ".confirmDelete", imageId);
    }

    @Override
    public BigDecimal sumImageSize(long imageId) {
        return sqlSessionTemplate.selectOne(this.getNameSpace() + ".imageSizeSum", imageId);
    }

    /**
     * 获取NAME_SPACE的常量值
     * @return NAME_SPACE
     */
    @Override
    public String getNameSpace() {
        return ImageVersionMngDAOImpl.NAME_SPACE;
    }

    /**
     *
     * @param imageVersion
     * @return
     */
    @Override
    public int doInsertByBean(ImageVersion imageVersion){

        return sqlSessionTemplate.insert(this.getNameSpace()+".doInsertByBean",imageVersion);
    }

    @Override
    public List<ImageVersion> getStatusByImageIds(List<Long> imageIds) {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".getStatusByImageIds",imageIds);
    }
}

