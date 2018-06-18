package com.cloud.paas.systemmanager.vo.dictory;

/**
 * 字典内容排序入参类型
 */
public class ItemSort {
    private int dictItemId;
    private int sort;

    public int getDictItemId() {
        return dictItemId;
    }

    public void setDictItemId(int dictItemId) {
        this.dictItemId = dictItemId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
