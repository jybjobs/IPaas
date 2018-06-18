package com.cloud.paas.appservice.util.job;

import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.appservice.service.app.AppMngService;

import com.cloud.paas.appservice.dao.SrvOperationDAO;
import com.cloud.paas.appservice.service.srv.SrvImplementService;
import com.cloud.paas.appservice.websocket.AppWebSocket;
import com.cloud.paas.util.codestatus.CodeStatus;
import com.cloud.paas.util.codestatus.CodeStatusContant;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Jobs {
    private static final Logger logger = LoggerFactory.getLogger(Jobs.class);
    public final static long ONE_Minute =  60 * 1000;
    public final static long FIVE_SECOND =  5 * 1000;
    public final static long TEN_SECOND =  10 * 1000;

    @Autowired
    private SrvOperationDAO srvOperationDAO;
    @Autowired
    private SrvImplementService srvImplementService;
    @Autowired
    private AppMngService appMngService;

//    @Scheduled(fixedDelay=FIVE_SECOND)
//    public void fixedDelayJob() throws Exception {
//        logger.debug(new Date()+" >>FIVE_SECOND————————fixedDelay执行....");
//        // 判断是否为空
//        if (SrvImplementServiceImpl.srvIdList == null){
//            CodeStatus codeStatus = CodeStatusUtil.getInstance().getStatusByCodeEn(CodeStatusContant.SERVICE_BUILDING);
//            // 数据库中查询正在创建中的状态
//            SrvImplementServiceImpl.srvIdList = srvOperationDAO.getSrvIdByCodeStatus(codeStatus.getCode());
//        } else {
//            if (SrvImplementServiceImpl.srvIdList.size() != 0){
//                for (int index = 0;index < SrvImplementServiceImpl.srvIdList.size();index++){
//                    logger.debug("srv数据:"+SrvImplementServiceImpl.srvIdList.get(index));
//                    // 2.如果是running，更新数据库，去除list中的一个对象
//                    try {
//                        if (srvImplementService.serviceUpdator(SrvImplementServiceImpl.srvIdList.get(index))){
//                            SrvImplementServiceImpl.srvIdList.remove(index);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        throw e;
//                    }
//                }
//            }
//        }
//    }

    @Scheduled(fixedDelay=TEN_SECOND)
    public void queryAppsStatus() throws Exception{
        for (AppWebSocket appWebSocket:AppWebSocket.webSocketSet){
            queryAppStatus(appWebSocket);
        }
    }

    @Async
    public void queryAppStatus(AppWebSocket appWebSocket) throws Exception{
        if (appWebSocket.appIds!=null){
            Result result=appMngService.refreshState("1",appWebSocket.appIds);
            appWebSocket.sendMessage(JSONObject.toJSONString(result));
        }
    }

    @Scheduled(fixedRate=ONE_Minute)
    public void fixedRateJob(){
        logger.debug(new Date()+" >>ONE_Minute————————fixedDelay执行....");
    }

    @Scheduled(cron="0 15 3 * * ?")
    public void cronJob(){
        logger.debug(new Date()+" >>FIVE_SECOND————————fixedDelay执行....");
    }
}

