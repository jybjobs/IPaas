package com.cloud.paas.appservice.vo.app;

import com.cloud.paas.appservice.model.AppDetail;
import com.cloud.paas.appservice.vo.srv.SrvDetailOrderVO;

import java.util.List;

/**
 * 考虑到排序的问题
 * Created by CSS on 2017/12/14.
 */
public class AppSrvListOrderVO extends AppDetail{

    /**
     * 包含启动顺序的服务列表
     */
    List<SrvDetailOrderVO> listSrvDetailOrder;

    public List<SrvDetailOrderVO> getListSrvDetailOrder() {
        return listSrvDetailOrder;
    }

    public void setListSrvDetailOrder(List<SrvDetailOrderVO> listSrvDetailOrder) {
        this.listSrvDetailOrder = listSrvDetailOrder;
    }


}
