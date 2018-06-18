package com.cloud.paas.util.dns;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.Config;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.lang.StringBuffer;

/**
 * DNS动态更新类
 */
public class DnsUpdator {
    private static final Logger logger = LoggerFactory.getLogger(DnsUpdator.class);

    private static DnsInfo dnsInfo = null;
    private Connection connection = null;
    private Session session = null;

    public DnsUpdator() {
        initDns();
    }

    /**
     * @return
     */
    public void createConnection() throws Exception {  // TODO
        if (dnsInfo.getDnsserver() == null || dnsInfo.getDnsserver().isEmpty()) {
            logger.info("DNS服务器连接失败：服务器地址为空");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("DNS_CONNECT_FALILURE"));
        }
        this.connection = new Connection(dnsInfo.getDnsserver(), dnsInfo.getPort());
        try {
            this.connection.connect();
        } catch (Exception e) {
            logger.info("DNS服务器连接失败");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("DNS_CONNECT_FALILURE"));
        }
        try {
            boolean isAuthenticated = this.connection.authenticateWithPassword(dnsInfo.getUsername(), dnsInfo.getPassword());
            if (!isAuthenticated) {
                logger.info("DNS服务器登录失败");
                throw new BusinessException( CodeStatusUtil.resultByCodeEn("DNS_LOGIN_FAILURE") );
            }
        } catch (Exception e) {
            if (this.connection != null) {
                this.connection.close();
                this.connection = null;
            }
            logger.info("DNS服务器登录失败");
            throw new BusinessException( CodeStatusUtil.resultByCodeEn("DNS_LOGIN_FAILURE") );
        }
    }

    /**
     * @return
     */
    public void createSession() throws Exception {  // TODO
        try {
            this.session = this.connection.openSession();
        } catch (Exception e) {
            if (this.session != null) {
                this.session.close();
                this.session = null;
            }
            logger.info("DNS会话创建失败");
            Result result = CodeStatusUtil.resultByCodeEn("DNS_BUILD_SESSION_FAILURE");
            throw new BusinessException(result);
        }
    }

    /**
     * @param cmdType
     * @param childDomainName
     * @param rootDomainName
     * @param ip
     * @return
     */
    public String createCommand(String cmdType, String childDomainName, String rootDomainName, String ip) throws Exception{ // TODO

        if (rootDomainName == null || rootDomainName.isEmpty()) {
            logger.info("DNS指令创建失败");
            Result result = CodeStatusUtil.resultByCodeEn("DNS_BUILD_CMD_FAILURE");
            throw new BusinessException(result);
        }

        StringBuffer cmdCore = new StringBuffer();
        if (cmdType.equals("add")) {
            if (childDomainName == null || childDomainName.isEmpty()) {
                cmdCore.append("update ").append(cmdType).append(" ").append(rootDomainName).append(" 8000 A ").append(ip);
            } else {
                cmdCore.append("update ").append(cmdType).append(" ").append(childDomainName).append(".").append(rootDomainName).append(" 8000 A ").append(ip);
            }
        } else if (cmdType.equals("delete")) {
            if (childDomainName == null || childDomainName.isEmpty()) {
                cmdCore.append("update ").append(cmdType).append(" ").append(rootDomainName).append(" 8000 A");
            } else {
                cmdCore.append("update ").append(cmdType).append(" ").append(childDomainName).append(".").append(rootDomainName).append(" 8000 A");
            }
        } else {
            logger.info("DNS指令创建失败");
            Result result = CodeStatusUtil.resultByCodeEn("DNS_BUILD_CMD_FAILURE");
            throw new BusinessException(result);
        }

        return getCommand(rootDomainName, cmdCore.toString());
    }

    public String getCommand(String rootDomainName, String cmdCore) {
        StringBuffer command = new StringBuffer();
        command.append("cat <<  EOF | nsupdate \n").
                append("server ").append(dnsInfo.getDnsserver()).append("\n").
                append("zone ").append(rootDomainName).append("\n").
                append(cmdCore).append("\n").
                append("send").append("\n").
                append("quit").append("\n").
                append("EOF");

        return command.toString();
    }

    /**
     * @return
     * @author SHY
     * @function 添加DNS纪录
     * @params
     * @date 2018/1/15 10:02
     */

    public boolean addDnsRecord(String childDomainName, String rootDomainName, String ip) {
        boolean res = true;
        try {
            this.createConnection();
            this.createSession();

            // 添加重复记录: 不执行，不出错
            String command = this.createCommand("add", childDomainName, rootDomainName, ip);
            this.session.execCommand(command);

            this.session.close();
            this.connection.close();
        } catch (Exception e) {
            logger.info("execCommand() error!");
            e.printStackTrace(System.err);
            res = false;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
            if (this.connection != null) {
                this.connection.close();
            }
            return res;
        }
    }

    /**
     * @return
     * @author SHY
     * @function 删除DNS纪录
     * @params
     * @date 2018/1/15 10:02
     */
    public boolean deleteDnsRecord(String childDomainName, String rootDomainName, String ip) {
        boolean res = true;

        try {
            this.createConnection();
            this.createSession();

            // 删除空记录: 不出错
            String command = this.createCommand("delete", childDomainName, rootDomainName, ip);
            this.session.execCommand(command);
            this.session.close();
            this.connection.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            res = false;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
            if (this.connection != null) {
                this.connection.close();
            }
            return res;
        }
    }

    /**
     * @return
     * @author SHY
     * @function 查询DNS纪录
     * @params
     * @date 2018/1/15 10:02
     */
    public String selectDnsRecord(String childDomainName, String rootChildName) {

        String res = new String();

        try {
            if (rootChildName == null || rootChildName.isEmpty())
                throw new Exception("invalid rootDomainName: rootDomainName is empty !");

            this.createConnection();
            this.createSession();

            String command = "cat /var/named/data/" + rootChildName + ".zone | grep '\\<A\\>' " + " | grep " + childDomainName;
            this.session.execCommand(command);

            InputStream stdout = new StreamGobbler(session.getStdout());
            BufferedReader stdoutReader = new BufferedReader(
                    new InputStreamReader(stdout));

            while (true) {
                String line = stdoutReader.readLine();
                if (line == null) {
                    break;
                }
                res += "\n";
                res += line;
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (this.session != null) {
                this.session.close();
            }
            if (this.connection != null) {
                this.connection.close();
            }
        }

        return res;
    }

    /**
     * @return
     * @author SHY
     * @function 打印指令执行结果
     * @params
     * @date 2018/1/15 10:02
     */
    public void printResult() {
        try {
            InputStream stdout = new StreamGobbler(this.session.getStdout());
            InputStream stderr = new StreamGobbler(this.session.getStderr());
            BufferedReader stdoutReader = new BufferedReader(
                    new InputStreamReader(stdout));
            BufferedReader stderrReader = new BufferedReader(
                    new InputStreamReader(stderr));

            logger.info("Here is the output from stdout:");
            while (true) {
                String line = stdoutReader.readLine();
                if (line == null) {
                    break;
                }
                logger.info(line);
            }

            logger.info("Here is the output from stderr:");
            while (true) {
                String line = stderrReader.readLine();
                if (line == null) {
                    break;
                }
                logger.info(line);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }


    /**
     * 初始化 dnsInfo
     */
    private void initDns() {
        if (dnsInfo == null) {
            dnsInfo = new DnsInfo();
            dnsInfo.setDnsserver(Config.DNS_SERVER_IP);
            dnsInfo.setUsername(Config.DNS_SERVER_USER);
            dnsInfo.setPassword(Config.DNS_SERVER_PASSWORD);
            dnsInfo.setPort(Config.DNS_SERVER_PORT);
        }
    }

}

