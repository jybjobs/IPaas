package com.cloud.paas.imageregistry.util.job;

import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.imageregistry.service.busipkg.BusiPkgVersionService;
import com.cloud.paas.imageregistry.service.image.ImageVersionService;
import com.cloud.paas.imageregistry.websocket.ImageWebSocket;
import com.cloud.paas.imageregistry.websocket.PkgWebSocket;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: zcy
 * @desc: 轮询工作方法
 * @Date: Created in 2018-01-31 11:15
 * @modified By:
 **/

@Component
public class Jobs {
    private static final Logger logger = LoggerFactory.getLogger(Jobs.class);
    public final static long ONE_Minute =  60 * 1000;
    public final static long FIVE_SECOND =  5 * 1000;
    public final static long TEN_SECOND =  10 * 1000;

    @Autowired
    ImageVersionService imageVersionService;
    @Autowired
    BusiPkgVersionService busiPkgVersionService;

    @Scheduled(fixedDelay=TEN_SECOND)
    public void queryAppsStatus() throws Exception{
        for (ImageWebSocket imageWebSocket:ImageWebSocket.imageWebSocketSet){
            queryImageStatus(imageWebSocket);
        }
    }

    @Async
    public void queryImageStatus(ImageWebSocket imageWebSocket) {
        if (null!=imageWebSocket.imageIds){
            Result result=imageVersionService.refreshStatus("1",imageWebSocket.imageIds);
            imageWebSocket.sendMessage(JSONObject.toJSONString(result));
        }
    }

    @Scheduled(fixedDelay=FIVE_SECOND)
    public void queryPkgStatus() throws Exception{
        for (PkgWebSocket pkgWebSocket:PkgWebSocket.pkgWebSocketSet){
            queryPkgStatus(pkgWebSocket);
        }
    }

    @Async
    public void queryPkgStatus(PkgWebSocket pkgWebSocket) {
        if (null!=pkgWebSocket.pkgIds){
            Result result=busiPkgVersionService.refreshStatus("1",pkgWebSocket.pkgIds);
            pkgWebSocket.sendMessage(JSONObject.toJSONString(result));
        }
    }
}
