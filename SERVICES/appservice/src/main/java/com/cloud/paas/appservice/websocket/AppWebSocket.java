package com.cloud.paas.appservice.websocket;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author: zcy
 * @desc: 应用状态码查询websocket实现类
 * @Date: Created in 2018-01-05 13:59
 * @modified By:
 **/

@ServerEndpoint(value = "/app/websocket")
@Component
public class AppWebSocket {
    private static final Logger logger = LoggerFactory.getLogger(AppWebSocket.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的AppWebSocket对象。
    public static CopyOnWriteArraySet<AppWebSocket> webSocketSet = new CopyOnWriteArraySet<AppWebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //获取需要轮询的appIds
    public Integer[] appIds=null;

    /**
     * 连接建立成功调用的方法
     * */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        logger.debug("有新连接加入！当前在线人数为" + getOnlineCount());
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
        webSocketSet.remove(this);  //从set中删除
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
        JSONArray msgJsa=JSONArray.parseArray(msgJso.getString("appIds"));
        appIds=new Integer[msgJsa.size()];
        for (int i=0;i<msgJsa.size();i++){
            appIds[i]=msgJsa.getInteger(i);
        }
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
        for (AppWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                logger.error(e.toString());
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        AppWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        AppWebSocket.onlineCount--;
    }
}
