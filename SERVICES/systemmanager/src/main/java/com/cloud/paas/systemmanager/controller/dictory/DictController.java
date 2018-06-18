package com.cloud.paas.systemmanager.controller.dictory;


import com.cloud.paas.systemmanager.model.DictItem;
import com.cloud.paas.systemmanager.model.DictType;
import com.cloud.paas.systemmanager.qo.dictionary.DictCondition;
import com.cloud.paas.systemmanager.service.dictory.DictItemService;
import com.cloud.paas.systemmanager.service.dictory.DictTypeService;
import com.cloud.paas.systemmanager.vo.dictory.ItemSort;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.result.Result;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/dict")
public class DictController {
    private static final Logger logger = LoggerFactory.getLogger(DictController.class);
    @Autowired
    private DictTypeService dictTypeService;
    @Autowired
    private DictItemService dictItemService;

    /**
     *查询全部字典类型
     */
    @ApiOperation(value = "查询全部字典类型", notes = "查询全部字典详情")
    @GetMapping(value = "/{userid}/dicttype/all")
    public Result getDictTypeList() throws BusinessException {
        return dictTypeService.listDictType();
    }

    /**
     *创建字典类型
     */
    @ApiOperation(value = "创建字典类型", notes = "创建字典类型")
    @PostMapping(value = "/{userid}/dictType")
    public Result insertDictType(@RequestBody DictType dictType) throws BusinessException {
        return dictTypeService.insertDict(dictType);
    }
    /**
     *修改字典类型
     */
    @ApiOperation(value = "编辑字典类型", notes = "编辑字典类型")
    @PutMapping(value = "/{userid}/dictType")
    public Result editDictType(@RequestBody DictType dictType) throws BusinessException {
        return dictTypeService.updateDict (dictType);
    }

    /**
     *禁用字典类型
     */
    @ApiOperation(value = "禁用字典类型", notes = "禁用字典类型")
    @PostMapping(value = "/{userid}/dicttype/disable/{dictTypeId}")
    public int disableDictType(@PathVariable("dictTypeId") int dictTypeId) throws BusinessException {
        return dictTypeService.disableDictType(dictTypeId);
    }

    /**
     *启用字典类型
     */
    @ApiOperation(value = "启用字典类型", notes = "启用字典类型")
    @PostMapping(value = "/{userid}/dicttype/enable/{dictTypeId}")
    public int enableDictType(@PathVariable("dictTypeId") int dictTypeId) throws BusinessException {
        return dictTypeService.enableDictType(dictTypeId);
    }

    /**
     *删除字典类型
     */
    @ApiOperation(value = "根据字典类型编号删除字典类型", notes = "根据字典类型编号删除字典类型")
    @DeleteMapping(value = "/{userid}/dictType/{dictTypeId}")
    public Result delDictType(@PathVariable("dictTypeId") int dictTypeId) throws BusinessException {
        return dictTypeService.delDictType(dictTypeId);
    }

    /**
     *恢复字典类型
     */
    @ApiOperation(value = "恢复字典类型", notes = "恢复字典类型")
    @PostMapping(value = "/{userid}/dicttype/recover/{dictTypeId}")
    public int recoverDictType(@PathVariable("dictTypeId") int dictTypeId) throws BusinessException {
        return dictTypeService.recoverDictType(dictTypeId);
    }

    /**
     *根据名字查找字典类型
     */
    @ApiOperation(value = "根据名字查找字典类型", notes = "根据名字查找字典类型")
    @PostMapping(value = "/{userid}/dicttype/find/{dictTypeName}")
    public DictType findDictTypeByName(@PathVariable("dictTypeName") String dictTypeName) throws BusinessException {
        return dictTypeService.doFindByName(dictTypeName);
    }

    /**
     *根据名字列出字典类型及内容
     */
    @ApiOperation(value = "根据名字列出字典类型及内容", notes = "根据名字列出字典类型及内容")
    @PostMapping(value = "/{userid}/detail")
    public Result getItemByTypeName(@RequestBody List<String> dictTypeNames) {
        return dictTypeService.getItemByTypeName(dictTypeNames);
    }

    /**
     *创建字典内容
     */
    @ApiOperation(value = "创建字典内容", notes = "创建字典内容")
    @PostMapping("/{userid}/dictItem")
    public Result insertDictItem(@RequestBody DictItem dictItem) {
        return dictItemService.insertDictItem (dictItem);
    }

    /**
     *修改字典内容
     */
    @ApiOperation(value = "编辑字典内容", notes = "编辑字典内容")
    @PutMapping("/{userid}/dictItem")
    public Result editDictItem(@RequestBody DictItem dictItem) {
        return  dictItemService.updateDictItem (dictItem);
    }

    /**
     *根据字典内容编号删除字典内容
     */
    @ApiOperation(value = "根据字典内容编号删除字典内容", notes = "根据字典内容编号删除字典内容")
    @DeleteMapping(value = "/{userid}/dictItem/{dictItemId}")
    public Result delDictItem(@PathVariable("dictItemId") int dictItemId) throws BusinessException {
        return dictItemService.delDictItem(dictItemId);
    }

    /**
     *条件查询字典列表
     */
    @ApiOperation(value = "分页 条件（类型名/类型标题/字典名称）查询字典类型及内容", notes = "条件查询字典类型及内容")
    @PostMapping("/{userid}/list")
    public Result searchDict(@RequestBody DictCondition condition) throws BusinessException {
        return dictTypeService.findDictIdListByConditionResult(condition);
    }

    /**
     *字典内容排序
     */
    @ApiOperation(value = "编辑字典内容排列顺序", notes = "编辑字典内容排列顺序")
    @PutMapping(value = "/{userid}/dictItem/sort")
    public Result sortDictItem(@RequestBody List<ItemSort> itemSorts) throws BusinessException {
        return dictItemService.sortDictItem(itemSorts);
    }

    /**
     *获取所有字典
     */
    @ApiOperation(value = "获取所有字典", notes = "获取所有字典")
    @GetMapping("/all")
    public Result getAllDict() throws BusinessException {
        return dictTypeService.getAllDict();
    }

    /**
     *根据类型编号获取字典内容
     */
    @ApiOperation(value = "根据类型编号获取字典内容",notes = "根据类型编号获取字典内容")
    @GetMapping("/{userid}/dictItem/list/{dictTypeId}")
    public Result getItemsByTypeId(@PathVariable("dictTypeId")int dictTypeId){
        return dictItemService.doSearchByTypeId(dictTypeId);
    }
}
