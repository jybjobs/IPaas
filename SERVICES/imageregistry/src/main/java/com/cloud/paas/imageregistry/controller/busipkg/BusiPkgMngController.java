package com.cloud.paas.imageregistry.controller.busipkg;

import com.alibaba.fastjson.JSONArray;
import com.cloud.paas.imageregistry.model.BusiPkgDetail;
import com.cloud.paas.imageregistry.model.BusiPkgVersionDetail;
import com.cloud.paas.imageregistry.qo.BusiPkgExample;
import com.cloud.paas.imageregistry.service.busipkg.BusiPkgService;
import com.cloud.paas.imageregistry.service.busipkg.BusiPkgVersionService;
import com.cloud.paas.imageregistry.vo.busipkg.BusiPkgListVO;
import com.cloud.paas.imageregistry.vo.busipkg.BusiPkgVersionDetailVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.remoteinfo.RemoteServerInfo;
import com.cloud.paas.util.result.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping(value = "/busipkg")
public class BusiPkgMngController {
    private static final Logger logger = LoggerFactory.getLogger(BusiPkgMngController.class);
    @Autowired
    private BusiPkgService busiPkgService;
    @Autowired
    private BusiPkgVersionService busiPkgVersionService;

    @ApiOperation(value = "分页 业务包条件信息查询", notes = "分页 业务包条件信息查询")
    @PostMapping(value = "/{userid}/querylistcondition/limitpage")
    public Result selectLimitVersionVOListByExample(@PathVariable("userid") String userid,@RequestBody BusiPkgExample busiPkgExample)throws BusinessException{
        return busiPkgService.findBusiPkgIdListByExampleResult(busiPkgExample);
    }

    @ApiOperation(value = "直接新建业务包", notes = "直接新建业务包")
    @PostMapping(value = "/{userid}/insertPkg")
    public Result insertPkgAndVersion(@RequestBody @Validated({BusiPkgDetail.BusiPkgAddValidate.class})BusiPkgVersionDetailVO busiPkgVersionDetailVO, @PathVariable("userid") String userid) throws BusinessException{
        return busiPkgVersionService.insertPkgAndVersion(userid,busiPkgVersionDetailVO);
    }


    //TODO 待定（不知道哪里使用了）
    @ApiOperation(value = "业务包条件信息查询", notes = "业务包条件信息查询")
    @PostMapping(value = "/{userid}/querylistcondition")
    public List<BusiPkgListVO> selectVersionVOListByExample(@PathVariable("userid") String userid, @RequestBody BusiPkgExample busiPkgExample){
        List list = busiPkgService.selectVersionVOListByExample(busiPkgExample);
        logger.debug(list.toString());
        return list;
    }

    //TODO 待定（不知道哪里使用了）
    @ApiOperation(value = "id业务包拼接信息查询", notes = "id业务包拼接信息查询")
    @GetMapping(value = "/queryList/{busiPkgId}")
    public Result selectVersionVOList(@PathVariable("busiPkgId") int busiPkgId) throws Exception{
        return busiPkgService.selectVersionVOList(busiPkgId);
    }


    //TODO 待定（不知道哪里使用了）
    @ApiOperation(value = "新建业务包", notes = "新建业务包")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/add")
    public Result createBusiPkg(@PathVariable("userid") String userid, @RequestBody BusiPkgDetail busiPkgDetail) throws Exception{
        return busiPkgService. doInsertByBusiPkgDetailBean(busiPkgDetail);
    }

    //TODO 待定（不知道哪里使用了）
    @ApiOperation(value = "获取全量业务包列表", notes = "获取全量业务包列表")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @GetMapping(value = "/all/{userid}")
    public Result getBusiPkgList(@PathVariable("userid") String userid)  throws Exception{
        return busiPkgService.listFindByPkgId();
    }

    //TODO 待定（不知道哪里使用了）
    @ApiOperation(value = "获取指定用户下的业务包列表", notes = "获取指定用户下的业务包列表")
    @ApiImplicitParam(name = "busipkgid", value = "业务包id", required = true, dataType = "String")
    @GetMapping(value = "/user/{userid}")
    public BusiPkgDetail getBusiPkgByUserId(@PathVariable("userid") String userid) throws Exception {
        //return busiPkgService.doFindById(busipkgid);
        return null;
    }

    @ApiOperation(value = "获取指定用户下的业务包列表", notes = "获取指定用户下的业务包列表")
    @ApiImplicitParam(name = "busipkgid", value = "业务包id", required = true, dataType = "Integer")
    @GetMapping(value = "/busipkgid/{busipkgid}")
    public BusiPkgDetail getBusiPkgByBusiPkgId(@PathVariable("busipkgid") int busipkgid) throws BusinessException {
        return busiPkgService.doFindById(busipkgid);
    }

