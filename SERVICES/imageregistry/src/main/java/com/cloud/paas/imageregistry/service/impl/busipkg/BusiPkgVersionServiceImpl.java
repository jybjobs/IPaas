package com.cloud.paas.imageregistry.service.impl.busipkg;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.imageregistry.dao.BaseDAO;
import com.cloud.paas.imageregistry.model.BusiPkgDetail;
import com.cloud.paas.imageregistry.service.busipkg.BusiPkgService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cloud.paas.imageregistry.dao.BusiPkgMngDAO;
import com.cloud.paas.imageregistry.dao.BusiPkgVersionMngDAO;
import com.cloud.paas.imageregistry.dao.DockerFileDetailDAO;
import com.cloud.paas.imageregistry.model.BusiPkgVersionDetail;
import com.cloud.paas.imageregistry.qo.BusiPkgExample;
import com.cloud.paas.imageregistry.service.busipkg.BusiPkgVersionService;
import com.cloud.paas.imageregistry.service.impl.BaseServiceImpl;
import com.cloud.paas.imageregistry.util.Config;
import com.cloud.paas.imageregistry.vo.busipkg.BusiPkgVersionDetailVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.bean.BeanUtil;
import com.cloud.paas.util.codestatus.CodeStatus;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.date.DateUtil;
import com.cloud.paas.util.fileupload.FileUtil;
import com.cloud.paas.util.fileupload.UploadFile;
import com.cloud.paas.util.page.PageUtil;
import com.cloud.paas.util.remoteinfo.RemoteServerInfo;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: css
 * @Description:业务版本服务接口实现类
 * @Date: Create in 10:30 2017/11/22
 * @Modified by:
 */
@SuppressWarnings({"serial", "rawtypes", "unchecked"})
@Service("busiPkgVersionService")
public class BusiPkgVersionServiceImpl extends BaseServiceImpl<BusiPkgVersionDetail> implements BusiPkgVersionService {

    private static final Logger logger = LoggerFactory.getLogger(BusiPkgVersionService.class);
    @Autowired
    BusiPkgMngDAO busiPkgMngDAO;
    @Autowired
    BusiPkgVersionMngDAO busiPkgVersionMngDao;
    @Autowired
    DockerFileDetailDAO dockerFileDetailDAO;
    @Autowired
    BusiPkgService busiPkgService;
    @Autowired
    BusiPkgAsyncService busiPkgAsyncService;

    /**
     * 删除指定业务版本
     */
    @Transactional
    public Result doDeleteByBusiPkgVersionId(int busiPkgVersionId) throws BusinessException {
        //1.检查是否被引用
        logger.info("检查业务包是否被引用【id:{}】", busiPkgVersionId);
        checkReference(busiPkgVersionId);
        //2.查询版本信息
        logger.info("查询版本信息");
        BusiPkgVersionDetail busiPkgVersionDetail = busiPkgVersionMngDao.selectByPrimaryKey(busiPkgVersionId);
        //3.查询业务包版本数
        logger.info("查询业务包版本数");
        Result result = countVersion(busiPkgVersionDetail.getBusiPkgId());
        //4.删除业务包版本
        logger.info("从数据库删除业务包版本【id:{}】", busiPkgVersionId);
        deleteVersion(busiPkgVersionDetail, result);
        return CodeStatusUtil.resultByCodeEn("BUSI_PKG_DELETE_SUCCESS");
    }

