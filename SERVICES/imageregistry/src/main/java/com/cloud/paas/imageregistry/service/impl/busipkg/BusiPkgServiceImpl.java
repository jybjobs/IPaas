package com.cloud.paas.imageregistry.service.impl.busipkg;

import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.imageregistry.dao.BaseDAO;
import com.cloud.paas.imageregistry.dao.BusiPkgMngDAO;
import com.cloud.paas.imageregistry.dao.BusiPkgVersionMngDAO;
import com.cloud.paas.imageregistry.dao.DockerFileDetailDAO;
import com.cloud.paas.imageregistry.model.BusiPkgDetail;
import com.cloud.paas.imageregistry.model.BusiPkgVersionDetail;
import com.cloud.paas.imageregistry.qo.BusiPkgExample;
import com.cloud.paas.imageregistry.service.busipkg.BusiPkgService;
import com.cloud.paas.imageregistry.service.impl.BaseServiceImpl;
import com.cloud.paas.imageregistry.util.Config;
import com.cloud.paas.imageregistry.vo.busipkg.BusiPkgListVO;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.ftp.FtpFileUtil;
import com.cloud.paas.util.page.PageUtil;
import com.cloud.paas.util.remoteinfo.RemoteServerInfo;
import com.cloud.paas.util.result.Result;
import com.cloud.paas.util.scp.ScpUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @Author: xujia
 * @Description: 业务包详细信息Service
 * @Date: 14:07 2017/11/24
 */
@SuppressWarnings({"serial", "rawtypes", "unchecked"})
@Service("busiPkgService")
public class BusiPkgServiceImpl extends BaseServiceImpl<BusiPkgDetail> implements BusiPkgService {
    private static final Logger logger = LoggerFactory.getLogger(BusiPkgServiceImpl.class);

    @Autowired
    private BusiPkgMngDAO busiPkgMngDAO;
    @Autowired
    private BusiPkgVersionMngDAO busiPkgVersionMngDAO;
    @Autowired
    DockerFileDetailDAO dockerFileDetailDAO;

    @Override
    public BaseDAO<BusiPkgDetail> getBaseDAO() {
        return busiPkgMngDAO;
    }

    /**
     * ftp上传
     */
//    public Result ftpUpLoadBusiPkgFile(String userid, RemoteServerInfo remoteServerInfo, int flag) throws BusinessException {
//        Result result = CodeStatusUtil.resultByCodeEn("FTP_UP_LOAD_FAILURE");
//        logger.debug("------进入FTP文件下载service-------");
//        String ip = remoteServerInfo.getIp();
//        logger.debug("ip地址:{}", ip);
//        int port = remoteServerInfo.getPort();
//        logger.debug("端口号:{}", port);
//        String user = remoteServerInfo.getUser();
//        logger.debug("用户名:{}", user);
//        String password = remoteServerInfo.getPassword();
//        logger.debug("密码:{}", password);
//        String remoteFile = remoteServerInfo.getRemoteFile();
//        String rFile = remoteFile.trim();
//        String fileName = rFile.substring(rFile.lastIndexOf("/") + 1);
//        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
//        //3.判断是否是zip包
//        if (!"zip".equals(suffix)) {
//            logger.error("上传文件非zip文件！上传失败！");
//            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_FAILURE_TYPE"));
//        }
//        logger.debug("文件后缀名:{}", suffix);
//        String localFile = remoteServerInfo.getLocalFile();
//        try {
//            FtpFileUtil ftpFileUtil = new FtpFileUtil();
//            result = ftpFileUtil.fileUpLoad(ip, port, user, password, localFile, remoteFile);
//        } catch (Exception e) {
//
//        }
//        return result;
//    }

    /**
     * ftp下载
     */
    public Result ftpDownLoadBusiPkgFile(String userId, RemoteServerInfo remoteServerInfo, int flag) throws BusinessException {
        Result result=CodeStatusUtil.resultByCodeEn("FTP_DOWN_LOAD_FAILURE");
        logger.debug("------进入FTP文件下载service-------");
        //1.获取FTP下载参数
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
        //3.判断该文件是否是zip包
        if (!"zip".equals(suffix)) {
            logger.error("上传文件非zip文件！上传失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_FAILURE_TYPE"));
        }
        logger.debug("文件后缀名:{}", suffix);
        //4.获取本地地址加文件名
        //String localFile = remoteServerInfo.getLocalFile();
        String localFile = Config.UPLOAD_DIRECTORY_BUSIPKG + userId + "/";
        String uuid = UUID.randomUUID().toString();
        localFile = localFile + uuid + "." + suffix;
        logger.debug("本地路径加文件名:{}", localFile);
        //5.建立连接获取文件
        FtpFileUtil ftpFileUtil = new FtpFileUtil();
        if( "success".equals(ftpFileUtil.fileDownLoad(ip, port, user, password, remoteFile, localFile))) {
            //6返回原文件名和现文件名
            result = boxDataFTP(fileName, uuid + "." + suffix);
        }
        return result;
    }

    /**
     * scp上传
     */