    @ApiOperation(value = "获取指定业务包详细信息", notes = "获取指定业务包详细信息")
    @ApiImplicitParam(name = "busipkgid", value = "业务包id", required = true, dataType = "String")
    @GetMapping(value = "/{userid}/{busipkgid}/detail")
    public Result getBusiPkgById(@PathVariable("userid") String userid, @PathVariable("busipkgid") int busipkgid) throws BusinessException {
        return busiPkgService.doFindByBusiPkgId(busipkgid);
    }


    //TODO 待定（不知道哪里使用了）
    @ApiOperation(value = "查询业务包", notes = "根据业务包名称查询业务包的详细信息")
    @ApiImplicitParam(name = "busiPkgName", value = "业务包名称", required = true, dataType = "String")
    @GetMapping(value = "/query/{busiPkgName}")
    public String getBusiPkgByName(@PathVariable("busiPkgName") String busiPkgName) throws Exception {
        return busiPkgName.toString();
    }
    @ApiOperation(value = "scp下载", notes = "其余服务器下载到项目运行的服务器")
    @PostMapping(value = "/{userid}/scpdownlownfile/{flag}")
    public Result scpDownLoadBusiPkgFile(@PathVariable("userid") String userid, @RequestBody RemoteServerInfo remoteServerInfo, @PathVariable("flag") int flag) throws Exception {
        return busiPkgService.scpDownLoadBusiPkgFile(userid,remoteServerInfo,flag);
    }
    @ApiOperation(value = "ftp下载", notes = "其余服务器下载到项目运行的服务器")
    @PostMapping(value = "/{userid}/ftpdownlownfile/{flag}")
    public Result ftpDownLoadBusiPkgFile(@PathVariable("userid") String userid, @RequestBody RemoteServerInfo remoteServerInfo, @PathVariable("flag") int flag) throws Exception {
        return busiPkgService.ftpDownLoadBusiPkgFile(userid,remoteServerInfo,flag);
    }


    @ApiOperation(value = "上传业务包", notes = "将离线业务包上传到平台")
    @ApiImplicitParam(name = "file", value = "本地业务包文件", required = true, dataType = "MultipartFile")
    @PostMapping(value = "/{userid}/uploadfile")
    public Result uploadBusiPkgFile(@PathVariable("userid") String userid, @RequestParam MultipartFile file) throws BusinessException{
        //上传文件并返回文件路径
        return busiPkgVersionService.uploadFile(file, userid);
    }

    @ApiOperation(value = "上传图片", notes = "上传本地图片到服务器")
    @ApiImplicitParam(name = "img", value = "上传的图片", required = true, dataType = "MultipartFile")
    @PostMapping(value = "/{userid}/uploadimg")
    public Result uploadBusiPkgImg(@PathVariable("userid") String userid, @RequestParam MultipartFile img) throws BusinessException{
        //上传文件并返回文件路径
        return busiPkgVersionService.uploadImg(img, userid);
    }

    @ApiOperation(value = "修改业务包", notes = "修改业务包的属性信息")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @PutMapping(value = "/{userid}/update")
    public Result editBusiPkg(@PathVariable("userid") String userid, @RequestBody BusiPkgDetail busiPkgDetail) throws BusinessException{
        return busiPkgService.doUpdateByBusiPkgBean(busiPkgDetail);
    }


    @ApiOperation(value = "删除业务包", notes = "删除平台上已经存在的业务包")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @DeleteMapping(value = "/{userid}/{busipkgid}")
    public Result deleteBusiPkgById(@PathVariable("userid") String userid, @PathVariable("busipkgid") int busipkgid) throws BusinessException {
        return busiPkgService.deleteBusiPkgById(busipkgid);
    }

    @ApiOperation(value = "获取指定业务包下务的版本详细信息列表", notes = "获取指定业务包下务的版本详细信息列表")
    @ApiImplicitParam(name = "busipkgid", value = "业务包id", required = true, dataType = "String")
    @GetMapping(value="/version/{userid}/{busipkgid}")
    public Result  listFindByPkgId(@PathVariable("userid") String userid, @PathVariable("busipkgid") String busipkgid)throws BusinessException{
        return busiPkgVersionService.listFindByPkgId(Integer.parseInt(busipkgid));
    }

    @ApiOperation(value = "业务包查询条件搜索信息", notes = "业务包查询条件搜索信息")
    @PostMapping(value="/{userid}/version/querycondition")
    public Result listFindByExample(@PathVariable("userid") String userid,@RequestBody BusiPkgExample busiPkgExample) throws BusinessException {
        return busiPkgVersionService.listFindByExample(busiPkgExample);
    }

