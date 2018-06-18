package com.cloud.paas.util.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.TelnetInputStream;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;
import java.util.StringTokenizer;
/**
 * Created by kaiwen on 2018/1/9.
 */
public class FtpFileUtil {
    // FTP客户端
    private FtpClient ftpClient;
    private static final Logger logger = LoggerFactory.getLogger(FtpFileUtil.class);

    /**
     * @param ip         ftp的id
     * @param port       ftp的端口，数据通信一般为21
     * @param user       用户名
     * @param password   密码
     * @param localFile  上传文件的路径加文件名
     * @param remoteFile 远端生成的文件名
     * @return
     * @throws Exception
     */
    public Result fileUpLoad(String ip, int port, String user, String password, String localFile, String remoteFile) throws Exception {
        Result result = null;
        try {
            connectServer(ip, port, user, password);
            upload(localFile, remoteFile);
            closeConnect();
            result = CodeStatusUtil.resultByCodeEn("FTP_UP_LOAD_SUCCESS");
            return result;
        } catch (Exception e) {
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("FTP_UP_LOAD_FAILURE"));
        }

    }

    /**
     * 下载
     *
     * @param ip         ftp的ip
     * @param port       ftp的链接端口
     * @param user       用户
     * @param password   密码
     * @param remoteFile ftp上的文件名
     * @param localFile  仓库中文件的路径
     * @return
     * @throws Exception
     */
    public String fileDownLoad(String ip, int port, String user, String password, String remoteFile, String localFile) throws BusinessException {
        String result = null;
        logger.debug("------FTP建立连接------");
        connectServer(ip, port, user, password);
        logger.debug("------FTP开始下载------");
        download(remoteFile, localFile);
        logger.debug("------FTP关闭连接------");
        closeConnect();
        result="success";
        return result;

    }

    /**
     * 服务器连接
     *
     * @param ip       服务器IP
     * @param port     服务器端口
     * @param user     用户名
     * @param password 密码
     */
    private void connectServer(String ip, int port, String user, String password) {
        try {
            ftpClient = FtpClient.create();
            try {
                SocketAddress addr = new InetSocketAddress(ip, port);
                this.ftpClient.connect(addr);
                this.ftpClient.login(user, password.toCharArray());
                logger.info("登陆Ftp成功");
                /*if (path.length() != 0) {
                    // 把远程系统上的目录切换到参数path所指定的目录
                    this.ftpClient.changeDirectory(path);
                }*/
            } catch (FtpProtocolException e) {
                logger.error("抛出FtpProtocolException错误", e);
                throw new RuntimeException(e);
            }
        } catch (IOException ex) {
            logger.error("抛出IOException错误", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * 上传文件
     *
     * @param localFile  本地文件
     * @param remoteFile 远程文件
     */
    private void upload(String localFile, String remoteFile) {
        OutputStream os = null;
        FileInputStream is = null;
        try {
            try {
                // 将远程文件加入输出流中
                os = this.ftpClient.putFileStream(remoteFile);
            } catch (FtpProtocolException e) {
                logger.error("将远程文件加入输出流中出现错误", e);
            }
            // 获取本地文件的输入流
            File file_in = new File(localFile);
            is = new FileInputStream(file_in);
            // 创建一个缓冲区
            byte[] bytes = new byte[1024];
            int c;
            while ((c = is.read(bytes)) != -1) {
                os.write(bytes, 0, c);
            }
            logger.debug("upload success");
        } catch (IOException ex) {
            logger.error("upload fail下载文件失败");
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                logger.error("关闭本地文件的输入流出错");
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    logger.error("关闭本地文件的输出流出错");
                }
            }
        }
    }

    /**
     * 文件下载
     *
     * @param remoteFile 远程文件
     * @param localFile  本地文件
     */
    private void download(String remoteFile, String localFile) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        //单位 字节
        long size = 0L;
        //IOExeption判断路径是否正确
        try {
            // 获取远程机器上的文件filename，借助TelnetInputStream把该文件传送到本地
            logger.info("获取远程机器上的文件");
            ftpClient.setBinaryType();
            inputStream = this.ftpClient.getFileStream(remoteFile);
            logger.info("获取远程机器上的文件{}", inputStream);
            File file_in = new File(localFile);
            fileOutputStream = new FileOutputStream(file_in);
            byte[] bytes = new byte[1024];
            int c;
            size = ftpClient.getLastTransferSize();
            logger.debug("镜像大小{}", size);
            //判断文件是否为空
            if (size <= 0) {
                logger.debug("镜像大小{}", size);
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_FAILURE_EMPTY"));
            }
            while ((c = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, c);
            }
            logger.debug("下载成功");
        } catch (IOException ex) {
            logger.error("下载失败");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_FAILURE_IOERROR"));
        } catch (FtpProtocolException e) {
            logger.error("获取远端文件名出错");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.error("关闭输入流失败");
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    logger.error("关闭输出流失败");
                }
            }
        }
    }

    /**
     * 关闭连接
     */
    private void closeConnect() {
        try {
            this.ftpClient.close();
            logger.debug("关闭连接成功");
        } catch (IOException ex) {
            logger.error("关闭连接失败");
        }
    }
}

