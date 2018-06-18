package com.cloud.paas.appservice.vo.srv;

import com.cloud.paas.appservice.model.DependencyStorage;
import com.cloud.paas.appservice.model.SrvDeployment;
import com.cloud.paas.appservice.model.SrvDetail;

import java.util.List;

/**
 * Created by 17798 on 2018/5/18.
 */
public class SrvDeploymentVO extends SrvDeployment {

    private List<SrvInstDetailVO> listSrvInstDetail;

    private SrvDetail srvDetail;

    private String mountDir;

    private String newDir;

    private String storage;

    private DependencyStorage dependencyStorage;

    public List<SrvInstDetailVO> getListSrvInstDetail() {
        return listSrvInstDetail;
    }

    public void setListSrvInstDetail(List<SrvInstDetailVO> listSrvInstDetail) {
        this.listSrvInstDetail = listSrvInstDetail;
    }

    public SrvDetail getSrvDetail() {
        return srvDetail;
    }

    public void setSrvDetail(SrvDetail srvDetail) {
        this.srvDetail = srvDetail;
    }

    public String getMountDir() {
        return mountDir;
    }

    public void setMountDir(String mountDir) {
        this.mountDir = mountDir;
    }

    public String getNewDir() {
        return newDir;
    }

    public void setNewDir(String newDir) {
        this.newDir = newDir;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public DependencyStorage getDependencyStorage() {
        return dependencyStorage;
    }

    public void setDependencyStorage(DependencyStorage dependencyStorage) {
        this.dependencyStorage = dependencyStorage;
    }
}
