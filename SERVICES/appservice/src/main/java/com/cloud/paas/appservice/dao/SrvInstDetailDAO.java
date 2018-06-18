package com.cloud.paas.appservice.dao;

import com.cloud.paas.appservice.model.SrvInstDetail;
import com.cloud.paas.appservice.qo.SrvInstDetailExample;
import com.cloud.paas.appservice.vo.srv.SrvInstDetailVO;

import java.util.List;


public interface SrvInstDetailDAO  extends BaseDAO<SrvInstDetail>{

    /**
     * 获取节点端口
     * @return
     */
    String doFindNodePort();

    /**
     * 根据服务实例编号查询服务实例详情
     * @param srvInstDetail
     * @return
     */
    SrvInstDetailVO listSrvInstInfoByInstId(SrvInstDetail srvInstDetail);

    /**
     * 根据条件查询服务实例（列表）
     * @param srvInstDetailExample
     * @return
     */
    List<SrvInstDetailVO> listSrvInstByCondition(SrvInstDetailExample srvInstDetailExample);

    /**
     * 根据应用名称和服务名称查询服务实例
     * @param srvInstDetailExample
     * @return
     */
    Integer querySrvInstByAppAndSrv(SrvInstDetailExample srvInstDetailExample);

}