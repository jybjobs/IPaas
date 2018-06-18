package com.cloud.paas.appservice.util.job;

import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.appservice.dao.SrvDeploymentDAO;
import com.cloud.paas.appservice.dao.SrvOperationDAO;
import com.cloud.paas.appservice.controller.srv.SrvMngController;
import com.cloud.paas.appservice.qo.SrvDeploymentExample;
import com.cloud.paas.appservice.service.srv.SrvDeploymentService;
import com.cloud.paas.appservice.service.srv.SrvMngService;
import com.cloud.paas.appservice.service.srv.SrvImplementService;
import com.cloud.paas.appservice.vo.srv.SrvDeploymentVO;
import com.cloud.paas.appservice.websocket.*;
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
import java.util.List;

@Component
public class srvJobs {
    private static final Logger logger = LoggerFactory.getLogger(Jobs.class);
    public final static long ONE_Minute =  60 * 1000;
    public final static long ONESECOND =  1 * 1000;
    public final static long FIVE_SECOND =  5 * 1000;
    public final static long TEN_SECOND =  10 * 1000;

    @Autowired
    private SrvOperationDAO srvOperationDAO;
    @Autowired
    private SrvImplementService srvImplementService;
    @Autowired
    private SrvDeploymentDAO srvDeploymentDAO;
    @Autowired
    private SrvDeploymentService srvDeploymentService;
    @Autowired
    private SrvMngController srvMngController;
    @Autowired
    private SrvMngService srvMngService;


    @Scheduled(fixedDelay=TEN_SECOND)
    public void querySrcStatus() throws Exception{
        for (SrvWebsocket srvWebsocket:SrvWebsocket.srvWebSocketSet){
            querySrcStatus(srvWebsocket);
        }
    }

    @Async
    public void querySrcStatus(SrvWebsocket srcWebsocket) throws Exception{
        if (SrvWebsocket.srvIds!=null){
            Result result=srvMngController.getGroupOfSrvMng(SrvWebsocket.srvIds);
            srcWebsocket.sendMessage(JSONObject.toJSONString(result));
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

    /**
     * 轮询中间状态的镜像的最新状态
     * @throws Exception
     */
    @Scheduled(fixedDelay=FIVE_SECOND)
    public void queryImageStatus() throws Exception{
        queryImageStatusAsync();
    }

    @Async
    public void queryImageStatusAsync() throws Exception{
        srvMngService.syncSrvVersionImageStatus();
    }







    @Scheduled(fixedDelay=TEN_SECOND)
    public void querySrvDefStatus() throws Exception{
        for (SrvDefWebsocket srvDefWebsocket:SrvDefWebsocket.srvDefWebSocketSet){
            querySrvDefStatus(srvDefWebsocket);
        }
    }

    @Async
    public void querySrvDefStatus(SrvDefWebsocket srvDefWebsocket) throws Exception{
        if (SrvDefWebsocket.srvDefIds!=null){
            Result result = srvMngService.listSrvVersionDetailByIds(SrvDefWebsocket.srvDefIds);
            srvDefWebsocket.sendMessage(JSONObject.toJSONString(result));
        }
    }

    @Scheduled(fixedDelay=FIVE_SECOND)
    public void rollingContainerLogJob() throws Exception{
        for (SrvInstLogWebSocket srvInstLogWebSocket:SrvInstLogWebSocket.srvInstLogWebSocketSet){
            rollingContainerLog(srvInstLogWebSocket);
        }
    }

    @Async
    public void rollingContainerLog(SrvInstLogWebSocket srvInstLogWebSocket) throws Exception{
        if (SrvInstLogWebSocket.srvInstIdMap!=null && SrvInstLogWebSocket.srvInstIdMap.get(srvInstLogWebSocket.getSession()) != null){
            Integer srvInstId = SrvInstLogWebSocket.srvInstIdMap.get(srvInstLogWebSocket.getSession());
            Result result = srvMngService.rollingContainerLog(srvInstId);
            srvInstLogWebSocket.sendMessage(JSONObject.toJSONString(result));
        }
    }

    @Scheduled(fixedDelay=FIVE_SECOND)
    public void queryPodLogJob() throws Exception{
        for (ContainerLogWebSocket containerLogWebSocket:ContainerLogWebSocket.containerLogWebSocketSet){
            queryPodLog(containerLogWebSocket);
        }
    }

    @Async
    public void queryPodLog(ContainerLogWebSocket containerLogWebSocket) throws Exception{
        if (ContainerLogWebSocket.podNameMap!=null && ContainerLogWebSocket.podNameMap.get(containerLogWebSocket.getSession()) != null
                && ContainerLogWebSocket.podNamespaceMap!=null && ContainerLogWebSocket.podNamespaceMap.get(containerLogWebSocket.getSession()) != null){
            String podName = ContainerLogWebSocket.podNameMap.get(containerLogWebSocket.getSession());
            String namespace = ContainerLogWebSocket.podNamespaceMap.get(containerLogWebSocket.getSession());
            Result result = srvDeploymentService.queryPodLog(podName, namespace);
            containerLogWebSocket.sendMessage(JSONObject.toJSONString(result));
        }
    }

    /**
     * 轮询部署信息
     * @throws Exception
     */
    @Scheduled(fixedDelay=ONESECOND)
    public void queryDeployInfo() throws Exception{
        for (SrvDeployWebsocket srvDeployWebsocket:SrvDeployWebsocket.srvDeployWebsocketSet){
            queryDeployInfoAsync(srvDeployWebsocket);
        }

    }

    @Async
    public void queryDeployInfoAsync(SrvDeployWebsocket srvDeployWebsocket) throws Exception{
        if (SrvDeployWebsocket.srvDeploymentExampleMap!=null && SrvDeployWebsocket.srvDeploymentExampleMap.get(srvDeployWebsocket.getSession()) != null){
            SrvDeploymentExample srvDeploymentExample = srvDeployWebsocket.srvDeploymentExampleMap.get(srvDeployWebsocket.getSession());
            List<SrvDeploymentVO> srvDeploymentVOS = srvDeploymentDAO.listSrvDeploymentWithSrvInst(srvDeploymentExample);
            Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.DEPLOY_QUERY_SUCCESS);
            result.setData(srvDeploymentVOS);
            srvDeployWebsocket.sendMessage(JSONObject.toJSONString(result));
        }
    }


}
