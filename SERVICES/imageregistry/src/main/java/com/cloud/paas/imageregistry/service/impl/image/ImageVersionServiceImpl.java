package com.cloud.paas.imageregistry.service.impl.image;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.imageregistry.dao.*;
import com.cloud.paas.imageregistry.model.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cloud.paas.imageregistry.service.image.ImageVersionService;
import com.cloud.paas.imageregistry.service.impl.BaseServiceImpl;
import com.cloud.paas.imageregistry.service.impl.registry.RegistryServiceImpl;
import com.cloud.paas.imageregistry.service.registry.RegistryService;
import com.cloud.paas.imageregistry.util.Config;
import com.cloud.paas.imageregistry.util.DockerUtil;
import com.cloud.paas.imageregistry.util.RegistryUtil;
import com.cloud.paas.imageregistry.vo.image.ImageListVO;
import com.cloud.paas.imageregistry.vo.image.ImageVersionVO;
import com.cloud.paas.imageregistry.vo.image.SrvImageVO;
import com.cloud.paas.configuration.PropertiesConfUtil;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.bean.BeanUtil;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.fileupload.UploadFile;
import com.cloud.paas.util.ftp.FtpFileUtil;
import com.cloud.paas.util.page.PageUtil;
import com.cloud.paas.util.remoteinfo.RemoteServerInfo;
import com.cloud.paas.util.rest.RestClient;
import com.cloud.paas.util.rest.RestConstant;
import com.cloud.paas.util.result.Result;
import com.cloud.paas.util.scp.ScpUtil;
import com.cloud.paas.util.zip.ZipUtil;
import com.spotify.docker.client.ProgressHandler;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ProgressMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: yht
 * @Description: 镜像版本信息service层接口实现类
 * @Date: Create in 11:19 2017/11/24
 * @Modified by:
 */
@Service
public class ImageVersionServiceImpl extends BaseServiceImpl<ImageVersion> implements ImageVersionService {

    private static final Logger logger = LoggerFactory.getLogger(ImageVersionServiceImpl.class);
    /**
     * 注入Dao层对象
     */
    @Autowired
    private ImageVersionMngDAO imageVersionMngDAO;
    @Autowired
    private ImageDetailDAO imageDetailDAO;
    @Autowired
    private RegistryService registryService;
    @Autowired
    DockerFileDetailDAO dockerFileDetailDAO;
    @Autowired
    RegistryServiceImpl registryServiceimpl;
    @Autowired
    RegistryDetailDAO registryDetailDAO;
    @Autowired
    private ImageVersionRuleDAO imageVersionRuleDAO;
    @Autowired
    VeryImageVersion veryImageVersion;
    @Autowired
    private BusiPkgVersionMngDAO busiPkgVersionMngDAO;
    //注入异步方法类
    @Autowired
    private ImageVersionAsyncService imageVersionAsyncService;
    /**
     * 获取Dao层对象
     * @return Dao层对象
     */
    @Override
    public BaseDAO getBaseDAO() {
        return imageVersionMngDAO;
    }

    /**
     * 根据imageId查找镜像版本信息
     *
     * @param userId  用户id
     * @param imageId 镜像id
     * @return 镜像版本信息
     */
    @Override
    public Result listVersionByImageId(String userId, int imageId) throws Exception {
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_QUERY_SUCCESS");
        List<ImageVersion> imageVersions = imageVersionMngDAO.listVersionByImageId(imageId);
        result.setData(imageVersions);
        return result;
    }

    /**
     * 插入一条版本信息
     *
     * @param userId       用户id
     * @param imageVersion 需要插入的镜像版本id
     */
    @Override
    public int doInsertImage(String userId, ImageVersion imageVersion) {
        return imageVersionMngDAO.doInsertByBean(imageVersion);
    }

    /**
     * 更新版本信息
     *
     * @param userId       用户id
     * @param imageVersion 需要插入的镜像版本id
     */
    @Override
    public Result doUpdateImage(String userId, ImageVersion imageVersion) throws Exception {
        String id = userId;
        //  return imageVersionMngDAO.doUpdateByBean (imageVersion);
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_MODIFY_FAILURE");
        if (imageVersionMngDAO.doUpdateByBean(imageVersion) == 1) {
            result = CodeStatusUtil.resultByCodeEn("IMAGE_MODIFY_SUCCESS");
            result.setData(1);
        }
        return result;
    }

    /**
     * 根据imageVersionId删除一条版本信息
     *
     * @param userId         用户id
     * @param imageVersionId 镜像版本信息id
     */
    @Override
    public Result doDeleteByImageVersionId(String userId, int imageVersionId){
        //检查镜像是否被服务引用
        logger.info ("检查镜像是否被服务引用");
        checkInUse(imageVersionId);
        //数据库删除镜像版本
        deleteImageVersion(imageVersionId);
        //返回结果
        Result result = CodeStatusUtil.resultByCodeEn ("IMAGE_DELETE_SUCCESS");
        return result;
    }

