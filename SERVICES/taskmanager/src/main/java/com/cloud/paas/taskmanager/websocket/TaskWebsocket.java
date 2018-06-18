package com.cloud.paas.taskmanager.websocket;

import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.taskmanager.service.JenkinsService;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author: srf
 * @desc: TaskWebsocket对象
 * @Date: Created in 2018/5/25 8:59
 * @Modified By:
 */
@ServerEndpoint(value = "/websocket/task")
@Component
public class TaskWebsocket {
    private static ApplicationContext applicationContext;
    private JenkinsService jenkinsService;

    private static final Logger logger = LoggerFactory.getLogger(TaskWebsocket.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的SrcWebSocket对象。
    public static CopyOnWriteArraySet<TaskWebsocket> taskWebSocketSet = new CopyOnWriteArraySet<TaskWebsocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        TaskWebsocket.applicationContext = applicationContext;
    }

    /**
     * 连接建立成功调用的方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        taskWebSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        logger.debug("有新连接加入！当前在线人数为" + getOnlineCount());
        try {
            jenkinsService = applicationContext.getBean(JenkinsService.class);
            sendMessage("连接成功");
        } catch (Exception e) {
            logger.error("IO异常:"+e.toString());
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.debug("来自客户端的消息:" + message);
        JSONObject msgJso=JSONObject.parseObject(message);
        int tenantId = msgJso.getInteger("tenantId");
        int srvId = msgJso.getInteger("srvId");
        int jobId = msgJso.getInteger("jobId");
        try {
            while (true) {
                Result result = jenkinsService.getBuildList(tenantId, srvId, jobId);
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(result);
                session.getBasicRemote().sendText(JSONObject.toJSONString(jsonObject));
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            try {
                session.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        taskWebSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        logger.debug("有一连接关闭！当前在线人数为" + getOnlineCount());
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
    }
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        TaskWebsocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        TaskWebsocket.onlineCount--;
    }
}
