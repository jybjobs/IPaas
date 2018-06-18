package com.cloud.paas.containerconsole.ws;

import java.net.Socket;

/**
 * websocket的session.
 * <p>用于区分不同的连接</p>
 * @author will
 */
public class ExecSession {
    private String ip;
    private String containerName;
    private Socket socket;
    private OutPutThread outPutThread;

    public ExecSession(String ip, String containerName, Socket socket, OutPutThread outPutThread) {
        this.ip = ip;
        this.containerName = containerName;
        this.socket = socket;
        this.outPutThread = outPutThread;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public OutPutThread getOutPutThread() {
        return outPutThread;
    }

    public void setOutPutThread(OutPutThread outPutThread) {
        this.outPutThread = outPutThread;
    }
}
