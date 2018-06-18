package com.cloud.paas.imageregistry.util;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.messages.ImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DockerUtil {

    private static final Logger logger = LoggerFactory.getLogger(DockerUtil.class);

    private static DockerClient docker = null;

    public static DockerClient getDocker() throws DockerCertificateException {
        if (docker == null) {
            docker = new DefaultDockerClient("unix:///var/run/docker.sock");
        }
        return docker;
    }

    public static DockerClient getDocker(String uri) throws DockerCertificateException {
        docker = DefaultDockerClient.fromEnv().uri(uri).build();
        return docker;
    }

    public static String imageNameDelPrefix(String imageName) {
        if (imageName == null) {
            return "";
        }
        String imageNameNew = "";
        String[] ds = imageName.split(".");
        if (ds.length > 1) {
            String[] xs = imageName.split("/");
            for (int i = 1; i < xs.length; i++) {
                imageNameNew += xs[i] + "/";
            }
            imageNameNew = imageNameNew.substring(imageNameNew.lastIndexOf("/") - 1);

        }
        return imageNameNew;
    }

    /**
     * 通过镜像id获取镜像的详细信息
     *
     * @param imageId 镜像id
     * @return 镜像详细信息
     */
    public static ImageInfo getImageInfoByImageId(String imageId) {
        ImageInfo imageInfo = null;
        try {
            imageInfo = getDocker().inspectImage(imageId);
            logger.debug("镜像详细信息:" + imageInfo.toString());
        } catch (Exception e) {
            logger.error("获取镜像详情失败:" + e.getMessage());
        }
        return imageInfo;
    }

    /**
     * 通过镜像id获取镜像大小
     *
     * @param imageId 镜像id
     * @return 镜像size(单位:MB)
     */
    public static float getImageSize(String imageId) {
        ImageInfo imageInfo = getImageInfoByImageId(imageId);
        if (null != imageInfo) {
            float imageSize = imageInfo.size() / 1048576;
            logger.debug("镜像大小-----------------------------:" + imageSize);
            return imageSize;
        }
        return 0;
    }


}