    private void deleteImageVersion(int imageVersionId) {
        ImageVersionVO imageVersionVO = imageVersionMngDAO.doFindDetailByImageId (imageVersionId);
        if (!imageVersionVO.getImageStatus ().equals (CodeStatusUtil.getStatusByCodeEn ("IMAGE_UPLOADING_SUCCESS").getCode ())){
            //镜像未构建，直接数据库删除
        }else {
            //先从仓库中删除镜像
            String registryIp =  imageVersionVO.getRegistryDetail ().getRegistryIp ();
            String registryPort = imageVersionVO.getRegistryDetail ().getRegistryPort ().toString ();
            String imageEnName = imageVersionVO.getImageDetail ().getImageNameEn ();
            String imageVersion = imageVersionVO.getImageVersion ();
            registryServiceimpl.removeRegistryImage (registryIp,registryPort,imageEnName,imageVersion);
        }
        if (1!=imageVersionMngDAO.doDeleteById (imageVersionId)){
            logger.error("数据库删除镜像版本失败");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn ("IMAGE_DELETE_FAILURE"));
        }
    }

    private void checkInUse(int imageVersionId) {
        String rest = RestClient.doGet (PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_IMAGE_INUSE)+imageVersionId+"/findAll");
        Result result1 = JSONObject.parseObject (rest,Result.class);
        if (result1.getSuccess ()==0){
            logger.error("无法删除，镜像已被引用");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("IMAGE_INUSE"));
        }
    }

    /**
     * 根据imageversionId查找一条版本的详细信息
     *
     * @param userId         用户id
     * @param imageVersionId 镜像版本信息id
     * @return 版本的详细信息
     */
    @Override
    public Result doFindImageInfo(String userId, int imageVersionId) throws Exception {
        String id = userId;
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_QUERY_SUCCESS");
        ImageVersionVO imageVersionVO = imageVersionMngDAO.doFindDetailByImageId(imageVersionId);
        DockerFileDetail dockerFileDetail = imageVersionVO.getDockerFileDetail();
        if (dockerFileDetail != null) {
            JSONObject jsonObject = JSONObject.parseObject(dockerFileDetail.getCmd());
            imageVersionVO.setDockerfileJson(jsonObject);
        }
        ImageVersionRule imageVersionRule = imageVersionVO.getImageVersionRule();
        if (imageVersionRule != null){
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(imageVersionRule);
            imageVersionVO.setImageVersionRuleJson(jsonObject);
        }
        result.setData(imageVersionVO);
        return result;
    }

    /**
     * @param list
     * @return
     */
    public List<Integer> findImageIdListByExample(List<ImageDetail> list) {
        ImageDetail imageDetail = null;
        List<Integer> listImageId = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            imageDetail = list.get(i);
            long imageId = imageDetail.getImageId();
            listImageId.add((int) imageId);
        }
        return listImageId;
    }

    /**
     * 根据imageversionVO查找版本的详细信息
     *
     * @param userId         用户id
     * @param imageVersionVO 镜像版本信息
     * @return 版本的详细信息
     */
    @Override
    public Result listImageInfo(String userId, ImageVersionVO imageVersionVO) throws Exception {
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_QUERY_SUCCESS");
        //1.分页查询业务包Id列表
        //xml 写为List的时候 pageNum 页码 pageSize 每页的记录数
        int pageNum = imageVersionVO.getPageNum();
        int pageSize = imageVersionVO.getPageSize();
        logger.debug("pageNum" + pageNum + " pageSize:" + pageSize);
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<Integer> imageVersionIdList = new ArrayList<Integer>();
        List<ImageDetail> list = new ArrayList();
        list = imageVersionMngDAO.findPageByIdList(imageVersionVO);
        imageVersionIdList = this.findImageIdListByExample(list);
        PageInfo pageInfo = PageUtil.getPageInfo(page, imageVersionIdList);
        imageVersionVO.setImageVersionIdList(imageVersionIdList);
        imageVersionVO.setPageSize(0);
        imageVersionVO.setPageNum(0);
        List<ImageListVO> listImageListVO = imageVersionMngDAO.listImageDetail(imageVersionVO);

        for (ImageListVO imageListVOTemp : listImageListVO) {
            imageListVOTemp.setImageSize(imageVersionMngDAO.sumImageSize(imageListVOTemp.getImageId()));
            for (ImageVersionVO imageVersionVOTemp : imageListVOTemp.getImageVersionVO()) {
                RegistryDetail registryDetail = (RegistryDetail) registryService.doFindById(imageVersionVOTemp.getRegistryId()).getData();
                imageVersionVOTemp.setRegistryDetail(registryDetail);
            }
        }
        pageInfo.setList(listImageListVO);
        result.setData(pageInfo);
        return result;
    }

    /**
     * 插入上传的镜像信息
     *
     * @param userId
     * @param imageVersion
     * @return
     */
    @Override
    public Result insertUploadImage(String userId, ImageVersion imageVersion) throws Exception {
        // 获取文件的文件名
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_UPLOADING_FAILURE");
        UploadFile uploadFile = new UploadFile();
        String fileName = uploadFile.getFileName(imageVersion.getImageUploadName());
        imageVersion.setImageUploadName(fileName);
        if (imageVersionMngDAO.doInsertByBean(imageVersion) == 1) {
            result = CodeStatusUtil.resultByCodeEn("IMAGE_UPLOADING_SUCCESS");
            result.setData(1);
        }
        return result;
    }

    /**
     * 镜像上传：本地镜像文件
     *
     * @param imageVersionVO
     * @return
     */
    @Override
    public Result addByLocal(String userId, ImageVersionVO imageVersionVO){
        logger.info("本地上传镜像");
        //1.验证版本是否重复
        veryImageVersion.addLocalImageVersion(imageVersionVO);
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_BUILD_FAILURE");
        ImageVersion imageVersion = new ImageVersion();
        try {
            BeanUtil.copyBean2Bean(imageVersion, imageVersionVO);
        } catch (Exception e) {
            logger.error("copyBean2Bean(imageVersion, imageVersionVO)失败:{}",e.getMessage());
            throw new BusinessException(result);
        }
        //2.添加版本信息 先返回结果
        logger.info("数据库插入版本信息");
        imageVersion.setImageUploadWay(1);
        imageVersion.setCreator(Config.Creator_Admin);
        imageVersion.setImageStatus(CodeStatusUtil.getStatusByCodeEn("IMAGE_BUILDING").getCode());
        insertImageVersion(imageVersion);
        //3.load和build过程放到线程中
        logger.info("开始load镜像");
        imageVersionAsyncService.addByLocalAsync(userId,imageVersionVO,imageVersion);
        result = CodeStatusUtil.resultByCodeEn("IMAGE_CREATE_SUCCESS");
        return result;
    }

    /**
     * 插入镜像 统一进行异常处理
     * @param imageVersion
     */
    private void insertImageVersion(ImageVersion imageVersion) {
        if (1!=imageVersionMngDAO.doInsertByBean(imageVersion)){
            logger.error("本地上传创建镜像插入数据库失败");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("IMAGE_UPLOADING_FAILURE"));
        }
    }


    /**
     * 插入本地上传镜像
     * @param path
     * @param imageVersion
     */
    public void finishLocalAdd(String path, ImageVersion imageVersion) {
        UploadFile uploadFile = new UploadFile();
        String fileSize = uploadFile.getFileSize(new File(path));
        imageVersion.setImageVersionSize(Float.parseFloat(fileSize));
        imageVersion.setImageStatus(CodeStatusUtil.getStatusByCodeEn("IMAGE_UPLOADING_SUCCESS").getCode());
        updateImageVersion(imageVersion);
    }

    /**
     * 统一处理更新异常
     * @param imageVersion
     */
    public void updateImageVersion(ImageVersion imageVersion) {
        if (1!=imageVersionMngDAO.doUpdateByBean(imageVersion)){
            logger.error("数据库更新镜像版本失败");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("IMAGE_MODIFY_FAILURE"));
        }
    }

    /**
     * 将本地服务器的镜像删除
     * 使用场景：
     * 1、镜像推送完成后，要删除本地镜像
     * 2、镜像拉取完成后，
     *
     * @param localTag 本地tag
     * @throws DockerCertificateException
     * @throws DockerException
     * @throws InterruptedException
     */
    public void removeImage(ImageVersion imageVersion,String localTag){
        try {
            DockerUtil.getDocker().removeImage(localTag);
        }catch (Exception e){
            //todo 区分处理不同异常
            logger.error("删除本地镜像失败{}",e.getMessage());
            imageVersion.setImageStatus(CodeStatusUtil.getStatusByCodeEn("IMAGE_DELLETE_FAILURE").getCode());
            updateImageVersion(imageVersion);
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("IMAGE_DELLETE_FAILURE"));
        }

    }

    public void addLocalPushToRegistry(ImageVersion imageVersion,String registryTag, RegistryUtil registryUtil){
//        String registryImageTag = registryUtil.getRegistryAddress() + registryTag;
        try {
            logger.info ("推入到仓库");
            // push到仓库
            DockerUtil.getDocker().push(registryTag, RegistryUtil.getRegistryAuth(registryUtil.getRegistryApiAddress()));
        }catch (Exception e){
            logger.error("push仓库失败:{}",e.getMessage());
            imageVersion.setImageStatus(CodeStatusUtil.getStatusByCodeEn("IMAGE_UPLOADING_FAILURE").getCode());
            updateImageVersion(imageVersion);
            throw new BusinessException(CodeStatusUtil.resultByCodeEn ("IMAGE_UPLOADING_FAILURE"));
        }
    }

    /**
     * 将镜像推送到镜像仓库
     * 流程:
     * 1、docker tag : 标记本地镜像，将其归入某一仓库。
     * 2、推送到指定的镜像仓库
     *
     * @param localImageId 本地的镜像id
     * @param registryTag  需要打的远程仓库tag
     * @param registryUtil 仓库地址和端口详细信息
     */
    public void pushToRegistry(String localImageId, String registryTag, RegistryUtil registryUtil)throws BusinessException{
        try {
            // 打tag
            logger.info("打tag");
            DockerUtil.getDocker().tag(localImageId, registryTag);
            logger.info ("推入到仓库");
            // push到仓库
            DockerUtil.getDocker().push(registryTag, RegistryUtil.getRegistryAuth(registryUtil.getRegistryApiAddress()));
        } catch (Exception e) {
            logger.error("push仓库失败:{}",e.getMessage());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn ("IMAGE_UPLOADING_FAILURE"));
        }
    }

    /**
     * 将镜像tar文件load到本地docker镜像,并返回镜像的Tag
     *
     * @param imageVersion
     * @return
     */

    public void load(ImageVersion imageVersion,String path,String registryTag) {
        try {
//            String tagLocal = "";
            File tarFileWithMultipleImages = new File(path);
            InputStream imagePayload = new BufferedInputStream(new FileInputStream(tarFileWithMultipleImages));
//            Set set = DockerUtil.getDocker().load(imagePayload);
////            Set set = DockerUtil.getDocker(Config.DOCKER_PATH).load(imagePayload);
//            for (Iterator it = set.iterator(); it.hasNext(); ) {
//                tagLocal = it.next().toString();
//            }
//            return tagLocal;
            DockerUtil.getDocker().create(registryTag,imagePayload);
        }catch (Exception e){
            logger.error("load镜像失败:{}",e.getMessage());
            imageVersion.setImageStatus(CodeStatusUtil.getStatusByCodeEn("IMAGE_BUILD_FAILURE").getCode());
            updateImageVersion(imageVersion);
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("IMAGE_BUILD_FAILURE"));
        }
    }

    /**
     * 生成仓库标签
     *
     * @param imageName    镜像名称
     * @param imageVersion 镜像版本
     * @return 仓库标签
     */
    public String getRegistryTag(String imageName, String imageVersion) {
        return imageName + ":" + imageVersion;
    }

    /**
     * 获取仓库详情
     * @param registryId   仓库id
     * @return
     */
    public RegistryDetail getRegistryDetail(Integer registryId) {
        RegistryDetail registryDetail = registryDetailDAO.doFindByRegistryId(registryId);
        if (null==registryDetail){
            logger.info("镜像创建失败，无该仓库");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn(""));
        }
        return  registryDetail;
    }

    /**
     * 上传镜像(tar包)
     *
     * @param multipartFile 多文本
     * @return
     */
    @Override
    public Result uploadImage(MultipartFile multipartFile) {
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_LOCAL_UPLOADING_FAILURE");
        UploadFile uploadFile = new UploadFile();
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
        if ("tar".equals(suffixName)) {
            String filePath = Config.UPLOAD_DIRECTORY_IMAGE;
            try {
                Result pathResult = uploadFile.uploadFile(multipartFile, filePath);
                Map<String,String> pathData = (Map)pathResult.getData();
                String uploadPath = pathData.get("path");
                Map<String, String> resultMap = new HashMap<>();
                resultMap.put("path", uploadPath);
                resultMap.put("uploadName", fileName);
                result = CodeStatusUtil.resultByCodeEn("IMAGE_LOCAL_UPLOAD_SUCCESS");
                result.setData(resultMap);
                return result;
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new BusinessException(result);
            }
        } else {
            logger.info("文件{}后缀不是tar",fileName);
            throw new BusinessException(result);
        }
    }

    /**
     * 插入镜像的详细信息
     *
     * @param imageVersionVO VO类对象
     * @return imageVersionId 镜像版本id
     */
    @Override
    public Result insertImageDetailInfo(String userId, ImageVersionVO imageVersionVO) throws Exception {
        String id = userId;
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_CREATE_FAIL");
        ImageDetail imageDetail = null;
        ImageVersion imageVersion = new ImageVersion();
        imageDetail = imageVersionVO.getImageDetail();
        try {
            BeanUtil.copyBean2Bean(imageVersion, imageVersionVO);
            long imageId = imageDetailDAO.doInsertByBean(imageDetail);
            logger.debug("imageId:" + imageDetail.getImageId());
            imageVersion.setImageId(imageDetail.getImageId());
            imageVersionMngDAO.doInsertByBean(imageVersion);
            result = CodeStatusUtil.resultByCodeEn("IMAGE_CREATE_SUCCESS");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return result;
        }
        return result;
    }

    /**
     * 分页查询
     *
     * @param userId
     * @param imageVersionVO
     * @return
     * @throws Exception
     */
    @Override
    public Result findDetailInfo(String userId, ImageVersionVO imageVersionVO) throws Exception {
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_QUERY_SUCCESS");
        String id = userId;
        int pageNum = imageVersionVO.getPageNum();
        int pageSize = imageVersionVO.getPageSize();
        logger.info("运行环境-------------------------");
        logger.info("pageNum" + pageNum + " pageSize:" + pageSize);
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<Integer> imageVersionIdList = new ArrayList<Integer>();
        List<ImageVersionVO> imageVersionVOS = imageVersionMngDAO.doFindDetail(imageVersionVO);
        for (ImageVersionVO imageVersionVO1 : imageVersionVOS) {
            imageVersionIdList.add(imageVersionVO1.getImageVersionId());
        }
        PageInfo pageInfo = PageUtil.getPageInfo(page, imageVersionIdList);
        pageInfo.setList(imageVersionVOS);
        result.setData(pageInfo);
        return result;
    }

    /**
     * 服务中选择镜像
     *
     * @param userId
     * @param imageVersionVO
     * @return
     * @throws Exception
     */
    @Override
    public Result findSrvDetailInfo(String userId, ImageVersionVO imageVersionVO) throws Exception {
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_QUERY_SUCCESS");
        String id = userId;
        int pageNum = imageVersionVO.getPageNum();
        int pageSize = imageVersionVO.getPageSize();
        logger.info("服务中选择镜像-------------------------");
        logger.info("pageNum" + pageNum + " pageSize:" + pageSize);
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<Integer> imageVersionIdList = new ArrayList<Integer>();
        List<ImageVersionVO> imageVersionVOS = imageVersionMngDAO.doFindSrvPage(imageVersionVO);
        for (ImageVersionVO imageVersionVO1 : imageVersionVOS) {
            imageVersionIdList.add(imageVersionVO1.getImageVersionId());
        }
        PageInfo pageInfo= PageUtil.getPageInfo(page,imageVersionIdList);
        pageInfo.setList (imageVersionVOS);
        result.setData (pageInfo);
        return result;
    }

    @Override
    public Result createVersionAndDockerfile(ImageVersionVO imageVersionVO){
        Result result = CodeStatusUtil.resultByCodeEn ("IMAGE_CREATE_FAIL");
        //1.校验版本是否唯一
        logger.info ("校验版本是否唯一");
        veryImageVersion.veryVersion(imageVersionVO);
        //2.创建dockerfile
        logger.debug ("创建dockerfile");
        DockerFileDetail dockerFileDetail = createDockerfile (imageVersionVO);
        //3.数据库插入镜像版本
        ImageVersion imageVersion = new ImageVersion ();
        try {
            BeanUtil.copyBean2Bean(imageVersion,imageVersionVO);
        } catch (Exception e) {
            logger.error ("copyBean2Bean(imageVersion,imageVersionVO)失败:{}",e.getMessage ());
            throw new BusinessException(result);
        }
        String basePkgVersionId = imageVersionVO.getDockerfileJson ().getString ("basePkgVersionId");
//        imageVersion.setBusiPkgVersionId (Integer.parseInt (basePkgVersionId));
        imageVersion.setCreator ("yht");
        imageVersion.setDockfileId (dockerFileDetail.getDockerfileId ());
        imageVersion.setImageUploadWay (4);
        imageVersion.setImageStatus (CodeStatusUtil.getInstance ().getStatusByCodeEn ("IMAGE_UNBUILD").getCode ());
        if (1!=imageVersionMngDAO.doInsertByBean (imageVersion)){
            logger.error("数据库插入镜像版本失败");
            throw new BusinessException(result);
        }
        //4、插入镜像版本规则
        imageVersionVO.setImageVersionId(imageVersion.getImageVersionId());
        if (1!=insertImageVersionRule(imageVersionVO)){
            logger.error("插入镜像版本规则失败");
            throw new BusinessException(result);
        }

        result = CodeStatusUtil.resultByCodeEn ("IMAGE_CREATE_SUCCESS");
        result.setData (imageVersion.getImageVersionId ());
        return result;
    }

    /**
     * 创建镜像版本规则
     * @param imageVersionVO
     * @return
     */
    private int insertImageVersionRule(ImageVersionVO imageVersionVO){
        JSONObject jsonObject = imageVersionVO.getImageVersionRuleJson ();
        Integer baseImageVersionId = imageVersionVO.getImageVersionId();
        JSONArray exposePortJsonArray = jsonObject.getJSONArray("exposePort");
        StringBuffer exposePortSb = new StringBuffer();
        for(int i = 0;i < exposePortJsonArray.size();i++){
            if(!"".equals(exposePortJsonArray.get(i))) {
                exposePortSb.append(exposePortJsonArray.get(i));
                exposePortSb.append(",");
            }
        }
        String exposePort = exposePortSb.substring(0,exposePortSb.length()-1).toString();
        JSONArray publishPathJsonArray = jsonObject.getJSONArray("publishPath");
        StringBuffer publishPathSb = new StringBuffer();
        for(int i = 0;i < publishPathJsonArray.size();i++){
            if(!"".equals(publishPathJsonArray.get(i))) {
                publishPathSb.append(publishPathJsonArray.get(i));
                publishPathSb.append(",");
            }
        }
        String publishPath = publishPathSb.substring(0,publishPathSb.length()-1).toString();
        logger.debug("镜像版本规则------------{}------","exposePort:"+exposePort+",publishPath:"+publishPath);
        ImageVersionRule imageVersionRule = new ImageVersionRule();
        imageVersionRule.setBaseImageVersionId(baseImageVersionId);
        imageVersionRule.setExposePort(exposePort);
        imageVersionRule.setPublishPath(publishPath);
        return imageVersionRuleDAO.doInsertImageVersionRule(imageVersionRule);
    }

    /**
     * 创建dockerfile
     * @param imageVersionVO
     * @return
     */
    private DockerFileDetail createDockerfile(ImageVersionVO imageVersionVO){
        JSONObject jsonObject = imageVersionVO.getDockerfileJson ();
        String content = jsonObject.getString ("content");
        String baseImageVersionId = jsonObject.getString ("baseImageVersionId");
        String basePkgVersionId = jsonObject.getString ("basePkgVersionId");
        String configureType = jsonObject.getString ("configureType");
        String dockerfile = jsonObject.toString ();
        DockerFileDetail dockerFileDetail = new DockerFileDetail ();
        dockerFileDetail.setCmd (dockerfile);
        dockerFileDetail.setContent (content);
        dockerFileDetail.setBaseImageVersionId (Integer.parseInt (baseImageVersionId));
        if(basePkgVersionId != null && !"".equals(basePkgVersionId)){
            dockerFileDetail.setBasePkgVersionId (Integer.parseInt (basePkgVersionId));
        }
        if(configureType != null && !"".equals(configureType)) {
            dockerFileDetail.setConfigureType(Integer.parseInt(configureType));
        }
        dockerFileDetail.setCreator ("skw");
        //插入dockerfile
        if(1!=dockerFileDetailDAO.doInsertByBean (dockerFileDetail)){
            logger.error("插入dockerfile失败");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn(""));
        }
        return dockerFileDetail;
    }
    /**
     * 根据镜像的id，获取镜像仓库中的镜像
     * ip:port/imageName:tag
     * @param imageVersionId 镜像id
     * @return
     * @throws Exception
     */
    @Override
    public Result getRemoteImageInfo(int imageVersionId) throws Exception {
        // 根据imageId，查找镜像的名称和版本
        ImageVersionVO imageVersionVO = imageVersionMngDAO.doFindDetailByImageId(imageVersionId);
        if (imageVersionVO == null){
            return CodeStatusUtil.resultByCodeEn ("IMAGE_QUERY_FAILURE");
        }
        String resultText = imageVersionVO.getRegistryDetail().getRegistryIp()+":"+imageVersionVO.getRegistryDetail().getRegistryPort()+"/"+imageVersionVO.getImageDetail().getImageNameEn()+":"+imageVersionVO.getImageVersion();
        logger.debug("result:"+resultText);
        Result result = CodeStatusUtil.resultByCodeEn ("IMAGE_QUERY_SUCCESS");
        result.setData(resultText);
        return result;
    }

    /**
     * 根据镜像版本的id，获取镜像版本的使用规则
     * ip:port/imageName:tag
     * @param imageVersionId 镜像id
     * @return
     * @throws Exception
     */
    @Override
    public Result getImageVersionRule(int imageVersionId) throws BusinessException {
        Result result;
        ImageVersionRule imageVersionRule = imageVersionRuleDAO.getImageVersionRuleByImageVersionId(imageVersionId);
        result = CodeStatusUtil.resultByCodeEn ("IMAGE_QUERY_SUCCESS");
        result.setData(imageVersionRule);
        return result;
    }

    /**
     *
     * @param remoteServerInfo
     * @return
     * @throws BusinessException
     */
    @Override
    public Result FtpDownload(String userId,RemoteServerInfo remoteServerInfo,int flag) throws BusinessException {
        Result result=CodeStatusUtil.resultByCodeEn("FTP_DOWN_LOAD_FAILURE");;
        logger.debug("------进入FTP文件下载service-------");
        //1.获取FTP下载参数
        String ip = remoteServerInfo.getIp();
        logger.debug("ip地址:{}",ip);
        int port = remoteServerInfo.getPort();
        logger.debug("端口号:{}",port);
        String user = remoteServerInfo.getUser();
        logger.debug("用户名:{}",user);
        String password = remoteServerInfo.getPassword();
        logger.debug("密码:{}",password);
        String  remoteFile = remoteServerInfo.getRemoteFile();
        logger.debug("远程地址:{}",remoteFile);
        //2.获取文件名
        String rFile=remoteFile.trim();
        String fileName=rFile.substring(rFile.lastIndexOf("/")+1);
        logger.debug("FTP上传文件:{}",fileName);
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        logger.debug("FTP上传文件后缀名:{}",suffix);
        //3.判断该文件是否是zip包
        if (!"zip".equals(suffix)) {
            logger.error("上传文件非zip文件！上传失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_FAILURE_TYPE"));
        }

        logger.debug("文件后缀名:{}", suffix);
        //String localFile = remoteServerInfo.getLocalFile();
        String localFile = Config.UPLOAD_DIRECTORY_IMAGE;
        localFile=localFile+ UUID.randomUUID() + "."+suffix;
        logger.debug("本地路径:{}",localFile);
        //5.建立连接获取文件
        FtpFileUtil ftpFileUtil = new FtpFileUtil();
        if( "success".equals(ftpFileUtil.fileDownLoad(ip, port, user, password, remoteFile, localFile))) {
            //6返回原文件名和现文件名
            result = boxDataFTP(fileName, UUID.randomUUID() + "." + suffix);
        }
        return result;
    }

