package com.cloud.paas.appservice.dao;

import com.cloud.paas.appservice.model.CtnDetailInfo;
import com.cloud.paas.appservice.qo.SrvCondition;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author YHT
 * @create 2017-12-14 16:16
 **/
public interface CtnDetailInfoDAO extends BaseDAO<CtnDetailInfo> {
    /**
     * 查询所有容器信息
     *
     * @return
     */
    public List<CtnDetailInfo> listAllCtn();

    /**
     * 查询指定用户下所有应用所有服务下所有容器
     */
    public  List<CtnDetailInfo> listUserGivenAppSrvDetailCnts(Integer userId);

    /**
     * 查询指定用户下指定应用所有服务下所有容器
     */
    public List<CtnDetailInfo> listCtnUserGivenAppGivenSrv(SrvCondition srvCondition);

    /**
     * 查询指定用户下指定应用指定服务下所有容器
     */
    public List<CtnDetailInfo> userGivenAppGivenSrvDetailGivenCnts(SrvCondition srvCondition);


}
