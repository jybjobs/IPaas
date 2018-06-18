package com.cloud.paas.containerconsole.ws;

import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.containerconsole.util.DockerHelper;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ExecCreation;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 连接容器执行命令.
 * @author will
 */
@Component
public class ContainerExecWSHandler extends TextWebSocketHandler {
    private Map<String,ExecSession> execSessionMap=new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //获得传参
        String ip=session.getAttributes().get("ip").toString();
        String containerName=session.getAttributes().get("containerName").toString();
        String width=session.getAttributes().get("width").toString();
        String height=session.getAttributes().get("height").toString();
        //创建bash
        String execId = createExec(ip, containerName);
        //连接bash
        Socket socket = connectExec(ip, execId);
        //获得输出
        getExecMessage(session, ip, containerName, socket);
        //修改tty大小
        resizeTty(ip, width, height, execId);
    }

    /**
     * 创建bash.
     * @param ip 宿主机ip地址
     * @param containerName 容器名称
     * @return 命令id
     * @throws Exception
     */
    private String createExec(String ip, String containerName) throws Exception {
        return DockerHelper.query(ip, docker->{
            ExecCreation execCreation=docker.execCreate(DockerHelper.findContainerIdByName(docker, containerName),new String[]{"/bin/bash"},
                    DockerClient.ExecCreateParam.attachStdin(), DockerClient.ExecCreateParam.attachStdout(), DockerClient.ExecCreateParam.attachStderr(),
                    DockerClient.ExecCreateParam.tty(true));
            return execCreation.id();
        });
    }

    /**
     * 连接bash.
     * @param ip 宿主机ip地址
     * @param execId 命令id
     * @return 连接的socket
     * @throws IOException
     */
    private Socket connectExec(String ip, String execId) throws IOException {
        Socket socket=new Socket(ip,2375);
        socket.setKeepAlive(true);
        OutputStream out = socket.getOutputStream();
        StringBuffer pw = new StringBuffer();
        pw.append("POST /exec/"+execId+"/start HTTP/1.1\r\n");
        pw.append("Host: "+ip+":2375\r\n");
        pw.append("User-Agent: Docker-Client\r\n");
        pw.append("Content-Type: application/json\r\n");
        pw.append("Connection: Upgrade\r\n");
        JSONObject obj = new JSONObject();
        obj.put("Detach",false);
        obj.put("Tty",true);
        String json=obj.toJSONString();
        pw.append("Content-Length: "+json.length()+"\r\n");
        pw.append("Upgrade: tcp\r\n");
        pw.append("\r\n");
        pw.append(json);
        out.write(pw.toString().getBytes("UTF-8"));
        out.flush();
        return socket;
    }

    /**
     * 获得输出.
     * @param session websocket session
     * @param ip 宿主机ip地址
     * @param containerName 容器名称
     * @param socket 命令连接socket
     * @throws IOException
     */
    private void getExecMessage(WebSocketSession session, String ip, String containerName, Socket socket) throws IOException {
        InputStream inputStream=socket.getInputStream();
        byte[] bytes=new byte[1024];
        StringBuffer returnMsg=new StringBuffer();
        while(true){
            int n = inputStream.read(bytes);
            String msg=new String(bytes,0,n);
            returnMsg.append(msg);
            bytes=new byte[10240];
            if(returnMsg.indexOf("\r\n\r\n")!=-1){
                session.sendMessage(new TextMessage(returnMsg.substring(returnMsg.indexOf("\r\n\r\n")+4,returnMsg.length())));
                break;
            }
        }
        OutPutThread outPutThread=new OutPutThread(inputStream,session);
        outPutThread.start();
        execSessionMap.put(containerName,new ExecSession(ip,containerName,socket,outPutThread));
    }

    /**
     * 修改tty大小.
     * @param ip
     * @param width
     * @param height
     * @param execId
     * @throws Exception
     */
    private void resizeTty(String ip, String width, String height, String execId) throws Exception {
        DockerHelper.execute(ip, docker->{
            docker.execResizeTty(execId,Integer.parseInt(height),Integer.parseInt(width));
        });
    }

    /**
     * websocket关闭后关闭线程.
     * @param session
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String containerName=session.getAttributes().get("containerName").toString();
        ExecSession execSession=execSessionMap.get(containerName);
        if(execSession!=null){
            execSession.getOutPutThread().interrupt();
        }
    }

    /**
     * 获得先输入.
     * @param session
     * @param message 输入信息
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String containerName=session.getAttributes().get("containerName").toString();
        ExecSession execSession=execSessionMap.get(containerName);
        OutputStream out = execSession.getSocket().getOutputStream();
        out.write(message.asBytes());
        out.flush();
    }
}
