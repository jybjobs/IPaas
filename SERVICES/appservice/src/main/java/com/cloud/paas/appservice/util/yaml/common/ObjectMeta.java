package com.cloud.paas.appservice.util.yaml.common;

/**
 * @Author: wyj
 * @desc: 公共MetaData
 * @Date: Created in 2017-12-14 10:43
 * @Modified By:
 */
public class ObjectMeta {

    /**
     * 创建时间
     */
    private String creationTimestamp;
    /**
     * 标签（可以为多个）
     */
    private ObjectLabel labels;

    private String name;
    private String namespace;

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public ObjectLabel getLabels() {
        return labels;
    }

    public void setLabels(ObjectLabel labels) {
        this.labels = labels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
