package com.cloud.paas.containerconsole.util;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import java.util.*;

/**
 * docker操作类.
 * @author will
 */
public class DockerHelper {
    public static void execute(String ip,DockerAction dockerAction)throws Exception{
        DockerClient docker = DefaultDockerClient.builder().uri("http://".concat(ip).concat(":2375")).apiVersion("v1.24").build();
        dockerAction.action(docker);
        docker.close();
    }

    public static <T> T query(String ip,DockerQuery<T> dockerQuery)throws Exception{
        DockerClient docker = DefaultDockerClient.builder().uri("http://".concat(ip).concat(":2375")).apiVersion("v1.24").build();
        T result=dockerQuery.action(docker);
        docker.close();
        return result;
    }

    public interface DockerAction {
        void action(DockerClient docker) throws Exception;
    }

    public interface DockerQuery<T> {
        T action(DockerClient docker) throws Exception;
    }

    /**
     * 通过容器名称获取容器ID
     * @param docker
     * @param containerName
     * @return
     */
    public static String findContainerIdByName (DockerClient docker, String containerName) {
        try {
            List<Container> containers = docker.listContainers();
            for (Container container : containers) {
                if (containerName.equals(container.labels().get("io.kubernetes.pod.name"))) {
                    return container.id();
                }
            }
        } catch (DockerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
