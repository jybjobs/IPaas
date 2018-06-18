package com.cloud.paas.appservice.dao;

import com.cloud.paas.appservice.model.SrvDeployment;
import com.cloud.paas.appservice.qo.SrvDeploymentExample;
import com.cloud.paas.appservice.vo.srv.SrvDeploymentVO;

import java.util.List;

public interface SrvDeploymentDAO extends BaseDAO<SrvDeployment> {

    List<SrvDeploymentVO> listSrvDeploymentWithSrvInst(SrvDeploymentExample srvDeploymentExample);

    List<SrvDeployment> doFindBySrvId(Integer srvId);

}