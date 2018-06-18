package com.cloud.paas.systemmanager.service.impl.status;

import com.cloud.paas.systemmanager.dao.BaseDAO;
import com.cloud.paas.systemmanager.dao.StatusDAO;
import com.cloud.paas.systemmanager.qo.status.StatusCondition;
import com.cloud.paas.systemmanager.service.impl.BaseServiceImpl;
import com.cloud.paas.systemmanager.service.status.StatusService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cloud.paas.systemmanager.model.CodeStatus;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.config.ConfigUtil;
import com.cloud.paas.util.page.PageUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wyj
 * @desc:
 * @Date: Created in 2017-12-26 18:29
 * @Modified By:hzy
 */
@SuppressWarnings( { "serial", "rawtypes", "unchecked","Duplicates" })
@Service("StatusService")
public class StatusServiceImpl extends BaseServiceImpl<CodeStatus> implements StatusService {
    private static final Logger logger = LoggerFactory.getLogger(StatusServiceImpl.class);

    @Autowired
    private StatusDAO statusDAO;
    /**
     * 修改状态
     */
    @Override
    public Result doUpdateByCodeStatusBean(CodeStatus codeStatus) throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn("STATUS_UPDATE_FAILURE");
        //更新
        int id = codeStatus.getId();
        logger.debug("状态码id{}",id);
        int code = codeStatus.getCode();
        logger.debug("状态码{}",code);
        String codeEn=codeStatus.getCodeEn();
        logger.debug("状态码描述{}",codeEn);
        String msg = codeStatus.getMsg();
        logger.debug("状态码message{}",msg);
        //1.状态码查询是否已存在
        if(null != statusDAO.findStatusByCode(code) && statusDAO.findStatusByCode(code).getId() != id){
            result.setData("状态码已存在，记录修改失败！");
            return result;
        }
        //2.状态码查询英文名称是否已存在
        if(null != statusDAO.findStatusByCodeEn(codeEn) && statusDAO.findStatusByCodeEn(codeEn).getId() != id ){
            result.setData("状态码英文名称已存在，记录修改失败！");
            return result;
        }
        //检查msg是否有重复
        StatusCondition statusCondition = new StatusCondition();
        statusCondition.setCondition(msg);
        PageInfo pageInfo=(PageInfo)getCodeStatusByCondition(statusCondition).getData();
        List<CodeStatus> codeStatusList = pageInfo.getList();
        if(null != codeStatusList && codeStatusList.size()>1){
            result.setData("状态码message已存在");
            return result;
        }
        if(codeStatusList.size()==1&&codeStatusList.get(0).getMsg()!=msg){
            result.setData("状态码message已存在");
            return result;
        }

