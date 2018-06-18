package com.cloud.paas.appservice.util.yaml.common;

/**
 * @Author: srf
 * @desc: OwnerReference对象
 * @Date: Created in 2018-03-29 10-55
 * @Modified By:
 */
public class OwnerReference {
    // api版本
    private String apiVersion;
    //阻塞owener删除
    private boolean blockOwnerDeletion;
    //控制器
    private boolean controller;
    // 类型
    private String kind;
    //名称
    private String name;
    //UID
    private String uid;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public boolean isBlockOwnerDeletion() {
        return blockOwnerDeletion;
    }

    public void setBlockOwnerDeletion(boolean blockOwnerDeletion) {
        this.blockOwnerDeletion = blockOwnerDeletion;
    }

    public boolean isController() {
        return controller;
    }

    public void setController(boolean controller) {
        this.controller = controller;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
