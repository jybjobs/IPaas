package com.cloud.paas.appservice.websocket;


import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.appservice.qo.SrvDeploymentExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by 17798 on 2018/4/2.
 */
@ServerEndpoint(value = "/websocket/srv/deploy")
@Component
public class SrvDeployWebsocket {
    private static final Logger logger = LoggerFactory.getLogger(SrvDeployWebsocket.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的SrcWebSocket对象。
    public static CopyOnWriteArraySet<SrvDeployWebsocket> srvDeployWebsocketSet = new CopyOnWriteArraySet<SrvDeployWebsocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //获取需要轮询的appIds
    public static Map<Session, SrvDeploymentExample> srvDeploymentExampleMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        srvDeployWebsocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        logger.debug("有新连接加入！当前在线人数" + getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            logger.error("IO异常:"+e.toString());
        }

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        srvDeployWebsocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        logger.debug("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.debug("来自客户端的消息:" + message);
        JSONObject msgJso=JSONObject.parseObject(message);
        SrvDeploymentExample srvDeploymentExample = new SrvDeploymentExample();
        srvDeploymentExample.setSrvId(msgJso.getInteger("srvId"));
        srvDeploymentExample.setEnvId(msgJso.getInteger("envId"));
        srvDeploymentExampleMap.put(session, srvDeploymentExample);
    }

    /**
     * 发生错误时调用
     **/
    @OnError

    public void onError(Session session, Throwable error) {
        logger.error(error.toString());
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message) throws IOException {
        for (SrvDeployWebsocket item : srvDeployWebsocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                logger.error(e.toString());
                continue;
            }
        }
    }

    public Session getSession() {
        return this.session;
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        SrvDeployWebsocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        SrvDeployWebsocket.onlineCount--;
    }
}
