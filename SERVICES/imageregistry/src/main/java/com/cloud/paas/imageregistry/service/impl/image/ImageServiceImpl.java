package com.cloud.paas.imageregistry.service.impl.image;

import com.alibaba.fastjson.JSON;
import com.cloud.paas.imageregistry.dao.BaseDAO;
import com.cloud.paas.imageregistry.dao.DockerFileDetailDAO;
import com.cloud.paas.imageregistry.dao.ImageDetailDAO;
import com.cloud.paas.imageregistry.dao.ImageVersionMngDAO;
import com.cloud.paas.imageregistry.model.*;
import com.cloud.paas.imageregistry.vo.dockerfile.DockerFileDetailVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cloud.paas.imageregistry.service.image.ImageService;
import com.cloud.paas.imageregistry.service.image.ImageVersionService;
import com.cloud.paas.imageregistry.service.impl.BaseServiceImpl;
import com.cloud.paas.imageregistry.util.Config;
import com.cloud.paas.imageregistry.util.RegistryUtil;
import com.cloud.paas.imageregistry.vo.image.ImageListVO;
import com.cloud.paas.imageregistry.vo.image.ImageVersionVO;
import com.cloud.paas.imageregistry.vo.image.SrvImageVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.bean.BeanUtil;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.date.DateUtil;
import com.cloud.paas.util.fileupload.UploadFile;
import com.cloud.paas.util.page.PageUtil;
import com.cloud.paas.util.result.Result;
import com.cloud.paas.util.zip.ZipUtil;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: wyj
 * @desc: 镜像相关操作的service层实现类
 * @Date: Created in 2017/11/25 13:18
 * @Modified By:
 */