        result.setData(statusDAO.doUpdateByBean(codeStatus));
        result = CodeStatusUtil.resultByCodeEn("STATUS_UPDATE_SUCCESS");
        return result;
    }
    /**
     * 插入状态  是否已存在校验
     */
    @Override
    public Result doInsertByCodeStatusBean(CodeStatus codeStatus) throws BusinessException{
        Result result =CodeStatusUtil.resultByCodeEn("STATUS_INSERT_FAILURE");
        int code = codeStatus.getCode();
        logger.debug("状态码{}",code);
        String codeEn=codeStatus.getCodeEn();
        logger.debug("状态码描述{}",codeEn);
        //设置状态信息
        String msg = codeStatus.getMsg();
        logger.debug("状态码message{}",msg);
        //1.状态码查询是否已存在
        if(null != statusDAO.findStatusByCode(code)){
            result.setData("状态码已存在，请重新换个状态码！");
            return result;
        }
        //2.状态码查询英文名称是否已存在
        if(null != statusDAO.findStatusByCodeEn(codeEn)){
            result.setData("状态码英文名称已存在，请重新换个英文名称！");
            return result;
        }
        //3.检查msg是否有重复
        StatusCondition statusCondition = new StatusCondition();
        statusCondition.setCondition(msg);
        PageInfo pageInfo=(PageInfo)getCodeStatusByCondition(statusCondition).getData();
        List<CodeStatus> codeStatusList = null;
        codeStatusList = pageInfo.getList();

        if((null == codeStatusList || codeStatusList.size()==0)){
             result = CodeStatusUtil.resultByCodeEn("STATUS_INSERT_SUCCESS");
             result.setData(statusDAO.doInsertByBean(codeStatus));
        }
        else{
            result.setData("状态信息已存在");
        }
        return  result;
    }

    /**
     * 通过code查找详细状态
     * @param code 状态码
     * @return result对象
     */
    @Override
    public CodeStatus findStatusByCode(int code) throws BusinessException {
        CodeStatus codeStatus=null;
        if (code != 0){
            codeStatus= statusDAO.findStatusByCode(code);
            if (codeStatus != null){
                return codeStatus;
            }
            else{
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("NO_STATUS"));
            }
        }
        return null;
    }

    /**
     * 通过code描述查找详细状态
     * @param codeEn 英文描述
     * @return  result对象
     */
    @Override
    public CodeStatus findStatusByCodeEn(String codeEn) throws BusinessException{
        if (codeEn != null){
            CodeStatus status = statusDAO.findStatusByCodeEn(codeEn);
            if (status != null){
                return status;
            }
            else{
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("NO_STATUS"));
            }
        }
        return null;
    }

    /**
     * 获取所有的状态码
     * @return
     */
    @Override
    public Map<Integer, CodeStatus> selectAllStatus() throws BusinessException {
        List<CodeStatus> codeStatusList = statusDAO.selectAllStatus();
        if (codeStatusList != null && codeStatusList.size() != 0){
            Map<Integer,CodeStatus> map = new LinkedHashMap<>();
            for (CodeStatus codeStatus :codeStatusList){
                map.put(codeStatus.getCode(),codeStatus);
            }
            return map;
        }
        return null;
    }


    /**
     * 获取所有的状态码
     * @return
     */
    @Override
    public Map<String, CodeStatus> selectAllStatusByCodeEn() throws BusinessException{
        List<CodeStatus> codeStatusList = statusDAO.selectAllStatus();
        if (codeStatusList != null && codeStatusList.size() != 0){
            Map<String,CodeStatus> map = new LinkedHashMap<>();
            for (CodeStatus codeStatus :codeStatusList){
                map.put(codeStatus.getCodeEn(),codeStatus);
            }
            return map;
        }
        return null;
    }

    /**
     * 通过code获取指定状态
     * @param code
     * @return
     */
    @Override
    public com.cloud.paas.util.codestatus.CodeStatus getStatusCodeByCode(int code) throws BusinessException{
        com.cloud.paas.util.codestatus.CodeStatus codeStatus = null;
        try {
            codeStatus = CodeStatusUtil.getInstance().getStatusByCode(code);
            if (codeStatus != null){
                return codeStatus;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("NO_STATUS"));
        }
        return new com.cloud.paas.util.codestatus.CodeStatus(2079900,"NO_STATUS","未查询到该状态码",0,0,0);
    }

    /**
     * 通过codeen获取指定状态
     * @param codeEn 英文code
     * @return
     */
    @Override
    public com.cloud.paas.util.codestatus.CodeStatus getStatusCodeBtCodeEn(String codeEn) throws BusinessException{
        com.cloud.paas.util.codestatus.CodeStatus codeStatus = null;
        try {
            codeStatus = CodeStatusUtil.getInstance().getStatusByCodeEn(codeEn);
            if (codeStatus != null){
                return codeStatus;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("NO_STATUS"));
        }
        return new com.cloud.paas.util.codestatus.CodeStatus(2079900,"NO_STATUS","未查询到该状态码",0,0,0);
    }

    /**
     * 分页，得到一个id的list
     * @param statusCondition
     * @return
     */
    @Override
    public List<Integer> findStatusIdListByCondition(StatusCondition statusCondition) throws BusinessException{
        List<Integer> idList = new ArrayList<>();
        List<CodeStatus> codeStatusList =  statusDAO.findStatusIdListByCondition(statusCondition);
        for(CodeStatus codeStatus:codeStatusList){
            idList.add(codeStatus.getId());
        }
        return idList;
    }

    /**
     * 根据条件（状态码/状态信息）查询状态码
     * @param statusCondition
     * @return
     */
    @Override
    public Result getCodeStatusByCondition(StatusCondition statusCondition) throws BusinessException{
        Result result =CodeStatusUtil.resultByCodeEn("STATUS_QUERY_PAGE_FAILURE");
        //页码
        int pageNum=statusCondition.getPageNum();
        logger.debug("页码{}",pageNum);
        //每页放的数据条数
        int pageSize=statusCondition.getPageSize();
        logger.debug("每页放的数据条数{}",pageSize);
        Page page= PageHelper.startPage(pageNum, pageSize);
        //存放id的List
        List<Integer> idList=this.findStatusIdListByCondition(statusCondition);
        //分页
        PageInfo pageInfo= PageUtil.getPageInfo(page,idList);
        List<CodeStatus> codeStatusList = statusDAO.findCodeStatusList(idList);
        pageInfo.setList(codeStatusList);
        if(null != pageInfo){
            result=CodeStatusUtil.resultByCodeEn("STATUS_QUERY_PAGE_SUCCESS");
            result.setData(pageInfo);
        }
        return result;
    }

    @Override
    public BaseDAO<CodeStatus> getBaseDAO() {
        return statusDAO;
    }
}
