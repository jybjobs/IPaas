package com.cloud.paas.util.websocket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;

/**
 * @author: zcy
 * @desc: websocket公共类
 * @Date: Created in 2018-01-31 11:30
 * @modified By:
 **/
public class BaseWebSocket {
    private static final Logger logger = LoggerFactory.getLogger(BaseWebSocket.class);
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    protected Session session;

    /**
     * 连接建立成功调用的方法
     * @param session
     */
    public void onOpen(Session session) {
        this.session = session;
        sendMessage("连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    public void onClose() {

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    public void onMessage(String message, Session session) {
        logger.debug("来自客户端的消息:" + message);

    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    public void onError(Session session, Throwable error) {
        logger.error("websocket错误:{}",error.getMessage());
    }

    /**
     * 发送消息
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message)  {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.error("发送消息发生错误:{}",e.getMessage());
        }
        //this.session.getAsyncRemote().sendText(message);
    }
}
