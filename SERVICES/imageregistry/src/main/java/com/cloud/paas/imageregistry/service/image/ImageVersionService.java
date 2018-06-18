package com.cloud.paas.imageregistry.service.image;

import com.cloud.paas.imageregistry.model.ImageVersion;
import com.cloud.paas.imageregistry.model.ImageVersionRule;
import com.cloud.paas.imageregistry.service.BaseService;
import com.cloud.paas.imageregistry.vo.image.ImageVersionVO;
import com.cloud.paas.imageregistry.vo.image.SrvImageVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.remoteinfo.RemoteServerInfo;
import com.cloud.paas.util.result.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: yht
 * @Description: 镜像版本信息service层接口
 * @Date: Create in 11:19 2017/11/24
 * @Modified by:
 */
public interface ImageVersionService extends BaseService<ImageVersion> {
    /**根据imageId查找镜像版本信息
     * @param userId 用户的id
     * @param imageId 镜像id
     * @return 镜像版本信息
     */
    Result listVersionByImageId(String userId, int imageId) throws  Exception;

    /**
     * 插入一条版本信息
     * @param userId 用户id
     * @param imageVersion 需要插入的镜像版本信息对象
     */
    int doInsertImage(String userId, ImageVersion imageVersion);

    /**
     * 更新版本信息
     * @param userId 用户id
     * @param imageVersion 需要插入的镜像版本信息对象
     */
    Result doUpdateImage(String userId, ImageVersion imageVersion) throws  Exception;

    /**
     * 根据imageVersionId删除一条版本信息
     * @param userId 用户id
     * @param imageVersionId 镜像版本信息id
     */
    Result doDeleteByImageVersionId(String userId, int imageVersionId) throws Exception;

    /**
     * 根据imageversionId查找一条版本的详细信息
     * @param userId 用户id
     * @param imageVersionId 镜像版本信息id
     * @return 版本的详细信息
     */
    Result doFindImageInfo(String userId, int imageVersionId) throws  Exception;
    /**
     * 根据imageversionVO查找版本的详细信息
     * @param userId 用户id
     * @param imageVersionVO 查询条件
     * @return 版本的详细信息
     */
    Result listImageInfo(String userId, ImageVersionVO imageVersionVO) throws Exception;

    /**
     * 插入上传镜像的信息
     * @param userId
     * @param imageVersion
     * @return
     */
    Result insertUploadImage(String userId, ImageVersion imageVersion) throws  Exception;
    /**
     * 上传镜像文件
     * @param multipartFile 多文本
     * @return
     */
    Result uploadImage(MultipartFile multipartFile);
    /**
     * 插入镜像的详细信息
     * @param imageVersionVO VO类对象
     * @return imageVersionId 镜像版本id
     */
    Result insertImageDetailInfo(String userId, ImageVersionVO imageVersionVO)throws Exception;
    /**
     * 查找镜像的详细信息
     * @param imageVersionVO
     * @return imageVersionVO集合
     */
     Result findDetailInfo(String userId, ImageVersionVO imageVersionVO) throws Exception;

    /**
     * 服务中查找镜像的详细信息
     * @param imageVersionVO
     * @return imageVersionVO集合
     */
    Result findSrvDetailInfo(String userId, ImageVersionVO imageVersionVO) throws Exception;
    /**
     * 查找镜像的详细信息
     * @param imageVersionVO
     * @return imageVersionVO集合
     */
    Result createVersionAndDockerfile(ImageVersionVO imageVersionVO);

    /**
     * 根据镜像的id，获取镜像仓库中的镜像
     * ip:port/imageName:tag
     * @param imageVersionId
     * @return
     * @throws Exception
     */
    Result getRemoteImageInfo(int imageVersionId) throws Exception;

    /**
     * 根据镜像版本的id，获取镜像版本的使用规则
     * ip:port/imageName:tag
     * @param imageVersionId 镜像id
     * @return
     * @throws Exception
     */
    Result getImageVersionRule(int imageVersionId) throws BusinessException;

    /**
     *
     * @param remoteServerInfo
     * @return
     * @throws Exception
     */
    Result FtpDownload(String userid, RemoteServerInfo remoteServerInfo, int flag) throws Exception;
    /**
     *
     * @param imageVersion
     * @return
     * @throws Exception
     */
    //public Result FtpUpload(String userId,ImageVersion imageVersion,int flag) throws Exception;

    /**
     * 镜像上传：本地镜像文件
     * @param imageVersionVO
     * @return
     */
    Result addByLocal(String userId, ImageVersionVO imageVersionVO);
   // Result ScpUpload(String userId, ImageSCP imageSCP, int flag) throws Exception;
    Result ScpDownload(String userId, RemoteServerInfo remoteServerInfo, int flag) throws Exception;
    /**
     * 创建并构建
     * @param imageVersionVO
     * @return
     * @throws Exception
     */
    Result createAndBuild(ImageVersionVO imageVersionVO);
    /**
     * 通过dockerfile构建镜像并push到仓库
     * @param imageVersionId 镜像版本VO
     * @return
     * @throws Exception
     */
    Result buildAndPushToRegistry(int imageVersionId);


    Result refreshStatus(String userid, List<Long> imageIds);

    /*********************************镜像版本规则开始**********************************/

    /**
     * 根据镜像版本编号查询镜像版本规则
     * @param imageVersionId
     * @return Result
     */
    Result listImageVersionRuleByImageVersionId(String imageVersionId);

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
    Result doUpdateImageVersionRule(ImageVersionRule imageVersionRule) throws BusinessException;

    /**
     * 根据镜像版本编号删除镜像版本规则
     * @param imageVersionId
     * @return
     * @throws BusinessException
     */
    Result doDeleteImageVersionRuleByImageVersionId(int imageVersionId) throws BusinessException;

    /*********************************镜像版本规则结束**********************************/

    /**
     * 查询服务镜像状态
     * @param srvImageVOS
     * @return
     * @throws BusinessException
     */
    Result querySrvImageStatus(List<SrvImageVO> srvImageVOS)throws BusinessException;

}



