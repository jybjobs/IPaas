package com.cloud.paas.appservice.dao;

import com.cloud.paas.appservice.model.SrvDetail;
import com.cloud.paas.appservice.qo.SrvCondition;
import com.cloud.paas.appservice.qo.SrvExample;
import com.cloud.paas.appservice.vo.app.AppDetailVO;
import com.cloud.paas.appservice.vo.srv.SrvDetailOrderVO;

import java.util.List;

/**
 * @Author: wangzhifeng
 * @Description:
 * @Date: Create in 19:18 2017/11/20
 * @Modified by:
 */
public interface SrvMngDAO extends BaseDAO<SrvDetail> {
    List<SrvDetail> doQueryAllSrv();

    /**
     * 所有用户下的所有应用的所有服务
     *
     * @return
     */
    public List<AppDetailVO> listUserAppSrvDetail();

    /**
     * 指定用户的指定应用下的所有服务列表
     *
     * @param srvCondition
     * @return List<SrvDetail>
     */
    public AppDetailVO listUserGivenAppGivenSrvDetail(SrvCondition srvCondition);

    /**
     * 查询指定用户下的指定应用指定服务
     *
     * @param srvCondition
     * @return
     */
    public AppDetailVO userGivenAppGivenSrvDetailGiven(SrvCondition srvCondition);

    /**
     * 改变服务的状态：1：开启，2：停止，3：扩容，4：缩容
     */
    public Integer SrvStatusChanged(SrvCondition srvCondition);


    /**
     * 页面条件查询
     * @param srvExample
     * @return
     */
	List<SrvDetailOrderVO> listAllSrvByConditions(SrvExample srvExample);

    List<SrvDetail> findPageByIdList(SrvExample srvExample);
    SrvDetail findNameEn(String nameEn);
    SrvDetail findNameZh(String nameZh);
    List<SrvDetail> findAll(int srvImageVersionId);
    SrvDetail findNodePort(int nodePort);


    List<SrvDetail> listUserSrvDetail(SrvExample srvExample);
}
