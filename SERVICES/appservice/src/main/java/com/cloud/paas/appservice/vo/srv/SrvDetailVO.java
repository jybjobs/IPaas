package com.cloud.paas.appservice.vo.srv;

import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.appservice.model.SrvDetail;
import com.cloud.paas.appservice.model.SrvPersistentRel;
import com.cloud.paas.util.codestatus.CodeStatusContant;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by 17798 on 2018/03/27.
 */
public class SrvDetailVO extends SrvDetail {

    private final static Logger logger = LoggerFactory.getLogger(SrvDetailVO.class);

    private List<SrvPersistentRel> srvPersistentRels;

    private JSONObject srvPersistentRelJson;

    private String base64Img;

    public JSONObject getSrvPersistentRelJson() {
        return srvPersistentRelJson;
    }

    public void setSrvPersistentRelJson(JSONObject srvPersistentRelJson) {
        this.srvPersistentRelJson = srvPersistentRelJson;
    }

    public List<SrvPersistentRel> getSrvPersistentRels() {
        return srvPersistentRels;
    }

    public void setSrvPersistentRels(List<SrvPersistentRel> srvPersistentRels) {
        this.srvPersistentRels = srvPersistentRels;
    }

    public String getBase64Img() {
        return base64Img;
    }

    public void setBase64Img(String srvDescImgPath) {
        ByteArrayOutputStream bos = null;
        if(srvDescImgPath == null){
            return;
        }
        try {
            //1.校验该文件是否存在
            File file = new File(srvDescImgPath);
            if (file.exists()) {
                bos = new ByteArrayOutputStream();
                //2.截取文件format格式
                String formatName = srvDescImgPath.substring(srvDescImgPath.lastIndexOf(".") + 1);
                //3.读取文件到图片流
                BufferedImage im = ImageIO.read(file);
                //4.写入到ByteArrayOutputStream
                ImageIO.write(im,formatName,bos);
                //5.转换成base64位的形式赋值给base64Img
                this.base64Img = new BASE64Encoder().encode(bos.toByteArray());
            }
        }catch (IOException e){
            logger.error("读取文件失败");
        }finally {
            if(bos != null){
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    logger.error("读取文件失败");
                }

            }
        }
    }
}
