package com.cloud.paas.systemmanager.vo.dictory;

/**
 * @author: zcy
 * @desc: 字典内容视图类
 * @Date: Created in 2017-12-14 9:50
 * @modified By:
 **/
public class DictItemVO{
    private String dictItemName;
    private String dictItemCode;

    public String getDictItemName() {
        return dictItemName;
    }

    public void setDictItemName(String dictItemName) {
        this.dictItemName = dictItemName;
    }

    public String getDictItemCode() {
        return dictItemCode;
    }

    public void setDictItemCode(String dictItemCode) {
        this.dictItemCode = dictItemCode;
    }
}
