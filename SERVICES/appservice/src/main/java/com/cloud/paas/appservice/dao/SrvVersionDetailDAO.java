package com.cloud.paas.appservice.dao;

import com.cloud.paas.appservice.model.SrvVersionDetail;
import com.cloud.paas.appservice.qo.SrvVersionDetailExample;
import com.cloud.paas.appservice.vo.srv.SrvVersionDetailVO;

import java.util.List;

public interface SrvVersionDetailDAO  extends BaseDAO<SrvVersionDetail>{

    /**
     * 查询服务定义信息
     * @param srvVersionDetailExample
     * @return
     */
    List<SrvVersionDetailVO> listSrvVersionDetail(SrvVersionDetailExample srvVersionDetailExample);

    /**
     * 查询中间状态的镜像版本编号
     * @return
     */
    List<SrvVersionDetailVO> doFindProcessingImageVersionIds();

    /**
     * 根据ids查询服务定义详情
     * @param list
     * @return
     */
    List<SrvVersionDetail> listSrvVersionDetailByIds(List<Integer> list);
}