    /**
     * 删除版本
     *
     * @param busiPkgVersionDetail
     * @param result
     */
    private void deleteVersion(BusiPkgVersionDetail busiPkgVersionDetail, Result result) {
        //1.删除业务包版本

        if (busiPkgVersionMngDao.doDeleteById(busiPkgVersionDetail.getBusiPkgVersionId()) > 0) {
            logger.debug("删除业务包版本成功！【versionid：{}】", busiPkgVersionDetail.getBusiPkgVersionId());
        } else {
            logger.error("数据库删除版本失败！【id:{}】", busiPkgVersionDetail.getBusiPkgVersionId());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_DELETE_FAILURE"));
        }

        //2.更新业务包状态
        BusiPkgDetail busiPkgDetail = busiPkgMngDAO.doFindById(busiPkgVersionDetail.getBusiPkgId());
        BigDecimal size = busiPkgDetail.getBusiPkgSize();
        BigDecimal versionSize = busiPkgVersionDetail.getBusiPkgVersionSize();
        if (size.compareTo(versionSize) == 1) {
            //如果size>versionSize，size就减法version
            busiPkgDetail.setBusiPkgSize(size.subtract(versionSize));
        }
        if (busiPkgMngDAO.doUpdateByBean(busiPkgDetail) > 0) {
            logger.debug("更新业务包大小成功！");
        } else {
            logger.error("更新业务包大小失败");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_DELETE_FAILURE"));
        }

        //3.删除对应zip包
        logger.debug("业务包路径path:{}", busiPkgVersionDetail.getPath());
        if (busiPkgVersionDetail.getPath() != null && busiPkgVersionDetail.getPath() != "") {
            if (FileUtil.isFileExist(busiPkgVersionDetail.getPath())) {
                logger.info("删除对应业务包【path：{}】", busiPkgVersionDetail.getPath());
                try {
                    FileUtil.deleteFile(busiPkgVersionDetail.getPath());
                } catch (IOException e) {
                    logger.error("业务包删除失败！");
                    e.printStackTrace();
                    throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_DELETE_FAILURE"));
                }
            }
        }
    }

    /**
     * 检查是否被引用
     *
     * @param busiPkgVersionId
     */
    private void checkReference(int busiPkgVersionId) {
        Integer countResult = dockerFileDetailDAO.CountByPkgVersionId(busiPkgVersionId);
        if (countResult > 0) {
            logger.error("业务包【{}】正在被使用", busiPkgVersionId);
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_USED"));

        }
    }

    /**
     * 更新业务版本
     */
    @Override
    @Transactional
    public Result doUpdateByBusiPkgVersionBean(String userid, BusiPkgVersionDetail busiPkgVersionDetail) throws BusinessException {
        Result result;
        //1.设置基本信息
        logger.info("设置业务包版本:{}基本信息", busiPkgVersionDetail.getBusiPkgVersionId());
        busiPkgVersionDetail = setBusiPkgVersionParams(busiPkgVersionDetail, userid);
        //2.更新到数据库
        logger.info("更新数据库！");
        updateVersion(busiPkgVersionDetail, userid);
        //3.更新成功
        return CodeStatusUtil.resultByCodeEn("BUSI_PKG_MODIFY_SUCCESS");
    }