    @ApiOperation(value = "获取单表业务版本信息", notes = "获取单表业务版本信息")
    @ApiImplicitParam(name = "busipkgversionid", value = "业务版本id", required = true, dataType = "String")
    @GetMapping(value = "/version/{userid}/{busipkgversionid}/detail")
    public Result findVersionById(@PathVariable("userid") String userid, @PathVariable("busipkgversionid") String busipkgversionid) throws BusinessException {
        return busiPkgVersionService.doFindByBusiPkgVersionId(Integer.parseInt(busipkgversionid));
    }

    @ApiOperation(value = "根据业务包编号集合查询业务包当前状态，并返回已上传的服务版本编号集合", notes = "根据业务包编号集合查询业务包当前状态，并返回已上传的服务版本编号集合")
    @PostMapping(value = "/version/findStatusByIdsReturnSrvVersionIds")
    public Result findStatusByIdsReturnSrvVersionIds(@RequestBody JSONArray srvVersionInfos) throws BusinessException {
        return busiPkgVersionService.findStatusByIdsReturnSrvVersionIds(srvVersionInfos);
    }

    @ApiOperation(value = "新建业务版本", notes = "新建业务版本返回下一条的id")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @PostMapping(value="/{userid}/version/add")
    public Result insertVersion(@PathVariable("userid") String userid,@RequestBody @Validated BusiPkgVersionDetail busiPkgVersionDetail)throws BusinessException{
        return  busiPkgVersionService.doInsertByBean(busiPkgVersionDetail,userid);
    }

    @ApiOperation(value = "根据业务包ID新增版本", notes = "根据业务包ID新增版本返回下一条的id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busiPkgId", value = "业务包ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    })
    @GetMapping(value="/{userid}/{busiPkgId}/version/addbybusipkgid")
    public Result addVersionByBusiPkgId(@PathVariable("userid") String userid,@PathVariable("busiPkgId") Integer busiPkgId)throws BusinessException{
        return  busiPkgVersionService.insertVersionByBusiPkgId(userid,busiPkgId);
    }

    @ApiOperation(value = "更新业务版本", notes = "更新业务版本")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @PutMapping(value = "/version/{userid}/update")
    public Result updateVersion(@PathVariable("userid") String userid,@RequestBody BusiPkgVersionDetail record) throws BusinessException{
        return busiPkgVersionService.doUpdateByBusiPkgVersionBean(userid,record);
    }

    @ApiOperation(value = "删除业务版本", notes = "删除指定业务版本")
    @ApiImplicitParam(name = "busipkgversionid", value = "业务版本id", required = true, dataType = "String")
    @DeleteMapping(value = "/version/{userid}/{busipkgversionid}")
    public Result deleteVersion(@PathVariable("userid") String userid,@PathVariable("busipkgversionid") String busipkgversionid)throws BusinessException{
        return busiPkgVersionService.doDeleteByBusiPkgVersionId(Integer.parseInt(busipkgversionid));
    }

    @GetMapping(value = "/version/{userid}/{busipkgid}/count")
    public Result countVersion(@PathVariable("userid") String userid, @PathVariable("busipkgid") int busiPkgId){
        return busiPkgVersionService.countVersion(busiPkgId);
    }

    @ApiOperation(value = "检查中文名是否重复", notes = "检查业务包中文名是否重复")
    @ApiImplicitParam(name = "nameZh", value = "业务包中文名", required = true, dataType = "String")
    @GetMapping(value = "/{userid}/busipkgnamezh/{busipkgnamezh}")
    public Result checkNameZh(@PathVariable("userid") String userId,@PathVariable("busipkgnamezh") String busiPkgNameZh){
        return busiPkgService.checkNameZh(busiPkgNameZh);
    }

    @ApiOperation(value = "检查英文名是否重复", notes = "检查业务包英文名是否重复")
    @ApiImplicitParam(name = "nameZh", value = "业务包中文名", required = true, dataType = "String")
    @GetMapping(value = "/{userid}/busipkgnameen/{busipkgnameen}")
    public Result checkNameEn(@PathVariable("userid") String userId,@PathVariable("busipkgnameen") String busiPkgNameEn){
        return busiPkgService.checkNameEn(busiPkgNameEn);
    }

    @ApiOperation(value = "检查版本是否重复", notes = "检查业务包版本是否重复")
    @ApiImplicitParam(name = "version", value = "业务包版本", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/busipkgversion")
    public Result checkVersion(@RequestBody BusiPkgExample busiPkgExample) {
        return busiPkgVersionService.checkVersion(busiPkgExample);
    }
}
