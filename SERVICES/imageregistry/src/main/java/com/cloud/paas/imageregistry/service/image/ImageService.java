package com.cloud.paas.imageregistry.service.image;

import com.cloud.paas.imageregistry.model.DockerFileDetail;
import com.cloud.paas.imageregistry.model.ImageDetail;
import com.cloud.paas.imageregistry.service.BaseService;
import com.cloud.paas.imageregistry.vo.dockerfile.DockerFileDetailVO;
import com.cloud.paas.imageregistry.vo.image.SrvImageVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.result.Result;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: wyj
 * @desc: 镜像操作相关的service层接口
 * @Date: Created in 2017/11/25 13:17
 * @Modified By:
 */
public interface ImageService extends BaseService<ImageDetail> {

    /**
     * 将仓库的镜像拉取到本地服务器
     *
     * @param registryTag 仓库tag
     * @param registryId 仓库id
     * @throws DockerCertificateException
     * @throws DockerException
     * @throws InterruptedException
     */
    void pull(String registryTag, String registryId) throws DockerCertificateException, DockerException, InterruptedException;

    /**
     * 获取全景镜像列表
     * @param userId 用户权限Id
     * @return 所有列表信息
     * @throws Exception
     */
    Result listAllImages(String userId) throws Exception;

    /**
     * 新增镜像
     * @param userId 用户id，用于权限验证
     * @param imageDetail 新增的镜像信息
     * @return 是否成功
     */
    Result insertImage(String userId, ImageDetail imageDetail) throws Exception;

    /**
     * 更新镜像信息
     * @param userId 用户id，用于权限验证
     * @param imageDetail 更新的镜像信息
     * @return 是否更新成功
     * @throws Exception
     */
    Result updateImage(String userId, ImageDetail imageDetail) throws Exception;

    /**
     * 删除指定镜像
     * @param imageId 删除的镜像id
     * @return 是否删除成功
     * @throws Exception
     */
    Result deleteImage(String userId, int imageId) throws Exception;

    /**
     * 获取镜像仓库下的镜像列表
     * @param registryId 仓库id
     * @return 所有列表接口
     * @throws Exception
     */
    Result listImageByRegistryId(String userId, int registryId) throws Exception;


    DockerFileDetail InsertDockerFile(DockerFileDetailVO dockerFileDetailVO) throws Exception;

    /**
     * 根据条件查询用户的镜像详情
     * @param imageDetail 查询的条件
     * @return 镜像详情的列表
     */
    Result listAllImagesByCondition(String userId, ImageDetail imageDetail) throws Exception;


    /**
     * 上传镜像图片
     * @param multipartFile 多文本文件
     * @return
     */
    Result uploadImagePicture(MultipartFile multipartFile) throws Exception;


    Result verifyNameZh(String imageNameZh);
    Result verifyNameEn(String imageNameEn);

    /**
     * 创建并且构建服务镜像
     * @throws BusinessException
     */
    Result createAndBuildSrvImage(String userid , SrvImageVO srvImageVO)throws BusinessException;

    /**
     * 构建服务镜像
     *
     * @param userid
     * @param srvImageVO
     * @return
     */
    public Result buildSrvImage(String userid, SrvImageVO srvImageVO)throws BusinessException;

    /**
     * devops创建镜像版本并创建dockerfile
     * @return
     * @throws BusinessException
     */
    public Result createImageVersionAndDockerfile(String userid,SrvImageVO srvImageVO)throws BusinessException;

    /**
     * 获取dockerfile
     * @param userid
     * @param imageVersionId
     * @return
     * @throws BusinessException
     */
    public String getVersionDockerfile(String userid,Integer imageVersionId)throws BusinessException;

}
