package com.cloud.paas.appservice.util.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.appservice.dao.SrvInstDetailDAO;
import com.cloud.paas.appservice.dao.SrvVersionDetailDAO;
import com.cloud.paas.appservice.model.SrvVersionDetail;
import com.cloud.paas.appservice.qo.SrvInstDetailExample;
import com.cloud.paas.appservice.service.srv.SrvMngService;
import com.cloud.paas.appservice.vo.srv.SrvInstDetailVO;
import com.cloud.paas.configuration.PropertiesConfUtil;
import com.cloud.paas.util.bean.BeanUtil;
import com.cloud.paas.util.codestatus.CodeStatusContant;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.rest.RestClient;
import com.cloud.paas.util.rest.RestConstant;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by 17798 on 2018/4/13.
 */
@Component
public class SrvAutoJobs {
    private static final Logger logger = LoggerFactory.getLogger(SrvAutoJobs.class);
    public final static long ONE_Minute = 60 * 1000;
    public final static long HALF_Minute = 30 * 1000;
    public final static long FIVE_SECOND = 5 * 1000;
    public final static long TEN_SECOND = 10 * 1000;

    @Autowired
    private SrvMngService srvMngService;
    @Autowired
    private SrvVersionDetailDAO srvVersionDetailDAO;
    @Autowired
    private SrvInstDetailDAO srvInstDetailDAO;

    /**
     * 轮询业务包状态，自动构建服务镜像
     *
     * @throws Exception
     */
    @Scheduled(fixedDelay = HALF_Minute)
    public void autoBuildSrvVersionJob() throws Exception {
        autoBuildSrvVersion();
    }

    @Async
    public void autoBuildSrvVersion() {
        //1.轮询业务包状态信息
        logger.debug("1.轮询业务包状态信息");
        Result result = srvMngService.pollingBusiPkgStatus();
        logger.debug("轮询业务包状态信息结果{}", result.getData());
        if (result.getData() != null) {
            JSONArray jsonArray = JSONArray.parseArray(result.getData().toString());
            //2.循环遍历返回信息，调用构建服务镜像的接口
            String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.OP_IMAGE) + "/" + 1 + "/createAndBuildSrvImage";
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                Iterator<String> iter = jsonObject.keySet().iterator();
                while (iter.hasNext()) {
                    //获取服务版本编号和业务包版本状态
                    int srvVersionId = Integer.parseInt(iter.next());
                    logger.debug("服务版本编号{}", srvVersionId);
                    Object status = jsonObject.get(srvVersionId);
                    if (status != null) {
                        String busiPkgStatusCode = status.toString();
                        //2为成功，3为失败
                        logger.debug("业务包版本状态简码{}", busiPkgStatusCode);
                        if ("2".equals(busiPkgStatusCode)) {
                            logger.debug("构建服务镜像{}", srvMngService.createAndBuildSrvImage(null, srvVersionId, url));
                        } else {
                            //4.1052100为镜像正在构建中
                            SrvVersionDetail srvVersionDetail = srvVersionDetailDAO.doFindById(srvVersionId);
                            srvVersionDetail.setSrvVersionStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.IMAGE_BUILDING).getCode().toString());
                            logger.debug("更新服务定义状态");
                            srvVersionDetailDAO.doUpdateByBean(srvVersionDetail);
                        }
                    }
                }
            }
        }
    }

    /**
     * 轮询服务版本状态，自动启动服务实例
     *
     * @throws Exception
     */
//    @Scheduled(fixedDelay = HALF_Minute)
//    public void autoStartSrvInstJob() throws Exception {
//        autoStartSrvInst();
//    }

    @Async
    public void autoStartSrvInst() {
        //1.获取待启动的并且状态为active的服务实例
        logger.debug("1.获取待启动的并且状态为active的服务实例列表");
        SrvInstDetailExample srvInstDetailExample = new SrvInstDetailExample();
        srvInstDetailExample.setNoHistory(1);
        srvInstDetailExample.setStatus(CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.SERVICE_INSTANCE_WAIT_PUBLISH).getCode());
        List<SrvInstDetailVO> list = srvInstDetailDAO.listSrvInstByCondition(srvInstDetailExample);
        //2.遍历列表，轮询服务定义状态
        logger.debug("2.遍历列表，轮询服务定义状态");
        for (SrvInstDetailVO srvInstDetailVO : list) {
            SrvVersionDetail srvVersionDetail = srvVersionDetailDAO.doFindById(srvInstDetailVO.getSrvVersionDetail().getSrvVersionId());
            logger.debug("服务版本{}状态为{}",srvVersionDetail.getSrvVersion(),CodeStatusUtil.getStatusByCode(Integer.parseInt(srvVersionDetail.getSrvVersionStatus())).getMsg());
            if (CodeStatusUtil.getStatusByCodeEn(CodeStatusContant.IMAGE_UPLOADING_SUCCESS).getCode().toString()
                    .equals(srvVersionDetail.getSrvVersionStatus())) {
                //3.启动服务实例
                logger.debug("3.启动服务实例");
                srvMngService.srvInstRun(srvInstDetailVO.getSrvInstId());
            }
        }
    }


}
