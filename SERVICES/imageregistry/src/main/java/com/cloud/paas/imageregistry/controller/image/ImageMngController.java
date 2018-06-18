package com.cloud.paas.imageregistry.controller.image;

import com.cloud.paas.imageregistry.model.ImageDetail;
import com.cloud.paas.imageregistry.model.ImageVersion;
import com.cloud.paas.imageregistry.service.image.ImageService;
import com.cloud.paas.imageregistry.service.image.ImageVersionService;
import com.cloud.paas.imageregistry.service.impl.image.ImageServiceImpl;
import com.cloud.paas.imageregistry.vo.dockerfile.DockerFileDetailVO;
import com.cloud.paas.imageregistry.vo.image.ImageVersionVO;
import com.cloud.paas.imageregistry.vo.image.SrvImageVO;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.remoteinfo.RemoteServerInfo;
import com.cloud.paas.util.result.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.cloud.paas.imageregistry.model.ImageDetail.ImageAddValidate;
import java.util.*;

/**
 * @Author: wyj
 * @desc: 镜像和镜像详情的Controller
 * @Date: Created in 2017/11/25 13:07
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/image")
public class ImageMngController {

    private static final Logger logger = LoggerFactory.getLogger(ImageMngController.class);
    @Autowired
    private ImageVersionService imageVersionService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageServiceImpl imageServiceImpl;

    @ApiOperation(value = "将dockerfile传入数据库中(弃用！！)", notes = "收集dockerfile信息转为json存入数据库")
    @ApiImplicitParam(name = "dockerFileDetailVO", required = true, dataType = "DockerFileDetailVO")
    @PostMapping(value = "/dockerfile")
    public String InsertDockerFile(@RequestBody DockerFileDetailVO dockerFileDetailVO) throws Exception {
        try {
            imageServiceImpl.InsertDockerFile(dockerFileDetailVO);
        } catch (Exception e) {
            logger.error("插入数据库失败", e.getMessage());
            return "error";
        }
        return "success";
    }

    @ApiOperation(value = "构建镜像", notes = "调用接口或者在线完成镜像构建")
    @ApiImplicitParam(name = "imageVersionVO", value = "构建镜像的版本", required = true, dataType = "imageVersionVO")
    @GetMapping(value = "/build/{imageVersionId}")
    public Result buildImage(@PathVariable("imageVersionId") int imageVersionId) throws Exception{
        //调用镜像构建服务
        return imageVersionService.buildAndPushToRegistry(imageVersionId);
    }

    @ApiOperation(value = "上传本地镜像", notes = "本地tar包添加到镜像仓库")
    @ApiImplicitParam(name = "imageVersionId", value = "镜像的版本id", required = true, dataType = "int")
    @PostMapping(value = "/{userId}/addlocal")
    public Result addByLocal(@PathVariable("userId") String userId,@RequestBody @Validated({ImageVersion.ImageVersionAddValidate.class, ImageVersion.ImageVersionLocalAddValidate.class}) ImageVersionVO imageVersionVO) throws Exception {
        return imageVersionService.addByLocal(userId,imageVersionVO);
    }

    @ApiOperation(value = "上传镜像", notes = "上传镜像到服务器")
    @ApiImplicitParam(name = "multipartFile", value = "上传的文件", required = true, dataType = "MultipartFile")
    @PostMapping(value = "/upload")
    public Result uploadImage(@RequestParam MultipartFile file) {
        return imageVersionService.uploadImage(file);
    }

    // yht
    @ApiOperation(value = "新建镜像版本", notes = "向版本信息表中添加版本信息")
    @ApiImplicitParam(name = "imageVersion", value = "镜像版本信息对象", required = true, dataType = "ImageVersion")
    @PostMapping("/version/{userId}/add")
    public Result addImageVersion(@PathVariable("userId") String userId, @RequestBody ImageVersion imageVersion) throws Exception {
        return imageVersionService.insertUploadImage(userId, imageVersion);
    }

    @ApiOperation(value = "删除镜像的指定版本", notes = "从版本信息表中删除版本信息")
    @ApiImplicitParam(name = "imageVersionId", value = "镜像版本信息对象id", required = true, dataType = "int")
    @DeleteMapping("/version/{userId}/{imageVersionId}")
    public Result deleteImageVersion(@PathVariable("userId") String userId, @PathVariable("imageVersionId") int imageVersionId) throws Exception{
        return imageVersionService.doDeleteByImageVersionId(userId, imageVersionId);
    }

    @ApiOperation(value = "修改镜像指定版本信息", notes = "从版本信息表中更新版本信息")
    @ApiImplicitParam(name = "imageVersion", value = "镜像版本信息对象", required = true, dataType = "ImageVersion")
    @PutMapping("/version/{userId}/update")
    public Result updateImageVersion(@PathVariable("userId") String userId, @RequestBody ImageVersion imageVersion) throws  Exception{
        return imageVersionService.doUpdateImage(userId, imageVersion);
    }

    @ApiOperation(value = "获取指定镜像的版本列表", notes = "从版本信息表中查找指定版本信息")
    @ApiImplicitParam(name = "imageId", value = "镜像编号", required = true, dataType = "imageId")
    @GetMapping("/version/{userId}/{imageId}")
    public Result findImageId(@PathVariable("userId") String userId, @PathVariable("imageId") int imageId) throws Exception{
        return imageVersionService.listVersionByImageId(userId, imageId);
    }

    @ApiOperation(value = "获取镜像指定版本信息", notes = "从版本信息表中查找指定版本信息")
    @ApiImplicitParam(name = "imageversionId", value = "镜像编号", required = true, dataType = "imageversionId")
    @GetMapping("/version/{userId}/{imageversionId}/detail")
    public Result findDetailImageByVersion(@PathVariable("userId") String userId, @PathVariable("imageversionId") int imageversionId) throws  Exception {
        return imageVersionService.doFindImageInfo(userId, imageversionId);
    }

    @ApiOperation(value = "多个条件查询版本信息", notes = "多个条件查询版本信息，页面传值,页面四个条件查询")
    @ApiImplicitParam(name = "imageVersionVO", value = "镜像VO", required = true, dataType = "imageVersionVO")
    @PostMapping("/version/{userId}/detail")
    public Result findDetailImage(@PathVariable("userId") String userId, @RequestBody ImageVersionVO imageVersionVO) throws Exception {
        return imageVersionService.listImageInfo (userId,imageVersionVO);
    }

    @ApiOperation(value = "插入镜像详情和镜像版本信息", notes = "页面传值可以插入两个表")
    @ApiImplicitParam(name = "imageVersionVO", value = "镜像VO", required = true, dataType = "imageVersionVO")
    @PostMapping("/version/{userId}/insert")
    public Result insertDetail(@PathVariable("userId") String userId,@RequestBody ImageVersionVO imageVersionVO) throws Exception {
        return imageVersionService.insertImageDetailInfo (userId,imageVersionVO);
    }

    @ApiOperation(value = "查找镜像详情、镜像版本信息和仓库", notes = "运行环境的查询")
    @ApiImplicitParam(name = "imageVersionVO", value = "镜像VO", required = true, dataType = "imageVersionVO")
    @PostMapping("/version/{userId}/find")
    public Result findDetail(@PathVariable("userId") String userId,@RequestBody ImageVersionVO imageVersionVO) throws  Exception {
        return imageVersionService.findDetailInfo (userId,imageVersionVO);
    }

    @ApiOperation(value = "查找镜像详情、镜像版本信息和仓库", notes = "服务中选择镜像")
    @ApiImplicitParam(name = "imageVersionVO", value = "镜像VO", required = true, dataType = "imageVersionVO")
    @PostMapping("/version/{userId}/srv/find")
    public Result findSrvDetail(@PathVariable("userId") String userId,@RequestBody ImageVersionVO imageVersionVO) throws  Exception {
        return imageVersionService.findSrvDetailInfo (userId,imageVersionVO);
    }

    // wyj
    @ApiOperation(value = "查询单个镜像详情", notes = "查询单个镜像详情")
    @ApiImplicitParam(name = "imageId", value = "单个镜像的id", required = true, dataType = "int")
    @GetMapping(value = "/find/{imageId}")
    public Result getImageById(@PathVariable("imageId") int imageId) throws Exception{
        Result result = CodeStatusUtil.resultByCodeEn ("IMAGE_QUERY_SUCCESS");
        result.setData (imageService.doFindById(imageId));
        return result;
    }

    @ApiOperation(value = "新建镜像", notes = "新增镜像基本信息")
    @ApiImplicitParam(name = "imageDetail", value = "新增的镜像信息", required = true, dataType = "ImageDetail")
    @PostMapping(value = "/{userId}/add")
    public Result insertImage(@PathVariable("userId") String userId, @RequestBody @Validated({ImageAddValidate.class}) ImageDetail imageDetail) throws Exception{
        return imageService.insertImage(userId, imageDetail);
    }

    @ApiOperation(value = "校验镜像中文名称", notes = "校验镜像中文名称")
    @ApiImplicitParam(name = "imageDetail", value = "新增的镜像信息", required = true, dataType = "ImageDetail")
    @GetMapping(value = "/{userId}/zh/{imageNameZh}")
    public Result verifyImageZh(@PathVariable("userId") String userId, @PathVariable("imageNameZh") String imageNameZh) throws Exception{
        return imageService.verifyNameZh (imageNameZh);
    }
    @ApiOperation(value = "校验镜像英文名称", notes = "校验镜像英文名称")
    @ApiImplicitParam(name = "imageDetail", value = "新增的镜像信息", required = true, dataType = "ImageDetail")
    @GetMapping(value = "/{userId}/en/{imageNameEn}")
    public Result verifyImageEn(@PathVariable("userId") String userId, @PathVariable("imageNameEn") String imageNameEn) throws Exception{
        return imageService.verifyNameEn (imageNameEn);
    }

    @ApiOperation(value = "更新镜像", notes = "更新镜像详情")
    @ApiImplicitParam(name = "imageDetail", value = "新增的镜像信息", required = true, dataType = "ImageDetail")
    @PutMapping(value = "/{userId}/update")
    public Result updateImage(@PathVariable("userId") String userId, @RequestBody @Validated({ImageDetail.ImageUpdateValidate.class}) ImageDetail imageDetail) throws Exception{
        return imageService.updateImage(userId, imageDetail);
    }

    @ApiOperation(value = "删除镜像", notes = "删除仓库中已经存在的镜像")
    @ApiImplicitParam(name = "imageId", value = "新增的镜像信息", required = true, dataType = "int")
    @DeleteMapping(value = "/{userId}/{imageId}")
    public Result deleteImageById(@PathVariable("userId") String userId, @PathVariable("imageId") int imageId) throws Exception{
        return imageService.deleteImage(userId, imageId);
    }

    @ApiOperation(value = "镜像列表", notes = "获取全量镜像列表")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String")
    @GetMapping(value = "/all/{userId}")
    public Result listAllImage(@PathVariable("userId") String userId) throws  Exception{
        return imageService.listAllImages(userId);
    }

    @ApiOperation(value = "用户下的所有镜像列表", notes = "获取指定用户下的所有镜像的列表")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String")
    @GetMapping(value = "/user/{userId}")
    public Result listImageByUserId(@PathVariable("userId") String userId) throws  Exception{
        return imageService.listAllImages(userId);
    }

    @ApiOperation(value = "镜像仓库下的镜像列表", notes = "获取镜像仓库下的镜像列表")
    @ApiImplicitParam(name = "registryId", value = "仓库id", required = true, dataType = "int")
    @GetMapping(value = "/registry/{userId}/{registryId}")
    public Result listImageByRegistryId(@PathVariable("userId") String userId, @PathVariable("registryId") int registryId) throws  Exception{
        return imageService.listImageByRegistryId(userId, registryId);
    }

    @ApiOperation(value = "根据条件查询镜像的详细信息", notes = "根据条件查询镜像的详细信息")
    @ApiImplicitParam(name = "imageDetail", value = "查询的条件", required = true, dataType = "imageDetail")
    @PostMapping(value = "/{userId}/queryCondition")
    public Result listImageByCondition(@PathVariable("userId") String userId, @RequestBody ImageDetail imageDetail) throws  Exception{
        return imageService.listAllImagesByCondition(userId,imageDetail);
    }

    @ApiOperation(value = "上传镜像图片", notes = "上传镜像图片到服务器")
    @ApiImplicitParam(name = "multipartFile", value = "上传的图片", required = true, dataType = "MultipartFile")
    @PostMapping(value = "/upLoadImg")
    public Result uploadImagePicture(@RequestParam MultipartFile multipartFile) throws  Exception{
        return imageService.uploadImagePicture(multipartFile);
    }

    @ApiOperation(value = "创建并构建镜像", notes = "通过dockerfile创建并构建镜像")
    @ApiImplicitParam(name = "multipartFile", value = "上传的图片", required = true, dataType = "MultipartFile")
    @PostMapping(value = "/createAndBuild")
    public Result createAndBuild(@RequestBody @Validated(ImageVersion.ImageVersionAddValidate.class) ImageVersionVO imageVersionVO) throws Exception {
        return imageVersionService.createAndBuild (imageVersionVO);
    }

    @ApiOperation(value = "创建镜像和dockerfile", notes = "创建镜像并创建dockerfile")
    @ApiImplicitParam(name = "multipartFile", value = "上传的图片", required = true, dataType = "MultipartFile")
    @PostMapping(value = "/version/createImageAndDockerfile")
    public Result createImageAndDockerfile(@RequestBody @Validated(ImageVersion.ImageVersionAddValidate.class) ImageVersionVO imageVersionVO) throws Exception {
        return imageVersionService.createVersionAndDockerfile (imageVersionVO);
    }

    @ApiOperation(value = "获取镜像(包括ip和端口)", notes = "根据镜像的id，获取镜像仓库中的镜像")
    @ApiImplicitParam(name = "imageVersionId", value = "镜像版本的id", required = true, dataType = "int")
    @GetMapping(value = "/getRemoteImageInfo/{imageVersionId}")
    public Result getRemoteImageInfo(@PathVariable("imageVersionId") int imageVersionId ) throws Exception {
        return imageVersionService.getRemoteImageInfo(imageVersionId);
    }

    @ApiOperation(value = "获取镜像使用规则", notes = "根据镜像版本的id，获取镜像版本的使用规则")
    @ApiImplicitParam(name = "imageVersionId", value = "镜像版本的id", required = true, dataType = "int")
    @GetMapping(value = "/getImageVersionRule/{imageVersionId}")
    public Result getImageVersionRule(@PathVariable("imageVersionId") int imageVersionId ) throws Exception {
        return imageVersionService.getImageVersionRule(imageVersionId);
    }

    @ApiOperation(value = "FTP下载", notes = "根据IP、端口和用户密码和远端服务器下载 ")
    @ApiImplicitParam(name = "imageVersion", value = "镜像版本", required = true, dataType = "imageVersion")
    @PostMapping(value = "/{userid}/ftpdownlownfile/{flag}")
    public Result FtpDownloadCtr(@PathVariable("userid") String userid, @RequestBody RemoteServerInfo remoteServerInfo, @PathVariable("flag") int flag) throws Exception {
        return imageVersionService.FtpDownload(userid,remoteServerInfo,flag);
    }
    @ApiOperation(value = "SCP下载", notes = "根据IP、端口和用户密码和远端服务器下载 ")
    @PostMapping(value = "/{userid}/scpdownloadfile/{flag}")
    public Result ScpDownloadCtr(@PathVariable("userid") String userid, @RequestBody RemoteServerInfo remoteServerInfo, @PathVariable("flag") int flag) throws Exception {
        return imageVersionService.ScpDownload(userid,remoteServerInfo,flag);
    }

    @ApiOperation(value = "服务创建镜像并构建", notes = "提供给服务模块调用创建镜像并构建")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userid", value = "用户编号", required = true, dataType = "String"),
        @ApiImplicitParam(name = "srvImageVO", value = "服务镜像参数", required = true, dataType = "SrvImageVO")
    })
    @PostMapping(value = "/{userid}/createAndBuildSrvImage")
    public Result createAndBuildSrvImage(@PathVariable("userid") String userid, @RequestBody SrvImageVO srvImageVO) throws Exception {
        return imageService.createAndBuildSrvImage(userid,srvImageVO);
    }

    @ApiOperation(value = "查询服务镜像的状态", notes = "查询服务镜像的状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "srvImageVOs", value = "服务镜像参数列表", required = true, dataType = "List")
    })
    @PostMapping(value = "/srv/syncSrvVersionImageStatus")
    public Result querySrvImageStatus( @RequestBody List<SrvImageVO> srvImageVOs) throws Exception {
        return imageVersionService.querySrvImageStatus(srvImageVOs);
    }

    @ApiOperation(value = "devops创建镜像", notes = "devops创建镜像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvImageVO", value = "服务镜像参数", required = true, dataType = "SrvImageVO")
    })
    @PostMapping(value = "/{userid}/srv/createImage")
    public Result createImage(@PathVariable("userid") String userid, @RequestBody SrvImageVO srvImageVO) throws Exception {
        return imageService.buildSrvImage(userid, srvImageVO);
    }

    @ApiOperation(value = "devops创建镜像版本并创建dockerfile", notes = "devops创建镜像版本并创建dockerfile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvImageVO", value = "服务镜像参数", required = true, dataType = "SrvImageVO")
    })
    @PostMapping(value = "/{userid}/createImageVersionAndDockerfile")
    public Result createImageVersionAndDockerfile(@PathVariable("userid") String userid, @RequestBody SrvImageVO srvImageVO) throws Exception {
        return imageService.createImageVersionAndDockerfile(userid, srvImageVO);
    }

    @ApiOperation(value = "devops查询dockerfile", notes = "devops查询dockerfile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "imageVersionId", value = "镜像版本编号", required = true, dataType = "Integer")
    })
    @GetMapping(value = "/{userid}/getVersionDockerfile/{imageVersionId}")
    public String getVersionDockerfile(@PathVariable("userid") String userid, @PathVariable("imageVersionId") Integer imageVersionId) throws Exception {
        return imageService.getVersionDockerfile(userid, imageVersionId);
    }

}
