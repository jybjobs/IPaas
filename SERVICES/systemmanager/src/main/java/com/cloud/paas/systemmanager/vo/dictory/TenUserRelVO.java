package com.cloud.paas.systemmanager.vo.dictory;

import com.cloud.paas.systemmanager.model.TenUserRel;

import java.util.ArrayList;

public class TenUserRelVO extends TenUserRel {
    /**
     * 租户人员
     */
    private ArrayList<TenUserRel> tenUserRelList;

    public ArrayList<TenUserRel> getTenUserRelList() {
        return tenUserRelList;
    }

    public void setTenUserRelList(ArrayList<TenUserRel> tenUserRelList) {
        this.tenUserRelList = tenUserRelList;
    }
}
