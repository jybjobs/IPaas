package com.cloud.paas.systemmanager.service.impl.dictory;


import com.cloud.paas.systemmanager.dao.BaseDAO;
import com.cloud.paas.systemmanager.service.impl.BaseServiceImpl;
import com.cloud.paas.systemmanager.vo.dictory.Dict;
import com.cloud.paas.systemmanager.vo.dictory.DictTypeItemVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cloud.paas.systemmanager.dao.DictTypeDAO;
import com.cloud.paas.systemmanager.model.DictType;
import com.cloud.paas.systemmanager.qo.dictionary.DictCondition;
import com.cloud.paas.systemmanager.service.dictory.DictTypeService;
import com.cloud.paas.systemmanager.util.Config;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.date.DateUtil;
import com.cloud.paas.util.page.PageUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
@Service("dictTypeService")
public class DictTypeServiceImpl extends BaseServiceImpl<DictType> implements DictTypeService {
    private static final Logger logger = LoggerFactory.getLogger(DictTypeServiceImpl.class);
    private DateUtil dateUtil = new DateUtil();
    private Date date = null;
    @Autowired
    private DictTypeDAO dictTypeDAO;

    @Override
    public BaseDAO<DictType> getBaseDAO() {
        return dictTypeDAO;
    }

    @Override
    public Result insertDict(DictType dictType) throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn ("DICT_CREATE_FAILURE");
        logger.debug ("校验名称和标题是否重复-------");
        DictType dictType1 = doFindByName (dictType.getDictTypeName ());
        if (dictType1!=null){
            result.setMessage ("字典类型已存在");
            result.setData("字典类型已存在,记录修改失败！");
            return result;
        }
        dictType1 = dictTypeDAO.doFindByTitle (dictType.getTitle ());
        if (dictType1!=null){
            result.setMessage ("标题已经存在");
            result.setData("字典标题已存在,记录修改失败！");
            return result;
        }
        if (doInsertByBean(dictType)==1){
            result = CodeStatusUtil.resultByCodeEn ("DICT_CREATE_SUCCESS");
            result.setData(dictType.getDictTypeId());
        }
        return result;
    }

    /**
     * 修改字典类型
     * @param dictType
     * @return
     */
    @Override
    public Result updateDict(DictType dictType) throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn ("DICT_UPDATE_FAILURE");
        logger.debug ("更新时校验标题是否存在！");
        int dictTypeId = dictType.getDictTypeId();
        DictType dictType1 = dictTypeDAO.doFindByTitle (dictType.getTitle ());
        if (dictType1!=null){
            int dictTypeId1 = dictType1.getDictTypeId();
            if(dictTypeId!=dictTypeId1){
                result.setMessage ("标题已经存在");
                result.setData("字典标题已存在,记录修改失败！");
                return result;
            }
        }
        if (doUpdateByBean(dictType)==1){
            result = CodeStatusUtil.resultByCodeEn ("DICT_UPDATE_SUCCESS");
            result.setData(dictType.getDictTypeId());
        }
        return result;
    }

    /**
     * 获取全部字典类型
     *
     * @return
     */
    @Override
    public Result listDictType() throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn("DICT_TYPE_QUERY_SUCCESS");
        List<DictType> dictTypeList;
        try{
            dictTypeList = dictTypeDAO.listDictType();
            if(dictTypeList==null){
                logger.info("获取全部字典类型失败，未发现字典类型");
                result = CodeStatusUtil.resultByCodeEn("DICT_TYPE_QUERY_FAILURE");
                throw new BusinessException(result);
            }
        }catch(Exception e){
            logger.info("获取全部字典类型失败，删除数据异常！");
            result = CodeStatusUtil.resultByCodeEn("DICT_TYPE_QUERY_FAILURE");
            throw new BusinessException(result);
        }
        result.setData(dictTypeList);
        return result;
    }

    /**
     * 设置字典类型不启用状态
     *
     * @param dictTypeId
     * @return
     */
    @Override
    public int disableDictType(int dictTypeId) throws BusinessException {
        logger.info("设置字典类型不启用状态");
        DictType dictType = new DictType();
        dictType.setDictTypeId(dictTypeId);
        dictType.setDisableFlag(1);
        date = dateUtil.getCurrentTime();
        dictType.setUpdateTime(date);
        return dictTypeDAO.toggleDisableDictType(dictType);
    }

    /**
     * 设置字典类型启用状态
     *
     * @param dictTypeId
     * @return
     */
    @Override
    public int enableDictType(int dictTypeId) throws BusinessException{
        logger.info("设置字典类型启用状态");
        DictType dictType = new DictType();
        dictType.setDictTypeId(dictTypeId);
        dictType.setDisableFlag(0);
        date = dateUtil.getCurrentTime();
        dictType.setUpdateTime(date);
        return dictTypeDAO.toggleDisableDictType(dictType);
    }

    /**
     * 设置字典类型删除状态
     *
     * @param dictTypeId
     * @return
     */
    @Override
    public Result delDictType(int dictTypeId) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("DICT_DELET_SUCCESS");
        try {
            DictType dictType = new DictType();
            dictType.setDictTypeId(dictTypeId);
            dictType.setDelFlag(1);
            if (dictTypeDAO.toggleDelDictType(dictType) != 1) {
                logger.info("删除字典失败，未发现需删除的记录！");
                result = CodeStatusUtil.resultByCodeEn("DICT_DELET_FAILURE");
                throw new BusinessException(result);
            }
        } catch (Exception e) {
            logger.info("字典删除失败，异常");
            result = CodeStatusUtil.resultByCodeEn("DICT_DELET_FAILURE");
            throw new BusinessException(result);
        }
        return result;
    }

    /**
     * 设置字典类型恢复状态
     *
     * @param dictTypeId
     * @return
     */
    @Override
    public int recoverDictType(int dictTypeId) throws BusinessException{
        DictType dictType = new DictType();
        dictType.setDictTypeId(dictTypeId);
        dictType.setDelFlag(0);
        return dictTypeDAO.toggleDelDictType(dictType);
    }

    /**
     * 根据类型名称查找字典类型
     *
     * @param dictTypeName
     * @return
     */
    @Override
    public DictType doFindByName(String dictTypeName) throws BusinessException{
        return dictTypeDAO.doFindByName(dictTypeName);
    }

    /**
     * 根据多个字典类型名称列出字典类型及内容
     *
     * @param dictTypeNames
     * @return
     */
    @Override
    public Result getItemByTypeName(List<String> dictTypeNames) throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn("DICT_QUERY_SUCCESS");
        List<DictTypeItemVO> dictTypeItemVOList;
        try{
            dictTypeItemVOList = dictTypeDAO.getItemByTypeName(dictTypeNames);
            if (dictTypeItemVOList == null){
                logger.info("根据多个字典类型名称列出字典类型及内容失败，未发现内容！");
                result = CodeStatusUtil.resultByCodeEn("DICT_QUERY_FAILURE");
                throw new BusinessException(result);
            }
        }catch (Exception e){
            logger.info("根据多个字典类型名称列出字典类型及内容失败，查询出现异常！");
            result = CodeStatusUtil.resultByCodeEn("DICT_QUERY_FAILURE");
            throw new BusinessException(result);
        }
        result.setData(dictTypeItemVOList);
        return result;
    }

    /**
     * 分页，获取所有字典类型的id
     * @param dictCondition
     * @return
     */
    @Override
    public List<Integer> findDictIdListByCondition(DictCondition dictCondition) throws BusinessException {
        logger.info("分页，获取所有字典类型的id");
        List<DictType> dictList = dictTypeDAO.findDictIdListByCondition(dictCondition);
        List<Integer> idList = new ArrayList<>();
        for(DictType dictType:dictList){
            idList.add(dictType.getDictTypeId());
        }
        return idList;
    }
    /**
     * 分页查询
     */
    @Override
    public Result findDictIdListByConditionResult(DictCondition dictCondition) throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn("PAGE_DICT_FAILURE");
        //1.分页查询业务包Id列表
        //xml 写为List的时候 pageNum 页码 pageSize 每页的记录数
        int pageNum=dictCondition.getPageNum();
        int pageSize=dictCondition.getPageSize();
        logger.debug("pageNum"+pageNum+" pageSize:"+pageSize);
        Page page= PageHelper.startPage(pageNum, pageSize);
        List<Integer> idList=this.findDictIdListByCondition(dictCondition);
        PageInfo pageInfo= PageUtil.getPageInfo(page,idList);
        List<Dict> dictlist=dictTypeDAO.findDictIdListByConditionResult(idList);
        pageInfo.setList(dictlist);
        if(null != pageInfo){
            logger.info("分页字典成功");
            result = CodeStatusUtil.resultByCodeEn("PAGE_DICT_SUCCESS");
            result.setData(pageInfo);
        }
        return result;
    }

    /**
     * 根据条件查询
     * @param condition
     * @return
     */
    @Override
    public Result searchDict(DictCondition condition) throws BusinessException{
        logger.info("条件查询中");
        Result result = CodeStatusUtil.resultByCodeEn("DICT_QUERY_SUCCESS");
        List<Dict> dictList;
        /**
         * 查询字典
         */
        try {
            dictList = dictTypeDAO.searchDict(condition);
            if(dictList == null){
                result = CodeStatusUtil.resultByCodeEn("DICT_QUERY_FAILURE");
                throw new BusinessException(result);
            }
        }catch (Exception e){
            logger.info("条件查询失败，异常");
            result = CodeStatusUtil.resultByCodeEn("DICT_QUERY_FAILURE");
            throw new BusinessException(result);
        }
        result.setData(dictList);
        return result;
    }

    /**
     * 获取所有字典
     *
     * @return
     */
    @Override
    public Result getAllDict() throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn("DICT_QUERY_SUCCESS");
        //Result result = new Result(1, "10100", "获取所有字典成功", 3, 3, null);
        List<Dict> dictList = new ArrayList<Dict>();
        try {
            dictList = dictTypeDAO.getAllDict();
            if(dictList == null){
                logger.info("获取所有字典失败，未发现相应记录！");
                result = CodeStatusUtil.resultByCodeEn("DICT_QUERY_FAILURE");
                throw new BusinessException(result);
            }
        }catch (Exception e){
            logger.info("获取所有字典失败，数据异常！");
            result = CodeStatusUtil.resultByCodeEn("DICT_QUERY_FAILURE");
            throw new BusinessException(result);
        }
        result.setData(dictList);
        return result;
    }

    /**
     * 插入字典类型
     *
     * @param bean
     * @return
     */
    @Override

    public int doInsertByBean(DictType bean) throws BusinessException{
        date = dateUtil.getCurrentTime();
        bean.setCreateTime(date);
        bean.setUpdateTime(date);
        bean.setCreator(Config.Creator_Admin);
        return super.doInsertByBean(bean);
    }

    /**
     * 编辑字典类型
     *
     * @param bean
     * @return
     */
    @Override
    public int doUpdateByBean(DictType bean) throws BusinessException{
        date = dateUtil.getCurrentTime();
        bean.setUpdateTime(date);
        return super.doUpdateByBean(bean);
    }
}