//    @Override
//    public Result FtpUpload(String userId,ImageVersion imageVersion,int flag) throws Exception {
//        Result result = CodeStatusUtil.resultByCodeEn("FTP_UPLOAD_FAILURE");
//
//       /* imageVersion.setImageUploadWay(3);
//        imageVersion.setImagePath(serverConnectionDetail.getRemotePath());
//        imageVersion.setPort(serverConnectionDetail.getPort());
//        imageVersion.setPasswd(serverConnectionDetail.getPassword());
//        imageVersion.setUserName(serverConnectionDetail.getUser());
//        imageVersion.setHostIp(serverConnectionDetail.getIp());
//       ImageVersion imageVersion = new ImageVersion();
//        BeanUtil.copyBean2Bean(imageVersion,imageFTP);
//        imageVersion.setImageUploadWay(3);*/
//        RegistryDetail registryDetail=new RegistryDetail();
//        String localFile="";
//        String remoteFile="";
//        if(registryDetailDAO.doFindByRegistryId(imageVersion.getRegistryId())!=null){
//            registryDetail=registryDetailDAO.doFindByRegistryId(imageVersion.getRegistryId());
//            FtpFileUtil  ftpFileUtil=new FtpFileUtil();
//            ftpFileUtil.fileUpLoad(registryDetail.getRegistryIp(),registryDetail.getRegistryPort(),registryDetail.getUserName(),registryDetail.getPasswd(),localFile,remoteFile);
//            if(imageVersionMngDAO.doInsertByBean(imageVersion)==1){
//                result = CodeStatusUtil.resultByCodeEn("FTP_UP_LOAD_SUCCESS");}
//        };
//        //FTPFileUtils.fileUpLoad();
//
//        return result;
//    }

