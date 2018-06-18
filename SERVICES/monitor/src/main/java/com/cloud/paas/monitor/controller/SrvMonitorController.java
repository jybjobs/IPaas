package com.cloud.paas.monitor.controller;

import com.cloud.paas.monitor.qo.HistoryCondition;
import com.cloud.paas.monitor.service.srvmonitor.SrvMonitorService;
import com.cloud.paas.util.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author: zcy
 * @desc: 服务监控Controller
 * @Date: Created in 2018-01-12 14:16
 * @modified By:
 **/
@RestController
@RequestMapping(value = "/srvmonitor")
@CrossOrigin
public class SrvMonitorController {
    @Autowired
    private SrvMonitorService srvMonitorService;

    @ApiOperation(value = "获取服务状态信息", notes = "获取服务状态信息")
    @GetMapping(value = "/{userid}/srvState/{srvid}")
    public Result getSrvState(@PathVariable int userid, @PathVariable int srvid) {
        return srvMonitorService.getSrvState(userid, srvid);
    }

    @ApiOperation(value = "获取Deploy状态信息", notes = "获取Deploy状态信息")
    @GetMapping(value = "/deployState/now/{deployname}")
    public Result getDeployState(@PathVariable String deployname) {
        return srvMonitorService.getDeployState(deployname);
    }

    @ApiOperation(value = "获取Deploy一段时间的状态信息", notes = "获取Deploy一段时间的状态信息")
    @GetMapping(value = "/deployState/period/{deployname}/{starttime}/{endtime}/{interval}/{unit}")
    public Result getDeployStateByPeriod(@PathVariable String deployname,
                                         @PathVariable String starttime,
                                         @PathVariable String endtime,
                                         @PathVariable int interval,
                                         @PathVariable String unit) {
        return srvMonitorService.getDeployStateByPeriod(deployname, starttime, endtime, interval, unit);
    }

    @ApiOperation(value = "根据容器名称获取容器当前的状态信息", notes = "获取一个容器的信息")
    @GetMapping(value = "/ctnState/{podName}")
    public Result getCtnStateWithCtnName(@PathVariable String podName){
        return srvMonitorService.getCtnStateWithCtnName(podName);
    }

    @ApiOperation(value = "获取服务历史记录", notes = "获取服务历史记录")
    @PostMapping(value = "{userid}/history/{srvid}")
    public Result getHistory(@PathVariable int userid, @PathVariable int srvid, @RequestBody HistoryCondition historyCondition) {
        return srvMonitorService.getHistroy(userid, srvid, historyCondition);
    }
}
