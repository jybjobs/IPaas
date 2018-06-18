package com.cloud.paas.imageregistry.websocket;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.util.websocket.BaseWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by 17798 on 2018/3/15.
 * 业务包状态查询的websocket
 */
@ServerEndpoint(value = "/pkg/websocket")
@Component
public class PkgWebSocket extends BaseWebSocket {
    private static final Logger logger = LoggerFactory.getLogger(PkgWebSocket.class);

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的pkgWebSocketSet对象。
    public static CopyOnWriteArraySet<PkgWebSocket> pkgWebSocketSet = new CopyOnWriteArraySet<PkgWebSocket>();


    //获取需要轮询的appIds
    public static List<Long> pkgIds=null;

    /**
     * 连接建立成功调用的方法
     * @param session
     */
    @OnOpen
    @Override
    public void onOpen(Session session) {
        super.onOpen(session);
        pkgWebSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        logger.debug("有新连接加入！当前在线人数为" + getOnlineCount());
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    @Override
    public void onClose() {
        super.onClose();
        pkgWebSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        logger.debug("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    @Override
    public void onMessage(String message, Session session) {
        super.onMessage(message,session);
        JSONObject msgJso=JSONObject.parseObject(message);
        JSONArray msgJsa=JSONArray.parseArray(msgJso.getString("pkgIds"));
        pkgIds=new ArrayList<>();
        for (int i=0;i<msgJsa.size();i++){
            pkgIds.add(msgJsa.getLong(i));
        }
    }
    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    @Override
    public void onError(Session session, Throwable error) {
        super.onError(session,error);
    }
    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        for (PkgWebSocket item : pkgWebSocketSet) {
            item.sendMessage(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        PkgWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        PkgWebSocket.onlineCount--;
    }

}
