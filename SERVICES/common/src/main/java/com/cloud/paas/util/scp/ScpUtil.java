package com.cloud.paas.util.scp;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.cloud.paas.util.fileupload.FileUtil;
import com.cloud.paas.util.fileupload.UploadFile;

/**
 * Created by CSS on 2018/1/9.
 */
public class ScpUtil {
    private static Logger logger = LoggerFactory.getLogger(ScpUtil.class);

    //jar包服务器地址
    private static String IP;
    private static int PORT;
    private static String USER;//登录用户名
    private static String PASSWORD;//生成私钥的密码和登录密码，这两个共用这个密码
    private static Connection connection;
//    private static String PRIVATEKEY = "C:\\Users\\ubuntu\\.ssh\\id_rsa";// 本机的私钥文件
//    private static boolean usePassword = false;// 使用用户名和密码来进行登录验证。如果为true则通过用户名和密码登录，false则使用rsa免密码登录

    /**
     * 初始化
     */
    public static void setConnection(String ip, int port, String user, String password) throws Exception {
        IP = ip;
        PORT = port;
        USER = user;
        PASSWORD = password;
        connection = new Connection(IP, PORT);
    }

    /**
     * ssh用户登录验证，使用用户名和密码来认证
     *
     * @param user
     * @param password
     * @return
     */
    public static boolean isAuthedWithPassword(String user, String password) {
        try {
            return connection.authenticateWithPassword(user, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isAuth() {
        //不考虑公钥的问题
        logger.debug("USER:{}", USER);
        logger.debug("PASSWORD:{}", PASSWORD);
        return isAuthedWithPassword(USER, PASSWORD);
    }

    /**
     * 下载文件
     *
     * @param remoteFile 服务器地址 /data/jar/appservice-1.0-SNAPSHOT.jar
     * @param path       本地地址
     */
    public static String getFile(String remoteFile, String path) throws BusinessException {
        try {
            connection.connect();
            boolean isAuthed = isAuth();
            if (isAuthed) {
                logger.info("认证成功!");
                SCPClient scpClient = connection.createSCPClient();
                try {
                    scpClient.get(remoteFile, path);
                } catch (IOException ex) {
                    logger.error("下载失败");
                    throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_FAILURE_IOERROR"));
                }
                String rFile = remoteFile.trim();
                String fileName = rFile.substring(rFile.lastIndexOf("/") + 1);
                logger.debug("文件路径及名称:{},{}",path,fileName);
                File file = new File(path + fileName);
                long length = file.length();
                if (length <= 0) {
                    logger.error("上传文件为空！");
                    file.delete();
                    throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_FAILURE_EMPTY"));
                }
                logger.info("下载成功!");
                String[] result = remoteFile.split("\\.");
                int size = result.length;
                logger.info("下载文件的类型：" + result[size - 1]);
                return result[size - 1];
            } else {
                logger.info("认证失败!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return "";
    }

    /**
     * 本地路径的jar全名
     */
    public static String getPathFileName(String remoteFile, String path) {
        return path + getFileName(remoteFile);
    }

    /**
     * 上传成功
     *
     * @param localFile
     * @param remoteTargetDirectory
     */
    public static void putFile(String localFile, String remoteTargetDirectory) {
        try {
            connection.connect();
            boolean isAuthed = isAuth();
            if (isAuthed) {
                logger.info("认证成功!");
                SCPClient scpClient = connection.createSCPClient();
                scpClient.put(localFile, remoteTargetDirectory);
                logger.info("上传成功!");
            } else {
                logger.info("认证失败!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            connection.close();
        }
    }

    /**
     * 压缩包
     *
     * @param inputFileName
     * @param zipFileName
     */
    public static void createZipFile(String inputFileName, String zipFileName) {

        try {
            logger.info("进入zip打包");
            //Create input and output streams
            FileInputStream inStream = new FileInputStream(inputFileName);
            ZipOutputStream outStream = new ZipOutputStream(new FileOutputStream(zipFileName));

            // Add a zip entry to the output stream
            //获取文件名称
            String fileName = FileUtil.getRealFileName(inputFileName);
            logger.debug("输入文件名称为:{}",fileName);
            outStream.putNextEntry(new ZipEntry(fileName));

            byte[] buffer = new byte[1024];
            int bytesRead;

            //Each chunk of data read from the input stream
            //is written to the output stream
            while ((bytesRead = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, bytesRead);
            }

            //Close zip entry and file streams
            outStream.closeEntry();

            outStream.close();
            inStream.close();
            logger.info("zip打包成功");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 删除下载的业务包 war包
     *
     * @param fileName
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            logger.info("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile()) {
                logger.info("删除文件成功:" + fileName);
                return deleteFile(fileName);
            }
        }
        return false;
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                logger.info("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                logger.info("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            logger.info("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * Linux服务器文件名提取
     *
     * @param file
     * @return
     */
    public static String getFileName(String file) {
        int len = file.length();
        StringBuffer sb = new StringBuffer(file);
        sb.reverse();
        char ch;
        int j = 0;

        for (int i = 0; i < sb.length(); i++) {
            ch = sb.charAt(i);
            if (ch == 47) {
                //  47/  92\
                j = i;
                break;
            }
        }
        return file.substring(len - j);
    }

    /**
     * 压缩包名称
     *
     * @param file
     * @return
     */
    public static String getZipName(String file) {
        String zipfile = file;
        int len = file.length();
        StringBuffer sb = new StringBuffer(file);
        sb.reverse();
        char ch;
        int j = 0;
        for (int i = 0; i < sb.length(); i++) {
            ch = sb.charAt(i);
            if (ch == 46) {
                // 46 .
                // 92 \
                j = i;
                break;
            }
        }
        zipfile = zipfile.substring(0, len - j) + "zip";
        return zipfile;
    }

    /**
     * flag 0:不覆盖 1：覆盖下载
     *
     * @return
     */
    public static String upload(String ip, int port, String user, String password, String localFile, String remoteTargetDirectory, int flag) throws Exception {
        setConnection(ip, port, user, password);
        putFile(localFile, remoteTargetDirectory);
        String separator = System.getProperty("file.separator");
        //1.window  获取本地的名称
        if ("\\".equals(separator)) {
            String judge[] = localFile.split("\\\\");
            int size = judge.length;
            return remoteTargetDirectory + judge[size - 1];
        } else if ("\\/".equals(separator)) {
            //2.本地为Linux
            return remoteTargetDirectory + getFileName(localFile);
        }
        return "failure";

    }

    /**
     * flag 0:不覆盖 1：覆盖下载
     *
     * @param remoteFile 远程路径+文件名
     * @param path       本地路径
     * @throws Exception
     */
    public static String download(String ip, int port, String user, String password, String remoteFile, String path, int flag) throws BusinessException {
        String result;
        try {
            setConnection(ip, port, user, password);
        } catch (Exception e) {
        }
        String file = getPathFileName(remoteFile, path);
        logger.info("路径业务包名称为file:" + file);
        String zipfile = getZipName(file);
        logger.info("路径业务包压缩包名称为zipfile:" + zipfile);
        //0.是否存在
        File fileDeal = new File(zipfile);
        if (fileDeal.exists()) {
            logger.info("下载文件:" + zipfile + "已存在，是否覆盖？");
            if (flag == 1) {
                delete(file);
                delete(zipfile);
            }
        }
        String type = getFile(remoteFile, path);
        logger.debug("下载文件类型:{}",type);
        //1.0判断下是否为zip格式
        if ("zip".equals(type)) {
            logger.info("下载类型为zip格式，不打包");
            result = "success";
        }else {
            //1.1非zip格式 即可打包
            //2.打包
            logger.debug("非zip格式 打包");
            createZipFile(file, zipfile);
            //3.删除原业务包
            logger.debug("file:{}",file);
            delete(file);
            result = "success";
        }
        return result;
    }


}