//    @Override
//    public Result ScpUpload(String userId, ImageSCP imageSCP, int flag) throws Exception {
//        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_SCP_UPLOAD_FAILURE");
//        RegistryDetail registryDetail = new RegistryDetail();
//        ImageVersion imageVersion=new ImageVersion();
//        //上传服务器的目标地址
//        String remotePath = imageSCP.getRemotePath();
//        //本地文件
//        String localFile = imageSCP.getLocalFile();
//        String ip=null;
//        int port=0;
//        String user=null;
//        String password=null;
//        BeanUtil.copyBean2Bean(imageVersion,imageSCP);
//        if(imageSCP.getRegistryId()!=0) {
//            if (registryDetailDAO.doFindByRegistryId(imageSCP.getRegistryId()) != null) {
//
//                registryDetail = registryDetailDAO.doFindByRegistryId(imageSCP.getRegistryId());
//                ip = registryDetail.getRegistryIp();
//                port = registryDetail.getRegistryPort();
//                user = registryDetail.getUserName();
//                password = registryDetail.getPasswd();
//            }
//        }
//           else{
//                ip ="10.1.163.69";
//                port = 22;
//                user = "root";
//                password ="!QAZ@WSX";
//            }
//            imageVersion.setImageUploadWay(3);
//            imageVersion.setImagePath(remotePath);
//            imageVersion.setPort(port);
//            imageVersion.setPasswd(password);
//            imageVersion.setUserName(user);
//            imageVersion.setHostIp(ip);
//            if (!"failure".equals(ScpUtil.upload(ip, port, user, password, localFile, remotePath, 1))) {
//                if(imageVersionMngDAO.doInsertByBean(imageVersion)==1) {
//                    result = CodeStatusUtil.resultByCodeEn("IMAGE_SCP_UPLOAD_SUCCESS");
//                    result.setData(ScpUtil.upload(ip, port, user, password, localFile, remotePath, 1));
//                }
//            }
//
//        return result;
//    }

    @Override
    public Result ScpDownload(String userId, RemoteServerInfo remoteServerInfo, int flag) throws BusinessException {
        Result result=CodeStatusUtil.resultByCodeEn("SCP_DOWNLOAD_FAILURE") ;
        String localFile;
        logger.debug("------进入FTP文件下载service-------");
        //1.获取SCP下载参数
        String ip = remoteServerInfo.getIp();
        logger.debug("ip地址:{}", ip);
        int port = remoteServerInfo.getPort();
        logger.debug("端口号:{}", port);
        String user = remoteServerInfo.getUser();
        logger.debug("用户名:{}", user);
        String password = remoteServerInfo.getPassword();
        logger.debug("密码:{}", password);
        String remoteFile = remoteServerInfo.getRemoteFile();
        logger.debug("远程地址:{}", remoteFile);
        //2.获取文件名
        String rFile = remoteFile.trim();
        String fileName = rFile.substring(rFile.lastIndexOf("/") + 1);
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        logger.debug("文件后缀名:{}", suffix);
        String path = Config.UPLOAD_DIRECTORY_IMAGE;
       //String path = remoteServerInfo.getLocalPath();
        logger.debug("本地路径:{}", path);
        //3.判断该文件是否是zip包
        if (!"zip".equals(suffix)) {
            logger.error("上传文件非zip文件！上传失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_FAILURE_TYPE"));
        }

        //4判断路径为Windows还是linux
        String separator = System.getProperty("file.separator");
        if ("".equals(path)) {
            if ("\\".equals(separator)) {
                path = com.cloud.paas.util.Config.WIN_DOWNLOAD_PATH;
            } else {
                path = com.cloud.paas.util.Config.Lin_DOWNLOAD_PATH;
            }
        }
        if("success".equals( ScpUtil.download(ip, port, user, password, remoteFile, path, flag))) {
            File file = new File(path + fileName);
            localFile = path + UUID.randomUUID() + "." + suffix;
            file.renameTo(new File(localFile));
            result = boxDataSCP(fileName, UUID.randomUUID() + "." + suffix);
        }
        return result;
    }
    /**
     * 创建并构建dockerfile
     *
     * @param imageVersionVO 镜像详情和对应版本信息
     * @return 是否成功
     */
    @Override
    public Result createAndBuild(ImageVersionVO imageVersionVO) {
        //创建dockerfile和镜像
        Result result = createVersionAndDockerfile (imageVersionVO);
        if (1==result.getSuccess ()){
            //构建并推入仓库
            buildAndPushToRegistry (Integer.parseInt (result.getData ().toString ()));
            result = CodeStatusUtil.resultByCodeEn ("IMAGE_CREATE_SUCCESS");
        }
        return result;
    }

    /**
     * 通过dockerfile生成镜像,并推送到镜像仓库
     * @param imageVersionId
     */
    @Override
    @Transactional
    public Result buildAndPushToRegistry(int imageVersionId) {
        logger.info("通过dockerfile生成镜像,并推送到镜像仓库");
        // 1.通过imageVersionId获取镜像详细信息
        logger.info ("通过imageVersionId获取镜像详细信息");
        ImageVersionVO imageVersionVO = imageVersionMngDAO.doFindDetailByImageId(imageVersionId);
        if (null==imageVersionVO){
            logger.error("镜像详细信息不存在");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("IMAGE_VERSION_QUERY_FAILURE"));
        }
        ImageVersion imageVersion = new ImageVersion();
        BeanUtil.copyBean2Bean(imageVersion, imageVersionVO);
        //2.设置镜像版本的状态
        logger.info("设置数据库的状态");
        imageVersion.setImageStatus(CodeStatusUtil.getStatusByCodeEn("IMAGE_BUILDING").getCode());
        updateImageVersion(imageVersion);
        //3.开始异步流程
        imageVersionAsyncService.buildAndPushToRegistryAsync(imageVersion,imageVersionVO);
        Result result=CodeStatusUtil.resultByCodeEn("IMAGE_BUILDING");
        return result;
    }

    @Override
    public Result refreshStatus(String userid, List<Long> imageIds) {
        List<ImageVersion>imageVersions=imageVersionMngDAO.getStatusByImageIds(imageIds);
        Result result= CodeStatusUtil.resultByCodeEn("IMAGE_QUERY_SUCCESS");
        result.setData(imageVersions);
        return result;
    }

    @Override
    public Result listImageVersionRuleByImageVersionId(String imageVersionId) {
        return null;
    }

    @Override
    public int doInsertImageVersionRule(ImageVersionRule imageVersionRule) {
        return 0;
    }

    @Override
    public Result doUpdateImageVersionRule(ImageVersionRule imageVersionRule) throws BusinessException {
        return null;
    }

    @Override
    public Result doDeleteImageVersionRuleByImageVersionId(int imageVersionId) throws BusinessException {
        return null;
    }

    /**
     * 查询服务镜像状态
     * @param srvImageVOS
     * @return
     * @throws BusinessException
     */
    @Override
    public Result querySrvImageStatus(List<SrvImageVO> srvImageVOS) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_VERSION_QUERY_FAILURE");
        //1、构造镜像状态查询返回数据
        JSONArray jsonArray = new JSONArray();
        for(SrvImageVO srvImageVO : srvImageVOS){
            if(srvImageVO.getSrvImageId() != null) {
                JSONObject jsonObject = new JSONObject();
                ImageVersion imageVersion = imageVersionMngDAO.doFindDetailByImageId(srvImageVO.getSrvImageId());
                jsonObject.put("srvVersionStatus", imageVersion.getImageStatus());
                jsonObject.put("srvVersionId", srvImageVO.getSrvVersionId());
                logger.debug("服务镜像编号为"+ srvImageVO.getSrvImageId() +"的镜像当前状态--------------------{}",imageVersion.getImageStatus());
                jsonArray.add(jsonObject);
            }
        }
        if(jsonArray.size() == 0){
            logger.debug("查询镜像版本无结果！");
            throw new BusinessException(result);
        }
        //2、返回参数
        result = CodeStatusUtil.resultByCodeEn("IMAGE_VERSION_QUERY_SUCCESS");
        result.setData(jsonArray);
        return result;
    }

    /**
     * 推入仓库准备工作
     * @param imageVersion
     */
    public String prepareToPush(RegistryUtil registryUtil, ImageVersion imageVersion) {
        if (registryUtil.getRegistryStatus ().longValue ()!=CodeStatusUtil.getStatusByCodeEn ("REGISTRY_START_SUCCESS").getCode ()){
            logger.error ("仓库未启动！");
            throw new BusinessException(CodeStatusUtil.resultByCode(registryUtil.getRegistryStatus ().intValue ()));
        }
        imageVersion.setImageStatus(CodeStatusUtil.getStatusByCodeEn ("IMAGE_UPLOADING").getCode ());
        updateImageVersion(imageVersion);
        ImageDetail imageDetail = imageDetailDAO.doFindById (imageVersion.getImageId ().intValue ());
        return getRegistryTag(imageDetail.getImageNameEn(), imageVersion.getImageVersion());
    }

    /**
     * 更新镜像大小
     * @param imageVersion
     * @param imageId
     */
    void setImageSize(ImageVersion imageVersion, String imageId) {
        float size = DockerUtil.getImageSize(imageId);
        imageVersion.setImageVersionSize(size);
        logger.info (imageVersion.getImageVersionId ()+"：设置构建成功状态");
        imageVersion.setImageStatus (CodeStatusUtil.getStatusByCodeEn("IMAGE_BUILD_SUCCESS").getCode());
        updateImageVersion(imageVersion);
    }

    /**
     * 通过registryId获取仓库地址的详细信息
     * @param registryId
     * @return
     */
    public RegistryUtil getRegistryAddressInfo(int registryId) {
        RegistryUtil registryUtil;
        RegistryDetail registryDetail = registryDetailDAO.doFindByRegistryId (registryId);
        if (null==registryDetail) {
            logger.error("仓库不存在");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_FAILURE"));
        }
        registryUtil = new RegistryUtil();
        BeanUtil.copyBean2Bean(registryUtil, registryDetail);
        return registryUtil;
    }
    /**
     * 准备生成镜像,针对有无业务包
     *
     * @param imageVersion   镜像版本详细信息
     * @param dockerFileDetail dockerfile详细信息
     * @return
     */
    public Result prepareToBuild(ImageVersion imageVersion, DockerFileDetail dockerFileDetail){
        Result result = CodeStatusUtil.resultByCodeEn ("IMAGE_BUILD_FAILURE");

        // 1.生成registryTag
        ImageDetail imageDetail = imageDetailDAO.doFindById (imageVersion.getImageId ().intValue ());
        String registryTag = getRegistryTag(imageDetail.getImageNameEn(), imageVersion.getImageVersion());
        //生成dockerfile
        String dockerFile= generateDockerfile(dockerFileDetail.getContent());
        //获取dockerfile所处位置
        logger.debug(dockerFile);
        String[] dockerFileNames=dockerFile.split("\\.");
        String dockerFilePath=dockerFileNames[0];
        String destPath=Config.TEMP_DIRECTORY_DOCKERFILE+dockerFilePath+"/";
        if (null != dockerFileDetail.getBasePkgVersionId ()) {
            // 有业务包
            //获取业务包的详细信息
            result = CodeStatusUtil.resultByCodeEn ("IMAGE_BUILD_SUCCESS");
            logger.debug ("有业务包-----------------------");
            logger.debug ("result的success值："+result.getSuccess ());
            BusiPkgVersionDetail busiPkgVersionDetail = busiPkgVersionMngDAO.doFindById(dockerFileDetail.getBasePkgVersionId ());
            //获取业务包的路径
            String budiPath = busiPkgVersionDetail.getPath();
            logger.debug ("业务包的路径："+budiPath);
            //解压到dockerfile同一目录下
            //解压压缩包
            try {
                ZipUtil.unZipFiles( budiPath, destPath);
            } catch (IOException e) {
                logger.error("解压业务包失败:{}",e.getMessage());
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("IMAGE_BUSI_UNZIP_FAILURE"));
            }
        }
        result.setData(buildToImage(registryTag, dockerFile,destPath));
        return result;
    }
    /**
     * build镜像
     *
     * @param registryTag       仓库tag
     * @param dockerFile        dockerfile名字
     * @param destPath          docckerfile路径
     * @return 生成的镜像id
     * @throws DockerCertificateException
     * @throws DockerException
     * @throws InterruptedException
     * @throws IOException
     *
     * 将生成dockerfile部分抽离，放到prepareBuild中
     * 1.25 zcy
     */
    private String buildToImage(String registryTag, String dockerFile,String destPath) {
        final AtomicReference<String> imageIdFromMessage = new AtomicReference<>();
        ProgressHandler progressHandler = new ProgressHandler() {
            @Override
            public void progress(ProgressMessage message) throws DockerException {
                logger.info("build:{}",message);
                if (message.error() != null) {
                    throw new DockerException(message.error());
                }
                final String imageId = message.buildImageId();
                if (imageId != null) {
                    imageIdFromMessage.set(imageId);
                }
            }
        };
        // 设置dockerfile的临时路径
        Path path = Paths.get(destPath);
        String build;
        try {
            build=DockerUtil.getDocker().build(path, registryTag, dockerFile, progressHandler);
        } catch (Exception e) {
            logger.error("镜像构建失败:{}",e.getMessage());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("IMAGE_BUILD_FAILURE"));
        }
        return build;
    }
    /**
     * 获取DockerFile详细信息
     *
     * @param imageVersionVO 镜像详情和版本信息
     * @return dockerfile详细信息
     */
    private DockerFileDetail getDockerFileDetail(ImageVersionVO imageVersionVO) {
        // 对查询的数据进行判断
        if (imageVersionVO != null) {
            // 获取dockerfile内容
            DockerFileDetail dockerFileDetail = dockerFileDetailDAO.doFindById(imageVersionVO.getDockfileId());
            System.out.println("dockerfile详细信息:" + dockerFileDetail.toJson());
            return dockerFileDetail;
        }
        return null;
    }

    /**
     * 通过dockerfile的字符串，生成dockerfile文件，并返回dockerfile文件的地址
     *
     * @param dockerfileContent dockerfile内容
     * @return 相关内容
     * @throws IOException
     *
     * dockerfile临时文件夹下增加一层目录，每个dockerfile及业务包放在单独目录下
     * 1.25 zcy
     */
    private String generateDockerfile(String dockerfileContent){
        if (null != dockerfileContent) {
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            //去掉“-”符号
            String dockerfileName = uuid + ".dockerfile";
            String dockerFileDir=Config.TEMP_DIRECTORY_DOCKERFILE+uuid;
            File dir=new File(dockerFileDir);
            dir.mkdirs();
            String dockerFilePath=dockerFileDir+"/"+dockerfileName;
            try {
                FileWriter writer = new FileWriter(dockerFilePath);
                writer.write(dockerfileContent);
                writer.flush();
                writer.close();
            }catch (Exception e){
                logger.error("生成dockerfile失败:{}",e.getMessage());
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("IMAGE_DOCKERFILE_CREATE_FAILURE"));
            }
            return dockerfileName;
        }
        else {
            logger.error("dockerfile内容为空");
            Result result=CodeStatusUtil.resultByCodeEn ("IMAGE_CREATE_FAILURE");
            throw new BusinessException(result);
        }
    }

    /**
     * 对FTP上传业务包数据返回包装
     * @param uploadName
     * @param path
     * @return
     */
    private Result boxDataFTP(String uploadName, String path) {
        Result result;
        //获取返回值里面数据
        Map map = new HashMap();
        map.put("path", path);
        logger.debug("上传生成的新文件名{}",path);
        map.put("uploadName", uploadName);
        logger.debug("上传的原文件名{}",uploadName);
        result = CodeStatusUtil.resultByCodeEn("FTP_DOWN_LOAD_SUCCESS");
        result.setData(map);
        return result;
    }

    /**
     * 对SCP上传业务包数据返回包装
     * @param uploadName
     * @param path
     * @return
     */
    private Result boxDataSCP(String uploadName, String path) {
        Result result;
        //获取返回值里面数据
        Map map = new HashMap();
        map.put("path", path);
        logger.debug("上传生成的新文件名{}",path);
        map.put("uploadName", uploadName);
        logger.debug("上传的原文件名{}",uploadName);
        result = CodeStatusUtil.resultByCodeEn("SCP_DOWNLOAD_SUCCESS");
        result.setData(map);
        return result;
    }

}
