package com.cloud.paas.imageregistry.util;


import com.cloud.paas.imageregistry.model.RegistryDetail;
import com.spotify.docker.client.messages.RegistryAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: wyj
 * @desc: 仓库工具类
 * @Date: Created in 2017/11/29 14:11
 * @Modified By:
 */
public class RegistryUtil extends RegistryDetail {

    private static final Logger logger = LoggerFactory.getLogger(RegistryUtil.class);


    /**
     * 仓库的具体ip地址：例如10.1.163.8:5000
     */
    private String registryAddress;
    /**
     * 仓库的具体的api地址,用于仓库认证
     */
    private String registryApiAddress;



    /**
     * 仓库验证
     */
    private static RegistryAuth registryAuth = null;

    /**
     * 获取仓库验证
     * @param apiAddress 仓库api地址
     * @return 仓库验证对象
     */
    public static RegistryAuth getRegistryAuth(String apiAddress) {
        if (registryAuth==null){
            registryAuth = RegistryAuth.builder().serverAddress(apiAddress).build();
        }
        if(registryAuth!=null){
            logger.info("获取镜像仓库成功");
        }else{
            logger.info("获取镜像仓库失败");
        }
        return registryAuth;
    }


    public String getRegistryAddress() {
        registryAddress=this.getRegistryIp()+":"+this.getRegistryPort()+"/";
        return registryAddress;
    }



    public String getRegistryApiAddress() {
        registryApiAddress="http://"+getRegistryAddress()+"/v2/";
        return registryApiAddress;
    }


}