@SuppressWarnings({"serial", "rawtypes", "unchecked"})
@Service("imageService")
public class ImageServiceImpl extends BaseServiceImpl<ImageDetail> implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    private static final String IMAGE_REMARK = "服务构建过程中创建的镜像";

    @Autowired
    private ImageDetailDAO imageDetailDAO;
    @Autowired
    private ImageVersionMngDAO imageVersionMngDAO;
    @Autowired
    private DockerFileDetailDAO dockerFileDetailDAO;

    @Autowired
    ImageVersionService imageVersionService;

    @Autowired
    VerifyImage verifyImage;
    @Autowired
    VeryImageVersion veryImageVersion;

    /**
     * 复写父类的方法
     *
     * @return dao对象
     */
    @Override
    public BaseDAO<ImageDetail> getBaseDAO() {
        return imageDetailDAO;
    }

    DateUtil dateUtil = new DateUtil();

    /**
     * 使用dockerfile构建镜像文件的时候，将dockerfile的信息传入数据库
     *
     * @param dockerFileDetailVO
     * @return
     * @throws Exception
     */
    public DockerFileDetail InsertDockerFile(DockerFileDetailVO dockerFileDetailVO) throws Exception {
        DockerFileDetail dockerFileDetail = new DockerFileDetail();
        //TODO 优化判断
        if (null != dockerFileDetailVO) {
            //把dockerFileDetailVO 和 dockerFileDetail相同属性的value复制到dockerFileDetail中
            BeanUtil.copyBean2Bean(dockerFileDetail, dockerFileDetailVO);
            if (null == dockerFileDetail.getPort()) {
                dockerFileDetailDAO.doInsertByBean(dockerFileDetail);
            } else {
                dockerFileDetail.setAddPath(JSON.toJSON(dockerFileDetailVO.getAddPath()).toString());
                dockerFileDetail.setCmd(JSON.toJSON(dockerFileDetailVO.getCmd()).toString());
                dockerFileDetail.setPort(JSON.toJSON(dockerFileDetailVO.getPort()).toString());
                dockerFileDetail.setEnv(JSON.toJSON(dockerFileDetailVO.getEnv()).toString());
                dockerFileDetail.setRun(JSON.toJSON(dockerFileDetailVO.getRun()).toString());
                DateUtil dateUtil = new DateUtil();
                Date date = dateUtil.getCurrentTime();
                dockerFileDetail.setCreateTime(date);
                dockerFileDetail.setUpdateTime(date);
                dockerFileDetailDAO.doInsertByBean(dockerFileDetail);
            }
            return dockerFileDetail;
        }
        return null;

    }

    /**
     * 将仓库的镜像拉取到本地服务器
     *
     * @param registryTag 仓库tag
     * @param registryId  仓库id
     * @throws DockerCertificateException
     * @throws DockerException
     * @throws InterruptedException
     */
    @Override
    public void pull(String registryTag, String registryId) throws DockerCertificateException, DockerException, InterruptedException {
        RegistryUtil registryUtil = new RegistryUtil();
        //DockerUtil.getDocker().pull(registryTag, registryUtil.getRegistryAuth(registryId));
    }


    /**
     * 获取全景镜像列表
     *
     * @param userId 用户权限Id
     * @return 所有列表信息
     * @throws Exception
     */
    @Override
    public Result listAllImages(String userId) throws Exception {
        //TODO: 2017/11/23  权限验证操作，成功进行数据查询
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_QUERY_SUCCESS");
        List<ImageDetail> imageDetails = imageDetailDAO.listAllImages();
        result.setData(imageDetails);
        return result;
    }

    /**
     * 新增镜像
     *
     * @param userId      用户id，用于权限验证
     * @param imageDetail 新增的镜像信息
     * @return 是否成功
     */
    @Override
    public Result insertImage(String userId, ImageDetail imageDetail) {
        //TODO 做用户权限验证
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_CREATE_FAIL");
        imageDetail.setCreator("wyj");
        logger.info("进行镜像信息校验");
        verifyImage.createImageDetail(imageDetail);
        logger.info("向数据库插入镜像");
        if (1 != super.doInsertByBean(imageDetail)) {
            logger.error("向数据库插入镜像失败");
            throw new BusinessException(result);
        }
        result = CodeStatusUtil.resultByCodeEn("IMAGE_CREATE_SUCCESS");
        result.setData(imageDetail.getImageId());
        return result;
    }

    public Result verifyNameZh(String imageNameZh) {
        Result result = new Result(1, "0", "名称未使用", 0, 1, 0);
        ImageDetail imageDetail = new ImageDetail();
        imageDetail = imageDetailDAO.findImageNameZh(imageNameZh);
        if (imageDetail == null) {
            return result;
        }
        if (imageDetail.getImageNameZh().equals(imageNameZh)) {
            result = CodeStatusUtil.resultByCodeEn("IMAGE_ZHNAME_REPEATE");
            result.setData(imageNameZh);
        }
        return result;
    }

    public Result verifyNameEn(String imageNameEn) {
        Result result = new Result(1, "0", "名称未使用", 0, 1, 0);
        ImageDetail imageDetail = imageDetailDAO.findImageNameEn(imageNameEn);
        if (imageDetail == null) {
            return result;
        }
        if (imageDetail.getImageNameEn().equals(imageNameEn)) {
            result = CodeStatusUtil.resultByCodeEn("IMAGE_ENNAME_REPEATE");
            result.setData(imageNameEn);
        }
        return result;
    }

    /**
     * 更新镜像信息
     *
     * @param userId      用户id，用于权限验证
     * @param imageDetail 更新的镜像信息
     * @return 是否更新成功
     * @throws Exception
     */
    @Override
    public Result updateImage(String userId, ImageDetail imageDetail) {
        //TODO 做用户权限验证
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_MODIFY_FAILURE");
        logger.info("进行修改镜像信息校验");
        verifyImage.updateImageDetail(imageDetail);
        logger.info("数据库中更新镜像信息");
        if (1 != super.doUpdateByBean(imageDetail)) {
            logger.error("数据库中更新镜像信息失败");
            throw new BusinessException(result);
        }
        result = CodeStatusUtil.resultByCodeEn("IMAGE_MODIFY_SUCCESS");
        return result;
    }

    /**
     * 删除指定镜像
     *
     * @param imageId 删除的镜像id
     * @return 是否删除成功
     * @throws Exception
     */
    @Override
    public Result deleteImage(String userId, int imageId) {
        //TODO 做用户权限验证
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_DELLETE_FAILURE");
        int count = imageVersionMngDAO.confirmByDelete(imageId);
        logger.debug("查看是否有镜像版本" + count + "个镜像版本");
        if (count == 0) {
            try {
                if (1 != super.doDeleteById(imageId)) {
                    logger.error("数据库删除镜像失败");
                    throw new BusinessException(result);
                }
            } catch (IOException e) {
                logger.error("数据库删除镜像失败{}", e.getMessage());
                throw new BusinessException(result);
            }
        } else {
            result.setMessage("镜像已创建版本，无法删除");
            result.setData(count);
            throw new BusinessException(result);
        }
        result = CodeStatusUtil.resultByCodeEn("IMAGE_DELETE_SUCCESS");
        // 判断返回，成功返回200
        return result;
    }

    /**
     * 获取镜像仓库下的镜像列表
     *
     * @param registryId 仓库id
     * @return 所有列表接口
     * @throws Exception
     */
    @Override
    public Result listImageByRegistryId(String userId, int registryId) throws Exception {
        //TODO 做用户权限验证
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_QUERY_SUCCESS");
        List<ImageListVO> imageListVOS = imageDetailDAO.listImageByRegistryId(registryId);
        result.setData(imageListVOS);
        return result;
    }

    /**
     * 根据条件查询镜像详细信息
     *
     * @param userId      用户id
     * @param imageDetail 查询的条件
     * @return 镜像详细信息列表
     */
    @Override
    public Result listAllImagesByCondition(String userId, ImageDetail imageDetail) throws Exception {
        //TODO 做用户权限验证
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_QUERY_SUCCESS");
        int pageNum = imageDetail.getPageNum();
        int pageSize = imageDetail.getPageSize();
        logger.debug("pageNum" + pageNum + " pageSize:" + pageSize);
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<ImageDetail> imageDetails = imageDetailDAO.listAllImagesByCondition(imageDetail);
        PageInfo pageInfo = PageUtil.getPageInfo(page, imageDetails);
        pageInfo.setList(imageDetails);
        result.setData(pageInfo);
        return result;
    }

    /**
     * 上传镜像图片
     *
     * @param multipartFile 多文本文件
     * @return
     */
    @Override
    public Result uploadImagePicture(MultipartFile multipartFile) throws Exception {
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_UPLOAD_PICTURE_FAIL");
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
        logger.debug("上传图片后缀名:" + suffixName);
        try {
            if (suffixName != null) {
                // 判断文件后缀
                if ("GIF".equals(suffixName.toUpperCase()) || "PNG".equals(suffixName.toUpperCase()) || "JPG".equals(suffixName.toUpperCase())) {
                    UploadFile uploadFile = new UploadFile();
                    String filePath = Config.UPLOAD_DIRECTORY_IMAGE_IMG;
                    Result uploadPathResult = uploadFile.uploadFile(multipartFile, filePath);
                    Map<String, String> imgPathData = (Map) uploadPathResult.getData();
                    String uploadPath = imgPathData.get("path");
                    result = CodeStatusUtil.resultByCodeEn("IMAGE_UPLOAD_PICTURE_SUCCESS");
                    result.setData(uploadPath);
                    return result;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    /*************************************************服务镜像创建开始*************************************************/

    /**
     * 创建并构建镜像并推入仓库
     *
     * @param userid
     * @param srvImageVO
     * @return 是否成功
     */
    @Override
    public Result createAndBuildSrvImage(String userid, SrvImageVO srvImageVO) throws BusinessException {
        Result result;
        //1、构建镜像
        logger.debug("1、构建镜像:{}",DateUtil.getCurrentTime(new Date()));
        result = buildSrvImage(userid, srvImageVO);
        long imageId = (long) result.getData();
        //2、构建dockerfile
        logger.debug("2、构建dockerfile:{}",DateUtil.getCurrentTime(new Date()));
        result = buildSrvDockerfile(srvImageVO);
        int dockerfileId = (int) result.getData();
        //3、构建镜像版本
        logger.debug("3、构建镜像版本:{}",DateUtil.getCurrentTime(new Date()));
        result = buildSrvImageVersion(userid, imageId, dockerfileId, srvImageVO);
        //4、构建镜像并推入仓库
        logger.debug("4、构建镜像并推入仓库:{}",DateUtil.getCurrentTime(new Date()));
        if (1 == result.getSuccess()) {
            //构建并推入仓库
            imageVersionService.buildAndPushToRegistry(Integer.parseInt(result.getData().toString()));
        }
        return result;
    }

    /**
     * 构建服务镜像
     *
     * @param userid
     * @param srvImageVO
     * @return
     */
    @Override
    public Result buildSrvImage(String userid, SrvImageVO srvImageVO) {
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_CREATE_FAIL");
        ImageDetail imageDetail = new ImageDetail();
        //1、初始化镜像类型为服务，标明该镜像为服务构建过程中创建的镜像
        imageDetail.setImageType(4);
        //2、使用服务名填充镜像名称
        imageDetail.setImageNameEn(srvImageVO.getSrvNameEn());
        imageDetail.setImageNameZh(srvImageVO.getSrvNameZh());
        //3、初始化镜像总的大小为0，否则会出错
        imageDetail.setImageSize(new BigDecimal(0));
        imageDetail.setImageRemark(IMAGE_REMARK);
        imageDetail.setCreateTime(dateUtil.getCurrentTime());
        //4、创建服务镜像
        try {
            result = insertImage(userid, imageDetail);
        }catch (BusinessException e){
            //捕获异常，已存在该英文名的服务
            logger.debug("该服务已存在，插入操作改为查询操作");
            imageDetail = imageDetailDAO.findImageNameEn(imageDetail.getImageNameEn());
            result = CodeStatusUtil.resultByCodeEn("IMAGE_CREATE_SUCCESS");
            result.setData(imageDetail.getImageId());
        }
        return result;
    }

    /**
     * devops创建镜像版本并创建dockerfile
     * @return
     * @throws BusinessException
     */
    @Override
    public Result createImageVersionAndDockerfile(String userid,SrvImageVO srvImageVO) throws BusinessException {
        Result result;
        //1、构建镜像(正常情况下，镜像在创建项目时已经生成)
        logger.debug("1、构建镜像:{}",DateUtil.getCurrentTime(new Date()));
        result = buildSrvImage(userid, srvImageVO);
        long imageId = (long) result.getData();
        //2、构建dockerfile
        logger.debug("2、构建dockerfile:{}",DateUtil.getCurrentTime(new Date()));
        result = buildDockerfile(srvImageVO);
        int dockerfileId = (int) result.getData();
        //3、构建镜像版本
        logger.debug("3、构建镜像版本:{}",DateUtil.getCurrentTime(new Date()));
        srvImageVO.setSrvImageId(null);
        return buildSrvImageVersion(userid, imageId, dockerfileId, srvImageVO);
    }

    /**
     * 构建dockerfile
     *
     * @param srvImageVO
     * @return
     */
    public Result buildDockerfile(SrvImageVO srvImageVO) {
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_DOCKERFILE_CREATE_FAILURE");
        //1、构建dockerfile的content
        String content = generateDockerfileContent(srvImageVO);
        //2、创建dockerfile
        logger.debug("创建dockerfile");
        DockerFileDetail dockerFileDetail = new DockerFileDetail();
        dockerFileDetail.setContent(content);
        dockerFileDetail.setBaseImageVersionId(srvImageVO.getSrvImageVersionId());
        dockerFileDetail.setCreator("skw");
        //插入dockerfile
        if (1 != dockerFileDetailDAO.doInsertByBean(dockerFileDetail)) {
            logger.error("插入dockerfile失败");
            throw new BusinessException(result);
        }
        result = CodeStatusUtil.resultByCodeEn("IMAGE_CREATE_SUCCESS");
        result.setData(dockerFileDetail.getDockerfileId());
        return result;
    }

    /**
     * 构建dockerfile
     *
     * @param srvImageVO
     * @return
     */
    public Result buildSrvDockerfile(SrvImageVO srvImageVO) {
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_DOCKERFILE_CREATE_FAILURE");
        //1、构建dockerfile的content
        String content = generateDockerfileContent(srvImageVO);
        //2、创建dockerfile
        logger.debug("创建dockerfile");
        DockerFileDetail dockerFileDetail = new DockerFileDetail();
        dockerFileDetail.setContent(content);
        dockerFileDetail.setBaseImageVersionId(srvImageVO.getSrvImageVersionId());
        dockerFileDetail.setBasePkgVersionId(srvImageVO.getBusiPkgVersionId());
        dockerFileDetail.setCreator("skw");
        //插入dockerfile
        if (1 != dockerFileDetailDAO.doInsertByBean(dockerFileDetail)) {
            logger.error("插入dockerfile失败");
            throw new BusinessException(result);
        }
        result = CodeStatusUtil.resultByCodeEn("IMAGE_CREATE_SUCCESS");
        result.setData(dockerFileDetail.getDockerfileId());
        return result;
    }


    private String generateDockerfileContent(SrvImageVO srvImageVO) {
        StringBuffer dockerfileContent = new StringBuffer();
        //1、获取镜像版本的pull路径
        ImageVersionVO imageVersionVO = imageVersionMngDAO.doFindDetailByImageId(srvImageVO.getSrvImageVersionId());
        RegistryDetail registryDetail = imageVersionVO.getRegistryDetail();
        String pullPath = registryDetail.getRegistryIp() + ":" + registryDetail.getRegistryPort() + "/"
                + imageVersionVO.getImageDetail().getImageNameEn() + ":" + imageVersionVO.getImageVersion();
        logger.debug("pull路径为:----------------{}", pullPath);
        dockerfileContent.append("FROM ").append(pullPath).append(" \n");
        //2、获取镜像版本的发布目录
        ImageVersionRule imageVersionRule = imageVersionVO.getImageVersionRule();
        String publishPaths = imageVersionRule.getPublishPath();
        for (String publishPath : publishPaths.split(",")) {
            //由于ADD * 时需要最路径末尾为/，即文件路径
            if (!"/".equals(publishPath.substring(publishPath.length() - 1)) &&
                    !"\\".equals(publishPath.substring(publishPath.length() - 1))) {
                publishPath += "/";
            }
            dockerfileContent.append("ADD * ").append(publishPath).append("\n");
            dockerfileContent.append("RUN chmod 755 ").append(publishPath).append("\n");
            dockerfileContent.append("RUN rm -rf ").append(publishPath).append("*.dockerfile").append("\n");
        }
        //3、暴露端口处理
        String exposePorts = imageVersionRule.getExposePort();
        for(String exposePort : exposePorts.split(",")){
            dockerfileContent.append("EXPOSE ").append(exposePort).append("\n");
        }
        //4、生成dockerfileContent
        return dockerfileContent.toString();
    }

    /**
     * 创建服务镜像版本
     *
     * @param userid
     * @param imageId
     * @param dockerfileId
     * @param srvImageVO
     * @return
     */
    private Result buildSrvImageVersion(String userid, long imageId, int dockerfileId, SrvImageVO srvImageVO) {
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_CREATE_FAIL");
        ImageVersion imageVersion;
        if(null != srvImageVO.getSrvImageId() && !"".equals(srvImageVO.getSrvImageId())){
            imageVersion = imageVersionMngDAO.doFindById(srvImageVO.getSrvImageId());
        }else{
            imageVersion = new ImageVersion();
            imageVersion.setCreateTime(dateUtil.getCurrentTime());
            //1、设置关联镜像
            imageVersion.setImageId(imageId);
            //2、设置关联dockerfile
            imageVersion.setDockfileId(dockerfileId);
            //3、设置版本号（服务版本号）
            //校验版本是否唯一
            logger.info("校验版本是否唯一");
            ImageVersionVO imageVersionVO = new ImageVersionVO();
            imageVersionVO.setImageVersion(srvImageVO.getSrvVersion());
            imageVersionVO.setImageId(imageId);
            veryImageVersion.veryVersion(imageVersionVO);
            imageVersion.setImageVersion(srvImageVO.getSrvVersion());
            //4、设置业务包关联
            imageVersion.setBusiPkgVersionId(srvImageVO.getBusiPkgVersionId());
            //5、初始化镜像为私有
            imageVersion.setImageAuth(2);
            //6、初始化镜像版本大小为0
            imageVersion.setImageVersionSize(0f);
            //7、设置镜像仓库编号为1，默认鸿信公有仓库，后期租户功能上线后，此处该转为租户私有仓库
            imageVersion.setRegistryId(1);
            //8、镜像构建中
            imageVersion.setImageStatus(CodeStatusUtil.getInstance().getStatusByCodeEn("IMAGE_BUILDING").getCode());
            if (1 != imageVersionMngDAO.doInsertByBean(imageVersion)) {
                logger.error("数据库插入镜像版本失败");
                throw new BusinessException(result);
            }
        }
        result = CodeStatusUtil.resultByCodeEn("IMAGE_CREATE_SUCCESS");
        result.setData(imageVersion.getImageVersionId());
        return result;
    }

    /**
     * 获取dockerfile
     * @param userid
     * @param imageVersionId
     * @return
     * @throws BusinessException
     */
    @Override
    public String getVersionDockerfile(String userid, Integer imageVersionId) throws BusinessException {
        ImageVersionVO imageVersionVO = imageVersionMngDAO.doFindDetailByImageId(imageVersionId);
        return imageVersionVO.getDockerFileDetail().getContent();
    }


}

