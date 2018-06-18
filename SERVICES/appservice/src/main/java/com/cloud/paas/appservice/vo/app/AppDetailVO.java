package com.cloud.paas.appservice.vo.app;

import com.cloud.paas.appservice.model.AppDetail;
import com.cloud.paas.appservice.model.SrvDetail;
import com.cloud.paas.appservice.vo.srv.SrvDetailVO;

import java.util.List;

/**
 * Created by CSS on 2017/12/6.
 */
public class AppDetailVO extends AppDetail{
    /**
     * 服务详情VO
     */
    List<SrvDetailVO> listSrvDetailVO;

    SrvDetailVO srvDetailVO;

    /**
     * 服务详情
     * @return
     */
    List<SrvDetail> listSrvDetail;

    SrvDetail srvDetailO;

    public SrvDetailVO getSrvDetailVO() {
        return srvDetailVO;
    }

    public void setSrvDetailVO(SrvDetailVO srvDetailVO) {
        this.srvDetailVO = srvDetailVO;
    }

    public SrvDetail getSrvDetailO() {
        return srvDetailO;
    }

    public void setSrvDetailO(SrvDetail srvDetailO) {
        this.srvDetailO = srvDetailO;
    }

    public List<SrvDetail> getListSrvDetail() {
        return listSrvDetail;
    }

    public void setListSrvDetail(List<SrvDetail> listSrvDetail) {
        this.listSrvDetail = listSrvDetail;
    }

    public List<SrvDetailVO> getListSrvDetailVO() {
        return listSrvDetailVO;
    }

    public void setListSrvDetailVO(List<SrvDetailVO> listSrvDetailVO) {
        this.listSrvDetailVO = listSrvDetailVO;
    }






}