    private void updateVersion(BusiPkgVersionDetail busiPkgVersionDetail, String userid) {
        int row = busiPkgVersionMngDao.doUpdateByBean(busiPkgVersionDetail);
        if (row > 0) {
            logger.info("更新数据库成功！");
        } else {
            logger.error("更新数据库失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_MODIFY_FAILURE"));
        }
    }

    /**
     * 获取单表业务版本信息
     */
    public Result doFindByBusiPkgVersionId(int busiPkgVersionId) throws BusinessException {
        logger.info("查询业务包版本信息【id:{}】", busiPkgVersionId);
        BusiPkgVersionDetail busiPkgVersionDetail = busiPkgVersionMngDao.doFindById(busiPkgVersionId);
        if (busiPkgVersionDetail != null) {
            logger.debug("查询成功！");
            Result result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_SUCCESS");
            result.setData(busiPkgVersionDetail);
            return result;
        } else {
            logger.error("查询失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_FAILURE"));
        }
    }

    /**
     * 根据业务包编号集合查询业务包当前状态，并返回已上传的服务版本编号集合
     *
     * @param srvVersionInfos
     * @return
     * @throws BusinessException
     */
    @Override
    public Result findStatusByIdsReturnSrvVersionIds(JSONArray srvVersionInfos) throws BusinessException {
        Result result;
        JSONArray jsonArray = new JSONArray();
        //1.循环遍历请求中的服务信息（key-value => 服务版本编号-业务包版本编号）
        logger.debug("1.循环遍历请求中的服务信息（key-value => 服务版本编号-业务包版本编号）");
        for (Object object : srvVersionInfos) {
            JSONObject srvVersionInfo = (JSONObject) object;
            //2.获取服务版本信息和业务包版本信息的key-value对
            Iterator<String> iter = srvVersionInfo.keySet().iterator();
            while (iter.hasNext()) {
                String srvVersionId = iter.next();
                logger.debug("获取到服务版本编号---------------{}",srvVersionId);
                Object id = srvVersionInfo.get(srvVersionId);
                if (id != null) {
                    Integer busiPkgVersionId = Integer.parseInt(id.toString());
                    logger.debug("获取到业务包版本的编号---------------{}",busiPkgVersionId);
                    //3.获取业务包版本信息，筛选最终状态的业务包版本
                    BusiPkgVersionDetail busiPkgVersionDetail = busiPkgVersionMngDao.doFindById(busiPkgVersionId);
                    //业务包不处于中间状态1061100上传中1061120未上传状态
                    if (busiPkgVersionDetail != null && busiPkgVersionDetail.getBusiPkgStatus() != 1061120 && busiPkgVersionDetail.getBusiPkgStatus() != 1061100) {
                        logger.debug("获取到业务包版本的状态---------------{}",busiPkgVersionDetail.getBusiPkgStatus());
                        logger.debug("筛掉中间状态的业务包版本---------------{}",busiPkgVersionId);
                        JSONObject jsonObject = new JSONObject();
                        //4.查询状态码信息，反馈状态简码
                        CodeStatus codeStatus = CodeStatusUtil.getStatusByCode(busiPkgVersionDetail.getBusiPkgStatus());
                        if(codeStatus != null && codeStatus.getProcessCode() != null){
                            logger.debug("状态简码为------------{}",codeStatus.getProcessCode());
                            jsonObject.put(srvVersionId,codeStatus.getProcessCode());
                            jsonArray.add(jsonObject);
                        }
                    }
                }
            }
        }
        logger.debug("返回最终状态的业务包版本信息，key-value => 服务版本编号-业务包版本状态",jsonArray.toJSONString());
        result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_SUCCESS");
        result.setData(jsonArray.toJSONString());
        return result;
    }


    /**
     * 插入业务包版本
     *
     * @param busiPkgVersionDetail
     * @param userid
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Result doInsertByBean(BusiPkgVersionDetail busiPkgVersionDetail, String userid) throws BusinessException {
        Result result;
        BusiPkgExample busiPkgExample = new BusiPkgExample();
        //复制属性
        BeanUtil.copyBean2Bean(busiPkgExample, busiPkgVersionDetail);
        //0.校验业务包是否存在
        logger.info("校验业务包是否存在【id：{}】", busiPkgExample.getBusiPkgId());
        checkPkg(busiPkgExample.getBusiPkgId());
        //1、进行版本校验 如果已经存在直接抛出异常
        logger.info("进行版本是否重复校验");
        checkVersion(busiPkgExample);
        //2、增加业务包版本信息到数据库
        logger.info("增加业务包版本信息到数据库");
        insertPkgVersion(busiPkgVersionDetail);
        //3、启动一个新线程去拉取远端服务器的业务包（本地上传无需拉取），更新业务包信息
        busiPkgAsyncService.doInsertBusiPkgAsync(busiPkgVersionDetail, userid);
        logger.info("业务包版本新增成功！");
        result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_SUCCESS");
        result.setSuccess(1);
        Map map = new HashMap();
        map.put("busiPkgVersionId", busiPkgVersionDetail.getBusiPkgVersionId());
        map.put("busiPkgVersion", busiPkgVersionDetail.getBusiPkgVersion());
        result.setData(map);
        return result;
    }

    @Override
    @Transactional
    public Result insertVersionByBusiPkgId(String userid,Integer busiPkgId) throws BusinessException {
        Result result;
        BusiPkgExample busiPkgExample = new BusiPkgExample();
        //构建一个新的版本
        BusiPkgVersionDetail busiPkgVersionDetail = buildVersion(busiPkgId);
        //复制属性
        BeanUtil.copyBean2Bean(busiPkgExample, busiPkgVersionDetail);
        //0.校验业务包是否存在
        logger.info("校验业务包是否存在【id：{}】", busiPkgExample.getBusiPkgId());
        checkPkg(busiPkgExample.getBusiPkgId());
        //1、进行版本校验 如果已经存在直接抛出异常
        logger.info("进行版本是否重复校验");
        checkVersion(busiPkgExample);
        //2、增加业务包版本信息到数据库
        logger.info("增加业务包版本信息到数据库");
        insertPkgVersion(busiPkgVersionDetail);
        //3、启动一个新线程去拉取远端服务器的业务包（本地上传无需拉取），更新业务包信息
        busiPkgAsyncService.doInsertBusiPkgAsync(busiPkgVersionDetail, userid);
        logger.info("业务包版本新增成功！");
        result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_SUCCESS");
        result.setSuccess(1);
        Map map = new HashMap();
        map.put("busiPkgVersionId",busiPkgVersionDetail.getBusiPkgVersionId());
        map.put("busiPkgVersion",busiPkgVersionDetail.getBusiPkgVersion());
        result.setData(map);
        return result;
    }

    /**
     * 构建新的业务包版本
     *
     * @param busiPkgId
     * @return BusiPkgVersionDetail
     */
    private BusiPkgVersionDetail buildVersion(Integer busiPkgId) {
        BusiPkgVersionDetail busiPkgVersionDetail = new BusiPkgVersionDetail();
        busiPkgVersionDetail.setBusiPkgId(busiPkgId);
        busiPkgVersionDetail.setBusiPkgVersion(new SimpleDateFormat("yyMMddHHmmss").format(new Date()));
        BusiPkgDetail busiPkgDetail = busiPkgMngDAO.doFindById(busiPkgId);
        busiPkgVersionDetail.setUploadWay(busiPkgDetail.getUploadWay());
        return busiPkgVersionDetail;
    }

    /**
     * 验证业务包是否存在
     *
     * @param busiPkgId
     */
    private void checkPkg(Integer busiPkgId) {
        if (busiPkgId == null) {
            logger.error("业务包id为空，插入失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_FAILURE_PKGIDNULL"));
        }
        BusiPkgDetail busiPkgDetail = busiPkgMngDAO.doFindById(busiPkgId);
        if (busiPkgDetail == null) {
            logger.error("业务包不存在【id：】", busiPkgId);
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_FAILURE_NULL"));
        }

    }

    /**
     * 业务包版本插入数据库
     *
     * @param busiPkgVersionDetail
     */
    private void insertPkgVersion(BusiPkgVersionDetail busiPkgVersionDetail) {
        Integer BUSI_PKG_UPLOADING = CodeStatusUtil.getInstance().getStatusByCodeEn("BUSI_PKG_UPLOADING").getCode();
        //上传状态 1061100:业务包正在上传中
        int status = BUSI_PKG_UPLOADING;
        //设置文件上传状态为上传中
        busiPkgVersionDetail.setBusiPkgStatus(status);
        //设置版本初始化大小为0M
        busiPkgVersionDetail.setBusiPkgVersionSize(new BigDecimal("0"));
        if (doInsertByBean(busiPkgVersionDetail) > 0) {
            logger.info("业务包版本数据库插入成功");
        } else {
            logger.error("业务包版本数据库插入失败");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_FAILURE"));
        }
    }

    /**
     * 更新业务包基本信息的业务包大小合计
     *
     * @param busiPkgId
     * @param busiPkgVersionSize
     */
    protected void updateBusiPkgDetailBusiPkgSize(Integer busiPkgId, BigDecimal busiPkgVersionSize) {
        logger.debug("------------新业务包大小：{}----------", busiPkgVersionSize);
        BusiPkgDetail busiPkgDetail = busiPkgMngDAO.doFindById(busiPkgId);
        BigDecimal busiPkgSize = busiPkgDetail.getBusiPkgSize();
        logger.debug("------------更新前业务包总大小：{}----------", busiPkgSize);
        busiPkgSize = busiPkgSize.add(busiPkgVersionSize);
        logger.debug("------------更新后业务包总大小：{}----------", busiPkgSize);
        busiPkgDetail.setBusiPkgSize(busiPkgSize);
        if (busiPkgMngDAO.doUpdateByBean(busiPkgDetail) > 0) {
            logger.info("业务包总大小更新成功！");
        } else {
            logger.error("业务包总大小更新失败");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_FAILURE"));
        }
    }

    /**
     * 文件校验
     *
     * @param busiPkgVersionDetail
     * @param userid
     */
    private BusiPkgVersionDetail checkFileAndSetVerion(BusiPkgVersionDetail busiPkgVersionDetail, String userid) {
        String path = busiPkgVersionDetail.getPath();
        BigDecimal busiPkgVersionSize;
        String busiPkgPostfix;
        //上传状态 BUSI_PKG_WITHOUTUPLOAD:未上传 BUSI_PKG_UPLOAD_SUCCESS:已上传
        //1、文件路径校验
        if (path != null && path.length() != 0) {
            //2、文件类型校验 zip
            File file = new File(path);
            //3、文件是否存在
            if (!file.exists()) {
                logger.error("业务包不存在！");
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_FAILURE_NULL"));
            }
            UploadFile uploadFile = new UploadFile();
            busiPkgVersionSize = new BigDecimal(uploadFile.getFileSize(file));
            if (busiPkgVersionSize == null || busiPkgVersionSize.equals(BigDecimal.ZERO)) {
                //文件为空
                logger.error("业务包为空！");
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_FAILURE_EMPTY"));
            }
            busiPkgPostfix = uploadFile.getSuffixName(file);
        } else {
            logger.error("文件路径错误，不能为空，创建版本失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_VERSION_PATH_NULL"));
        }
        busiPkgVersionDetail.setBusiPkgVersionSize(busiPkgVersionSize);
        busiPkgVersionDetail.setBusiPkgPostfix(busiPkgPostfix);
        return busiPkgVersionDetail;
    }


    @Override
    public BaseDAO<BusiPkgVersionDetail> getBaseDAO() {
        return busiPkgVersionMngDao;
    }

    /**
     * 获取指定业务包下的版本列表
     *
     * @param pkgId
     * @return List<BusiPkgVersionDetail>接收BusiPkgVersionDetailVO子类的版本列表
     */
    @Override
    public Result listFindByPkgId(int pkgId) throws BusinessException {
        logger.info("查询业务包【id：{}】下版本信息", pkgId);
        List<BusiPkgVersionDetail> busiPkgVersionDetailList = busiPkgVersionMngDao.listFindByPkgId(pkgId);
        if (busiPkgVersionDetailList != null) {
            logger.debug("查询成功！");
            Result result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_SUCCESS");
            result.setData(busiPkgVersionDetailList);
            return result;
        } else {
            logger.error("查询失败");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_FAILURE"));
        }

    }

    /**
     * 业务包查询条件搜索信息 版本+基本信息结构
     *
     * @param busiPkgExample
     * @return List<BusiPkgVersionDetailVO>业务包查询条件搜索信息
     */
    @Override
    public Result listFindByExample(BusiPkgExample busiPkgExample) throws BusinessException {
        int pageNum = busiPkgExample.getPageNum();
        int pageSize = busiPkgExample.getPageSize();
        logger.debug("pageNum" + pageNum + " pageSize:" + pageSize);
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<BusiPkgVersionDetailVO> list = busiPkgVersionMngDao.listFindByExample(busiPkgExample);
        PageInfo pageInfo = PageUtil.getPageInfo(page, list);
        pageInfo.setList(list);
        if (null != pageInfo) {
            logger.info("查询成功");
            Result result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_SUCCESS");
            result.setData(pageInfo);
            return result;
        } else {
            logger.error("业务包版本查询失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_FAILURE"));
        }
    }

    /**
     * 上传业务包
     *
     * @param file 本地业务包文件
     * @return
     */
    @Override
    public Result uploadFile(MultipartFile file, String userid) throws BusinessException {
        Result result;
        logger.debug("------进入文件上传service-------");
        //1.获取文件名
        String fileName = file.getOriginalFilename();
        logger.debug("文件名:{}", fileName);
        //2.获取后缀名
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        logger.debug("文件后缀名:{}", suffix);
        //3.判断是否是zip包
        if (!"zip".equals(suffix)) {
            logger.error("上传文件非zip文件！上传失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_FAILURE_TYPE"));
        }
        //4.非空校验
        if (file.isEmpty()) {
            logger.error("上传文件为空！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_FAILURE_EMPTY"));
        }
        //5.文件上传
        UploadFile uploadFilePkg = new UploadFile();
        String filePath = Config.UPLOAD_DIRECTORY_BUSIPKG + userid + "/";
        Result busiPkgPathResult = uploadFilePkg.uploadFile(file, filePath);
        //6.获取文件名称
        String uploadName = uploadFilePkg.getOriginalFileName(file);
        //7.数据打包
        result = boxData(uploadName, busiPkgPathResult);
        return result;
    }

    /**
     * 对上传业务包数据返回包装
     *
     * @param uploadName
     * @param busiPkgPathResult
     * @return
     */
    private Result boxData(String uploadName, Result busiPkgPathResult) {
        Result result;
        //获取返回值里面数据
        Map data = (HashMap) busiPkgPathResult.getData();
        Map map = new HashMap();
        map.put("path", data.get("path"));
        map.put("uploadName", uploadName);
        result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_FILE_SUCCESS");
        result.setData(map);
        return result;
    }

    /**
     * 上传图片
     *
     * @param img    本地图片
     * @param userid 用户id
     * @return 图片存放路径
     */
    @Override
    public Result uploadImg(MultipartFile img, String userid) throws BusinessException {
        logger.info("进入上床图片程序！");
        Result result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_IMG_FAILURE");
        UploadFile uploadFile = new UploadFile();
        //1.获取图片存储路径
        String imgPath = Config.UPLOAD_DIRECTORY_BUSIPKG_IMG + userid + "/";
        logger.debug("图片存储路径为：{}", imgPath);
        Result busiPkgImgPathResult = uploadFile.uploadFile(img, imgPath);
        //2.判断是否成功
        if (0 == busiPkgImgPathResult.getSuccess()) {
            logger.error("图片上传失败！message：{}", busiPkgImgPathResult.getMessage());
            return busiPkgImgPathResult;
        }
        //3.数据打包
        Map data = (HashMap) busiPkgImgPathResult.getData();
        Map map = new HashMap();
        map.put("path", data.get("fileName"));
        if (!map.isEmpty()) {
            result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_IMG_SUCCESS");
            result.setData(map);
        }
        return result;
    }

    /**
     * 直接新建业务包
     *
     * @param userid                 用户id
     * @param busiPkgVersionDetailVO 业务版本+业务信息结构的VO
     * @return 记录改变数目
     */
    @Transactional
    @Override
    public Result insertPkgAndVersion(String userid, BusiPkgVersionDetailVO busiPkgVersionDetailVO) throws BusinessException {
        //0.对象拆分
        BusiPkgDetail busiPkgDetail = busiPkgVersionDetailVO.getBusiPkgDetail();
        //初始化业务包大小为0
        busiPkgDetail.setBusiPkgSize(new BigDecimal("0"));
        //1.中文名唯一性校验
        logger.info("校验中文名【{}】唯一性", busiPkgDetail.getBusiPkgNameZh());
        validateNameZh(busiPkgDetail.getBusiPkgNameZh());

        //2.英文名校验
        logger.info("校验英文名【{}】唯一性", busiPkgDetail.getBusiPkgNameEn());
        validateNameEn(busiPkgDetail.getBusiPkgNameEn());

        //设置创建人
        //TODO 与用户中心集成后 设置创建人
        busiPkgDetail.setCreator(Config.Creator_Admin);
        //busiPkgDetail.setBusiPkgImgPath(imgPath);TODO

        //3.插入业务包信息
        logger.info("插入业务包信息");
        insertPkg(busiPkgDetail);

        return CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_SUCCESS");
    }

    /**
     * 插入业务包版本信息
     *
     * @param busiPkgDetail
     * @param busiPkgVersionDetail
     */
    private void insertVersion(BusiPkgDetail busiPkgDetail, BusiPkgVersionDetail busiPkgVersionDetail) {
        busiPkgVersionDetail.setBusiPkgId(busiPkgDetail.getBusiPkgId());
        int resultPkgVersion = busiPkgVersionMngDao.doInsertByBean(busiPkgVersionDetail);
        if (resultPkgVersion == 1) {
            logger.info("插入业务包版本成功！id:{}", busiPkgDetail.getBusiPkgId());
        } else {
            //插入失败
            logger.error("插入业务包及其版本失败！");
            Result result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_FAILURE");
            throw new BusinessException(result);
        }
    }

    /**
     * 插入业务包信息
     *
     * @param busiPkgDetail
     */
    private void insertPkg(BusiPkgDetail busiPkgDetail) {
        int resultPkg = busiPkgMngDAO.doInsertByBean(busiPkgDetail);
        if (resultPkg == 0) {
            logger.error("插入业务包信息失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_FAILURE_INSERTPKG"));
        }
    }

    /**
     * 英文名校验
     *
     * @param busiPkgNameEn
     */
    private void validateNameEn(String busiPkgNameEn) {
        Result result = busiPkgService.checkNameEn(busiPkgNameEn);
        if (result.getSuccess() == 0) {
            logger.error("英文名已存在");
            throw new BusinessException(result);
        }
    }

    /**
     * 中文名校验
     *
     * @param busiPkgNameZh
     */
    private void validateNameZh(String busiPkgNameZh) {
        Result result = busiPkgService.checkNameZh(busiPkgNameZh);
        if (result.getSuccess() == 0) {
            logger.error("中文名已存在");
            throw new BusinessException(result);
        }
    }

    /**
     * 对业务包版本的路径、大小、上传状态参数进行设置
     *
     * @param busiPkgVersionDetail
     * @param userid
     * @return BusiPkgVersionDetail
     */
    protected BusiPkgVersionDetail setBusiPkgVersionParams(BusiPkgVersionDetail busiPkgVersionDetail, String userid) {
        String path = busiPkgVersionDetail.getPath();
        BigDecimal versionSize = new BigDecimal(0);
        String postfix = "";
        Integer BUSI_PKG_UPLOAD_FAILURE = CodeStatusUtil.getInstance().getStatusByCodeEn("BUSI_PKG_UPLOAD_FAILURE").getCode();
        Integer BUSI_PKG_UPLOAD_SUCCESS = CodeStatusUtil.getInstance().getStatusByCodeEn("BUSI_PKG_UPLOAD_SUCCESS").getCode();
        Integer BUSI_PKG_UPLOADING = CodeStatusUtil.getInstance().getStatusByCodeEn("BUSI_PKG_UPLOADING").getCode();
        Integer BUSI_PKG_WITHOUTUPLOAD = CodeStatusUtil.getInstance().getStatusByCodeEn("BUSI_PKG_WITHOUTUPLOAD").getCode();
        //上传状态 1061100:业务包正在上传中 1061120:业务包未上传 1061200:业务包上传成功 1061300:业务包上传失败
        int status = BUSI_PKG_WITHOUTUPLOAD;
        //设置文件大小、文件类型、上传状态
        if (path != null && path.length() != 0) {
            String filePath = Config.UPLOAD_DIRECTORY_BUSIPKG + userid + "/" + path;
            File file = new File(filePath);
            //判断文件是否存在
            if (file.exists()) {
                //设置文件路径
                busiPkgVersionDetail.setPath(filePath);
                UploadFile uploadFile = new UploadFile();
                //获取文件大小
                versionSize = new BigDecimal(uploadFile.getFileSize(file));
                //设置文件大小
                busiPkgVersionDetail.setBusiPkgVersionSize(versionSize);
                //获取文件后缀名
                postfix = uploadFile.getSuffixName(file);
                busiPkgVersionDetail.setBusiPkgPostfix(postfix);
                //设置文件上传状态
                status = BUSI_PKG_UPLOAD_SUCCESS;
                busiPkgVersionDetail.setBusiPkgStatus(status);
                //设置引用状态
                busiPkgVersionDetail.setBusiPkgReference(0);
                //设置创建者
                busiPkgVersionDetail.setCreator(Config.Creator_Admin);
            } else {
                //路径参数错误，文件不存在
                logger.error("文件路径：{}错误！没有找到文件！", filePath);
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_FAILURE_FILE_NULL"));
            }
        } else {
            //没有路径参数则设置为未上传
            busiPkgVersionDetail.setBusiPkgStatus(status);
            //设置文件大小为0
            busiPkgVersionDetail.setBusiPkgVersionSize(versionSize);
            //设置文件后缀名为""
            busiPkgVersionDetail.setBusiPkgPostfix(postfix);
            //设置引用状态
            busiPkgVersionDetail.setBusiPkgReference(0);
            //设置创建者
            busiPkgVersionDetail.setCreator(Config.Creator_Admin);
        }

        return busiPkgVersionDetail;

    }

    @Override
    @Transactional
    public int doUpdateByBean(BusiPkgVersionDetail bean) {

        DateUtil dateUtil = new DateUtil();
        //获取当前时间
        Date date = dateUtil.getCurrentTime();
        bean.setUpdateTime(date);
        return super.doUpdateByBean(bean);
    }

    @Override
    @Transactional
    public int doDeleteById(int id) throws IOException {

        BusiPkgVersionDetail busiPkgVersionDetail = busiPkgVersionMngDao.selectByPrimaryKey(id);
        if (busiPkgVersionDetail == null) {
            return 0;
        }
        String path = busiPkgVersionDetail.getPath();
        Integer reference = busiPkgVersionDetail.getBusiPkgReference();
        //如果未被引用就删除版本
        if (reference == 0) {
            Integer busiPkgId = busiPkgVersionDetail.getBusiPkgId();
            int count = busiPkgVersionMngDao.countVersion(busiPkgId);
            //版本删除的时候文件一并删除
            if (count == 1) {
                super.doDeleteById(id);
                logger.info("业务包版本删除成功");
                busiPkgMngDAO.doDeleteById(busiPkgId);
                logger.info("业务包基本信息删除成功");
                if (" ".equals(path)) {
                    FileUtil.deleteFile(path);
                    logger.info("业务包文件删除成功");
                }
                return 1;
            } else if (count > 1) {
                BusiPkgDetail busiPkgDetail = busiPkgMngDAO.doFindById(busiPkgId);
                BigDecimal size = busiPkgDetail.getBusiPkgSize();
                BigDecimal versionSize = busiPkgVersionDetail.getBusiPkgVersionSize();
                if (size.compareTo(versionSize) == 1) {
                    //如果size>versionSize，size就减法version
                    busiPkgDetail.setBusiPkgSize(size.subtract(versionSize));
                }
                busiPkgMngDAO.doUpdateByBean(busiPkgDetail);
                super.doDeleteById(id);
                logger.info("业务包版本删除成功");
                if (" ".equals(path)) {
                    FileUtil.deleteFile(path);
                    logger.info("业务包文件删除成功");
                }
                return 1;
            }
        }
        return 0;
    }

    @Override
    public Result countVersion(int busiPkgId) {
        logger.debug("统计业务包下版本个数");
        Result result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_SUCCESS_COUNT");
        result.setData(busiPkgVersionMngDao.countVersion(busiPkgId));
        return result;
    }


    @Override
    public Result checkVersion(BusiPkgExample busiPkgExample) {
        String busiPkgVersion = busiPkgExample.getBusiPkgVersion();
        logger.debug("校验版本号为：{}", busiPkgVersion);
        int resultCount = busiPkgVersionMngDao.countByVersion(busiPkgExample);
        if (resultCount > 0) {
            //版本号重复，抛出异常
            logger.error("版本号：{}已使用！", busiPkgVersion);
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_VERSION_EXISTED"));
        } else {
            logger.debug("业务包版本可用");
            return CodeStatusUtil.resultByCodeEn("BUSI_PKG_VERSION_CANUSE");
        }
    }

    @Override
    public Result refreshStatus(String userid, List<Long> pkgIds) {
        List<BusiPkgVersionDetail> busiPkgVersionDetails = busiPkgVersionMngDao.getStatusByPkgIds(pkgIds);
        Result result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_SUCCESS");
        result.setData(busiPkgVersionDetails);
        return result;
    }

}
