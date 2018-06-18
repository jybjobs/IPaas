package com.cloud.paas.util.fileupload;


import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.result.Result;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * 文件上传
 * Created by kaiwen on 2017/11/29.
 */
public class UploadFile {

    public Result uploadFile(MultipartFile file, String filePath) throws BusinessException {
        Result result;
        //判断文件是否为空
        if (file.isEmpty()) {
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_FAILURE_EMPTY"));
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //获取文件类型
        String filetype = file.getContentType();
        //获取文件大小
        long size = file.getSize();
        // 重命名
        fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        //上传文件
        try {
            //上传
            file.transferTo(dest);
            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String today = sdf.format(date);
            result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_SUCCESS");
            Map<String,String> sMap = new HashMap<String, String>();
            sMap.put("path",fileName);
            result.setData(sMap);
            //上传后输出上传成功的时间
            return result;
        }catch (IllegalStateException e) {
            //文件过大,内存溢出异常
           result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_FAILURE_TOOBIG");
           return result;
        }catch (IOException e) {
          //  "文件路径错误,IO异常";
            result = CodeStatusUtil.resultByCodeEn("BUSI_PKG_UPLOAD_FAILURE_IOERROR");
            return result;
        }
    }


    public String getFileName(String localFilePath) {
        File tempFile =new File(localFilePath.trim());
        String fileName = tempFile.getName();
        return fileName;
    }

    /**
     * 获取原文件名+后缀
     * @param file
     * @return
     */
    public String getOriginalFileName(MultipartFile file){
        // 获取文件名
        String fileName = file.getOriginalFilename();
        return fileName;
    }

    public String getSuffixName(File file){
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return suffix;
    }

    /**
     * 获得文件大小，单位为MB，保留2位小数
     * @param file
     * @return
     */
    public String getFileSize(File file){
        String size = "";
        if (file.exists()&&file.isFile()){
            long fileS = file.length();
            DecimalFormat df = new DecimalFormat("#.000000");
            size = df.format((double) fileS / 1048576);
        }else  if(file.exists() && file.isDirectory()){
            size = "";
        }else {
            size = "0";
        }
        return size;
    }

    /**
     * 将单位为kb的文件大小转换为KB、MB、GB为单位，保留2位小数
     * @param fileS
     * @return
     */
    public String changeFileSize(long fileS){
        String size = "";
        DecimalFormat df = new DecimalFormat("#.00");
        if(fileS < 1024){
            size = df.format((double) fileS)+"KT";
        } else if (fileS < 1048576) {
            size = df.format((double) fileS / 1024) + "MB";
        } else if (fileS < 1073741824) {
            size = df.format((double) fileS / 1048576) + "GB";
        }
        return size;
    }
}
