package com.cloud.paas.systemmanager.service.impl.dictory;

import com.cloud.paas.systemmanager.dao.BaseDAO;
import com.cloud.paas.systemmanager.dao.DictItemDAO;
import com.cloud.paas.systemmanager.model.DictItem;
import com.cloud.paas.systemmanager.service.dictory.DictItemService;
import com.cloud.paas.systemmanager.service.impl.BaseServiceImpl;
import com.cloud.paas.systemmanager.util.Config;
import com.cloud.paas.systemmanager.vo.dictory.ItemSort;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.date.DateUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author: zcy
 * @desc: 字典内容service实现
 * @Date: Created in 2017-12-14 14:18
 * @modified By:
 **/

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
@Service("dictItemService")
public class DictItemServiceImpl extends BaseServiceImpl<DictItem> implements DictItemService {
    private static final Logger logger = LoggerFactory.getLogger(DictItemServiceImpl.class);
    private DateUtil dateUtil = new DateUtil();
    private Date date = null;
    @Autowired
    private DictItemDAO dictItemDAO;

    @Override
    public BaseDAO<DictItem> getBaseDAO() {
        return dictItemDAO;
    }

    /**
     * 编辑字典内容
     * @param dictItem
     * @return
     */
    @Override
    public int doUpdateByBean(DictItem dictItem) throws BusinessException{
        date = dateUtil.getCurrentTime();
        dictItem.setUpdateTime(date);
        return super.doUpdateByBean(dictItem);
    }

    /**
     * 根据字典内容编号删除字典内容
     * @param dictItemId
     * @return
     */
    @Override
    public Result delDictItem(int dictItemId) throws BusinessException {
         Result result = CodeStatusUtil.resultByCodeEn("DICT_DELET_SUCCESS");
        //Result result = new Result(1, "10100", "删除字典内容成功", 3, 3, null);
        try {
            DictItem dictItem = new DictItem();
            dictItem.setDictItemId(dictItemId);
            dictItem.setDelFlag(1);
            if (dictItemDAO.toggleDelDictItem(dictItem) != 1) {
                logger.info("删除字典内容失败，未发现需要删除的字典内容！");
                result = CodeStatusUtil.resultByCodeEn("DICT_DELET_FAILURE");
                throw new BusinessException(result);
            }
        }catch (Exception e){
            result = CodeStatusUtil.resultByCodeEn("DICT_DELET_FAILURE");
            result.setMessage("刪除字典內容失敗，刪除数据异常！");
            throw new BusinessException(result);
        }
        return result;
    }

    /**
     * 创建字典内容
     * @param dictItem
     * @return
     */
    @Override
    public Result insertDictItem(DictItem dictItem) throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn("DICT_CREATE_FAILURE");
        String message = result.getMessage();
        if (!verifyCode(result,dictItem).getMessage().equals(message)){
            return  result;
        }
        else {
            if (!verify(result,dictItem).getMessage().equals(message)){
                return  result;
            }
            else{
                if (doInsertByBean(dictItem)==1){
                    result = CodeStatusUtil.resultByCodeEn("DICT_CREATE_SUCCESS");
                    result.setData(dictItem.getDictItemId());
                }
            }
        }
        return result;
    }

    public Result verify(Result result,DictItem dictItem)throws BusinessException{
        logger.debug ("校验名称！-------------");
        DictItem dictItem1 = dictItemDAO.findDictItemName(dictItem);
        if (dictItem1!=null && dictItem.getDictItemId()!=dictItem1.getDictItemId()){
            result.setMessage ("字典名称已存在");
            result.setData("字典名称已存在,记录修改失败！");
        }
        return  result;
    }
    public Result verifyCode(Result result,DictItem dictItem)throws BusinessException{
        logger.debug ("校验编码！-------------");
        DictItem dictItem1 = dictItemDAO.findDictItemCode(dictItem);
        if (dictItem1!=null && dictItem1.getDictItemId()!=dictItem.getDictItemId()){
            result.setMessage ("字典编码已存在");
            result.setData("字典编码已存在,记录修改失败！");
        }
        return  result;
    }
    @Override
    public Result updateDictItem(DictItem dictItem) throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn ("DICT_UPDATE_FAILURE");
        String message = result.getMessage ();
        if (!verifyCode(result,dictItem).getMessage ().equals (message)){
            return  result;
        }
        if (!verify(result,dictItem).getMessage ().equals (message)){
            return  result;
        }
        if (doUpdateByBean(dictItem)==1){
            result = CodeStatusUtil.resultByCodeEn ("DICT_UPDATE_SUCCESS");
            result.setData(dictItem.getDictItemId());
        }
        return result;
    }

    /**
     * 字典内容排序
     *
     * @param itemSorts
     * @return
     */
    @Override
    public Result sortDictItem(List<ItemSort> itemSorts) throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn ("DICT_ORDER_FAILURE");
        //Result result = new Result(0, "10100", "修改排序数据库保存错误", 3, 3, null);
        for (ItemSort itemSort : itemSorts) {
            DictItem dictItem = dictItemDAO.doFindById(itemSort.getDictItemId());
            dictItem.setDictItemId(itemSort.getDictItemId());
            dictItem.setSort(itemSort.getSort());
            if (doUpdateByBean(dictItem) == 0) {
                result = CodeStatusUtil.resultByCodeEn ("DICT_ORDER_FAILURE");
                throw new BusinessException(result);
            }
        }
        result = CodeStatusUtil.resultByCodeEn("DICT_ORDER_SUCCESS");
        result.setMessage("修改排序数据库保存成功");
        return result;
    }

    @Override
    public Result doSearchByTypeId(int dictTypeId) throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn ("DICT_QUERY_SUCCESS");
        //Result result = new Result(1, "10100", "查询成功", 3, 3, null);
        result.setData(dictItemDAO.doSearchByTypeId(dictTypeId));
        return result;
    }

    /**
     * 插入字典内容
     */
    @Override
    public int doInsertByBean(DictItem bean) throws BusinessException{
        date=dateUtil.getCurrentTime();
        bean.setCreateTime(date);
        bean.setUpdateTime(date);
        bean.setCreator(Config.Creator_Admin);
        return super.doInsertByBean(bean);
    }
}
