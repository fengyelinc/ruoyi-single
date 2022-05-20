package com.iuiga.web.controller.vms;

import com.iuiga.common.core.controller.BaseController;
import com.iuiga.common.core.domain.AjaxResult;
import com.iuiga.common.core.page.TableDataInfo;
import com.iuiga.vms.domain.VmsVideoLog;
import com.iuiga.vms.service.VmsVideoLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vms/videoLog")
@Api(tags = {"视频日志"})
public class VmsVideoLogController extends BaseController {

    @Autowired
    private VmsVideoLogService videoLogService;

    @ApiOperation("查询视频日志列表")
    @PreAuthorize("@ss.hasPermi('vms:videoLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(VmsVideoLog log) {
        startPage();
        return getDataTable(videoLogService.listVideoLog(log));
    }

    @ApiOperation("查询视频日志详情")
    @PreAuthorize("@ss.hasPermi('vms:videoLog:query')")
    @GetMapping("/{videoLogId}")
    public AjaxResult getInfo(@PathVariable Long videoLogId) {
        return AjaxResult.success(videoLogService.getVideoLogInfo(videoLogId));
    }
}
