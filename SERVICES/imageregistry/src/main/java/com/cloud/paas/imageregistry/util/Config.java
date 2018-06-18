package com.cloud.paas.imageregistry.util;

public class Config {
    /**
     * 上传的业务包
     */
    public static String UPLOAD_DIRECTORY_BUSIPKG = "/data/upload/busipkg/";
//    public static String UPLOAD_DIRECTORY_BUSIPKG = "D:/uploadAdr";
    /**
     * 上传的业务包图片
     */
    public static String UPLOAD_DIRECTORY_BUSIPKG_IMG = "/data/upload/busipkg/img/";
    /**
     * 镜像上传的地址
     */
    public static String UPLOAD_DIRECTORY_IMAGE = "/data/upload/image/";
    /**
     * 镜像上传的图片地址
     */
    public static String UPLOAD_DIRECTORY_IMAGE_IMG = "/data/upload/image/img";
    /**
     * dockerfile临时文件夹
     */
    public static String TEMP_DIRECTORY_DOCKERFILE = "/data/temp/dockerfile/";
    /**
     * 仓库地址
     */
    public static String REGISTRY_ADDRESS = "10.1.163.8:5000/";
    /**
     * 仓库api地址
     */
    public static String REGISTRY_API_V2 = "http://10.1.163.8:5000/v2/";
    /**
     * 业务包 业务版本默认设置的创建者为admin
     */
    public static String Creator_Admin = "admin";


    /**
     * 仓库所使用的镜像和版本
     */
    public static String REGISTRY_IMAGE_INFO = "10.1.162.171:5000/registry:2.6";

    /**
     * 仓库主机的Docker Daemon http端口
     */
    public static int REGISTRY_DD_PORT =2375;


    /**
     * 仓库容器默认端口
     */
    public static String REGISTRY_CONTAINER_PORT = "5000";

    /**
     * 仓库容器配置文件
     */
    public static String REGISTRY_CONTAINER_CONFIG = "/etc/docker/registry/config.yml";

    /**
     * 仓库容器配置文件映射到宿主机
     */
    public static String REGISTRY_HOST_CONFIG = "/data/registry/conf/config.yml";

    /**
     * 仓库容器配置文件映射位置
     */
    public static String REGISTRY_HOST_STORAGE_PATH = "/data/registry/";

    /**
     * 仓库容器配置文件映射位置
     */
    public static String REGISTRY_CONTAINER_STORAGE_PATH = "/var/lib/registry/";



}
