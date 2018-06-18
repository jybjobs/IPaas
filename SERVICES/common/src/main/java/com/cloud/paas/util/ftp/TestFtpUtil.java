package com.cloud.paas.util.ftp;

import java.text.SimpleDateFormat;
/**
 * Created by kaiwen on 2018/1/9.
 */
public class TestFtpUtil {
    public static void main(String agrs[]) {
        FtpFileUtil fu=new FtpFileUtil();
        String filepath[] = { "/FTPInstall.docx"};
        String localfilepath[] = { "E:\\acp_code\\test\\FTPInstall.docx" };
        /* * 使用默认的端口号、用户名、密码以及根目录连接FTP服务器 */
       // fu.connectServer("10.1.163.69", 21, "test", "123123");
        // 下载
        for (int i = 0; i < filepath.length; i++) {
           // fu.download(filepath[i], localfilepath[i]);
        }
      /*  String localfile = "E:\\acp_code\\ACP项目资料\\AxureRP_for_chorme.zip";
        String remotefile = "AxureRP_for_chorme.zip";
        // 上传
        fu.upload(localfile, remotefile);
        fu.closeConnect();*/
    }
}