//    public Result scpUpLoadBusiPkgFile(String userId, RemoteServerInfo remoteServerInfo, int flag) throws BusinessException {
//        Result result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_SCP_UPLOAD_FAILURE");
//        String ip = remoteServerInfo.getIp();
//        int port = remoteServerInfo.getPort();
//        String user = remoteServerInfo.getUser();
//        String password = remoteServerInfo.getPassword();
//        //上传服务器的目标地址
//        String remotePath = remoteServerInfo.getRemotePath();
//        //本地文件
//        String localFile = remoteServerInfo.getLocalFile();
//        try {
//            if (!"failure".equals(ScpUtil.upload(ip, port, user, password, localFile, remotePath, 1))) {
//                result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_SCP_UPLOAD_SUCCESS");
//                result.setData(ScpUtil.upload(ip, port, user, password, localFile, remotePath, 1));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    /**
     * scp下载
     */
    public Result scpDownLoadBusiPkgFile(String userId, RemoteServerInfo remoteServerInfo, int flag) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("SCP_DOWNLOAD_FAILURE");
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
        String path = Config.UPLOAD_DIRECTORY_BUSIPKG + userId + "/";
        //String path = remoteServerInfo.getLocalPath();
        logger.debug("本地路径:{}", path);
        //3判断路径为Windows还是linux
        String separator = System.getProperty("file.separator");
        if ("".equals(path)) {
            if ("\\".equals(separator)) {
                path = com.cloud.paas.util.Config.WIN_DOWNLOAD_PATH;
            } else {
                path = com.cloud.paas.util.Config.Lin_DOWNLOAD_PATH;
            }
        }
        if("success".equals( ScpUtil.download(ip, port, user, password, remoteFile, path, flag))) {
            logger.debug("文件scp下载成功");
            File file = new File(path + ScpUtil.getZipName(fileName));
            suffix = "zip";
            String uuid = UUID.randomUUID().toString();
            localFile = path + uuid + "." + suffix;
            file.renameTo(new File(localFile));
            result = boxDataSCP(fileName, uuid + "." + suffix);
        }
        return result;
    }

    /**
     * 新建业务包
     */
    @Transactional
    public Result doInsertByBusiPkgDetailBean(BusiPkgDetail busiPkgDetail) throws Exception {
        Result result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_FAILURE");
        int row = busiPkgMngDAO.doInsertByBean(busiPkgDetail);
        if (row != 0) {
            result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_SUCCESS");
            result.setData(row);
        }
        return result;
    }

    /**
     * 删除平台上已经存在的业务包
     */
    @Transactional
    public Result deleteBusiPkgById(int busiPkgId) throws BusinessException {
        //1.检查是否被引用
        logger.info("检查业务包是否被引用【id：{}】", busiPkgId);
        checkReference(busiPkgId);
        //2.删除业务包表头
        logger.info("删除业务包！");
        if (busiPkgMngDAO.doDeleteById(busiPkgId) > 0) {
            logger.info("删除业务包成功！");
        } else {
            logger.error("删除业务包失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_DELETE_FAILURE"));
        }
        //3.删除业务包下的版本
        logger.info("删除业务包下的版本");
        int versionCount = busiPkgVersionMngDAO.countVersion(busiPkgId);
        if (busiPkgVersionMngDAO.doDeleteByPkgId(busiPkgId) == versionCount) {
            logger.info("删除版本成功！");
        } else {
            logger.error("删除业务包版本失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_DELETE_FAILURE"));
        }
        return CodeStatusUtil.resultByCodeEn("BUSI_PKG_DELETE_SUCCESS");

    }

    /**
     * 检查业务包是否被引用
     *
     * @param busiPkgId
     */
    private void checkReference(int busiPkgId) {

        //1.统计业务包下的版本信息
        logger.info("统计业务包【id：{}】版本信息",busiPkgId);
        List<BusiPkgVersionDetail> list = busiPkgVersionMngDAO.listFindByPkgId(busiPkgId);
        //2.查询各个版本引用信息
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                logger.debug("查询版本【id：{}】的引用信息", list.get(i).getBusiPkgVersionId());
                Integer countResult = dockerFileDetailDAO.CountByPkgVersionId(list.get(i).getBusiPkgVersionId());
                if (countResult > 0) {
                    logger.error("业务包【id：{}】被引用！", list.get(i).getBusiPkgVersionId());
                    throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_USED"));
                }
            }

        }
    }

    /**
     * 修改业务包的属性信息
     */
    @Transactional
    public Result doUpdateByBusiPkgBean(BusiPkgDetail busiPkgDetail) throws BusinessException {
        logger.info("修改业务包信息，id：{}，名称：{}", busiPkgDetail.getBusiPkgId(), busiPkgDetail.getBusiPkgNameZh());
        Result result;
        int row = busiPkgMngDAO.doUpdateByBean(busiPkgDetail);
        if (row != 0) {
            logger.info("修改业务包信息成功！");
            result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_MODIFY_SUCCESS");
        } else {
            logger.error("修改业务包信息失败！");
            result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_MODIFY_FAILURE");
        }
        return result;
    }

    /**
     * 获取指定用户下的指定业务包信息
     */
    public Result doFindByBusiPkgId(int busiPkgId) throws BusinessException {
        Result result;
        BusiPkgDetail busiPkgDetail = busiPkgMngDAO.doFindById(busiPkgId);
        if (busiPkgDetail != null) {
            result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_SUCCESS");
            result.setData(busiPkgDetail);
        } else {
            result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_FAILURE");
        }
        return result;
    }

    /**
     * 根据业务包id删除业务包详细信息
     *
     * @param id 业务包id
     * @return
     */
    @Override
    @Transactional
    public int doDeleteById(int id) throws IOException {
        int count = busiPkgVersionMngDAO.countVersion(id);
        logger.debug("业务包版本数量：" + count);
        if (count == 0) {
            return super.doDeleteById(id);
        }
        return 0;
    }

    /**
     * 查询业务包详细信息列表
     *
     * @return
     */
    @Override
    public Result listFindByPkgId() throws Exception {
        Result result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_FAILURE");
        List<BusiPkgDetail> busiPkgDetailList = busiPkgMngDAO.listBusipkg();
        if (busiPkgDetailList != null) {
            result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_SUCCESS");
            result.setMessage("获取业务包详情信息");
        }
        return result;
    }

    /**
     * busiPKgId条件业务包详情查询
     * params:busiPkgId
     * return 	List<BusiPkgListVO>
     */
    @Override
    public Result selectVersionVOList(int busiPkgId) throws Exception {
        Result result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_FAILURE");
        List<BusiPkgListVO> busiPkgListVOList = busiPkgMngDAO.selectVersionVOList(busiPkgId);
        if (busiPkgListVOList != null) {
            result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_SUCCESS");
            result.setData(busiPkgListVOList);
        }
        return result;
    }

    /**
     * 条件业务包详情查询
     * params:condition
     * return 	List<BusiPkgListVO>
     */
    @Override
    public List<BusiPkgListVO> selectVersionVOListByExample(BusiPkgExample busiPkgExample) {
        List<BusiPkgListVO> voList = busiPkgMngDAO.selectVersionVOListByExample(busiPkgExample);
        return voList;

    }

    /**
     * 分页
     */
    public List<Integer> findBusiPkgIdListByExample(BusiPkgExample busiPkgExample) {
        List<BusiPkgDetail> list = busiPkgMngDAO.findBusiPkgIdListByExample(busiPkgExample);
        BusiPkgDetail busiPkgDetail = null;
        List<Integer> listBkgId = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            busiPkgDetail = list.get(i);
            listBkgId.add(busiPkgDetail.getBusiPkgId());
        }
        return listBkgId;
    }

    /**
     * 分页查询
     */
