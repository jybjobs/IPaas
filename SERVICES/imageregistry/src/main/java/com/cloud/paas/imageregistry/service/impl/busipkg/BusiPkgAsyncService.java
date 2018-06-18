package com.cloud.paas.imageregistry.service.impl.busipkg;

import com.cloud.paas.imageregistry.model.BusiPkgDetail;
import com.cloud.paas.imageregistry.model.BusiPkgVersionDetail;
import com.cloud.paas.imageregistry.service.busipkg.BusiPkgService;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.remoteinfo.RemoteServerInfo;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by 17798 on 2018/3/15.
 */
@Service
public class BusiPkgAsyncService {
    private static final Logger logger = LoggerFactory.getLogger(BusiPkgAsyncService.class);

    @Autowired
    private BusiPkgVersionServiceImpl busiPkgVersionService;
    @Autowired
    private BusiPkgService busiPkgService;

    /**
     * 线程
     * 从远端服务器拉取业务包（本地上传忽略拉取步骤）
     * 校验业务包是否成功拉取到
     * 成功拉取后更新业务包版本信息
     *
     * @param busiPkgVersionDetail
     * @param userId
     * @return
     * @throws BusinessException
     */
    @Async
    public void doInsertBusiPkgAsync(BusiPkgVersionDetail busiPkgVersionDetail, String userId) throws BusinessException {
        //1、拉取远端服务器的业务包(本地上传无需考虑)
        pullPkgByUploadWay(busiPkgVersionDetail,userId);
        //2、进行文件校验，并将文件信息写入到busiPkgVersionDetail，如果校验成功，则文件已经上传成功
        logger.info("进行文件校验，并将文件信息写入到busiPkgVersionDetail");
        busiPkgVersionDetail = busiPkgVersionService.setBusiPkgVersionParams(busiPkgVersionDetail,userId );
        //3、更新版本信息
        int row = busiPkgVersionService.doUpdateByBean(busiPkgVersionDetail);
        if (row > 0) {
            logger.info("更新数据库成功！");
        } else {
            logger.error("更新数据库失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_MODIFY_FAILURE"));
        }
        //4、将版本大小添加到业务包大小中
        logger.info("将版本大小添加到业务包大小中");
        busiPkgVersionService.updateBusiPkgDetailBusiPkgSize(busiPkgVersionDetail.getBusiPkgId(), busiPkgVersionDetail.getBusiPkgVersionSize());
    }

    protected void pullPkgByUploadWay(BusiPkgVersionDetail busiPkgVersionDetail, String userid){
        //根据上传方式选择后续处理方式
        if (busiPkgVersionDetail.getUploadWay() != 0) {
            try {
                BusiPkgDetail busiPkgDetail = busiPkgService.doFindById(busiPkgVersionDetail.getBusiPkgId());
                RemoteServerInfo rsi = new RemoteServerInfo(busiPkgDetail.getHostIp(), busiPkgDetail.getPort(), busiPkgDetail.getUserName(), busiPkgDetail.getPasswd() ,busiPkgDetail.getRemoteFile());
                Result result = null;
                logger.debug("------------上传远端服务器信息：{}----------", rsi.toString());
                if (busiPkgVersionDetail.getUploadWay() == 1) {//main主机上传方式
                    result = busiPkgService.scpDownLoadBusiPkgFile(userid, rsi, 1);
                    logger.debug("------------主机上传：{}----------", "成功");
                } else if (busiPkgVersionDetail.getUploadWay() == 2) {//ftp方式
                    result = busiPkgService.ftpDownLoadBusiPkgFile(userid, rsi, 1);
                    logger.debug("------------FTP上传：{}----------", "成功");
                }
                if (result != null) {
                    //设置上传文件名和上传文件路径
                    busiPkgVersionDetail.setPath(((HashMap) result.getData()).get("path").toString());
                    busiPkgVersionDetail.setUploadName(((HashMap) result.getData()).get("uploadName").toString());
                }
            } catch (BusinessException e) {
                logger.error("上传业务包失败！");
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_BUILD_FAILURE_PKGIDNULL"));
            }
        }
    }
}
