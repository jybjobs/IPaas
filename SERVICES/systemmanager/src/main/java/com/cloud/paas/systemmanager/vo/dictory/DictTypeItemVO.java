package com.cloud.paas.systemmanager.vo.dictory;

import java.util.List;

/**
 * @author: zcy
 * @desc: 字典类型详细信息实体类，包含字典类型和对应键值对
 * @Date: Created in 2017-12-13 16:58
 * @modified By:
 **/
public class DictTypeItemVO{
    private String dictTypeName;
    private String Title;
    private String remark;
    private List<DictItemVO> listDictItem;


    public String getDictTypeName() {
        return dictTypeName;
    }

    public void setDictTypeName(String dictTypeName) {
        this.dictTypeName = dictTypeName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String dictTypeTitle) {
        this.Title = dictTypeTitle;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<DictItemVO> getListDictItem() {
        return listDictItem;
    }

    public void setListDictItem(List<DictItemVO> listDictItem) {
        this.listDictItem = listDictItem;
    }
}