//    public List<BusiPkgListVO>  findBusiPkgIdListByExampleResult(HashMap<String,Object> map){
//        return busiPkgMngDAO.findBusiPkgIdListByExampleResult(map);
//    }
    public Result findBusiPkgIdListByExampleResult(BusiPkgExample busiPkgExample) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_LIMIT_FAILURE");
        //1.获取页码及其每页数目
        int pageNum = busiPkgExample.getPageNum();
        int pageSize = busiPkgExample.getPageSize();
        logger.debug("------分页信息----");
        logger.debug("pageNum" + pageNum + " pageSize:" + pageSize);
        //2.设置分页条件
        Page page = PageHelper.startPage(pageNum, pageSize);
        //3.进行分页查询
        List<Integer> busiPkgIdList = this.findBusiPkgIdListByExample(busiPkgExample);
        //4.设置分页信息
        busiPkgExample.setIdList(busiPkgIdList);
        PageInfo pageInfo = PageUtil.getPageInfo(page, busiPkgIdList);
        busiPkgExample.setPageSize(0);
        busiPkgExample.setPageNum(0);
        //5.查询对应业务包版本
        List<BusiPkgListVO> list = busiPkgMngDAO.findBusiPkgIdListByExampleResult(busiPkgExample);
        //6.返回分页信息
        pageInfo.setList(list);
        if (null != pageInfo) {
            result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_QUERY_LIMIT_SUCCESS");
            result.setData(pageInfo);
        }
        return result;
    }

    /**
     * 检查业务包中文名是否重复
     *
     * @param busiPkgNameZh
     * @return
     */
    @Override
    public Result checkNameZh(String busiPkgNameZh) {
        int resultCount = busiPkgMngDAO.doFindByNameZh(busiPkgNameZh);
        if (resultCount > 0) {
            return CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_FAILURE_REPEATED_ZH");
        }
        return CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_SUCCESS_REPEATED_ZH");
    }

    /**
     * 检查业务包英文名是否重复
     *
     * @param busiPkgNameEn
     * @return
     */
    @Override
    public Result checkNameEn(String busiPkgNameEn) {
        int resultCount = busiPkgMngDAO.doFindByNameEn(busiPkgNameEn);
        if (resultCount > 0) {
            return CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_FAILURE_REPEATED_EN");
        }
        return CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_SUCCESS_REPEATED_EN");
    }

    /**
     * 对SCP上传业务包数据返回包装
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

