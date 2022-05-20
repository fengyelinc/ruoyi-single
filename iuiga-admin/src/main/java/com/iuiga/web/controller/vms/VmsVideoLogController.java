package com.iuiga.web.controller.vms;

import com.iuiga.common.core.controller.BaseController;
import com.iuiga.common.core.domain.AjaxResult;
import com.iuiga.common.core.page.TableDataInfo;
import com.iuiga.common.utils.DateUtils;
import com.iuiga.common.utils.ServletUtils;
import com.iuiga.common.utils.bean.BeanUtils;
import com.iuiga.common.utils.poi.ExcelUtil;
import com.iuiga.vms.domain.VmsVideoLog;
import com.iuiga.vms.domain.vo.VmsVideoLogExcelVo;
import com.iuiga.vms.service.VmsVideoLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @ApiOperation("导出视频日志")
    @PreAuthorize("@ss.hasPermi('vms:videoLog:export')")
    @PostMapping("/export")
    public void export(VmsVideoLog log) {
        List<VmsVideoLog> logList = videoLogService.listVideoLog(log);
        List<VmsVideoLogExcelVo> list = logList.stream().map(item -> {
            VmsVideoLogExcelVo vo = new VmsVideoLogExcelVo();
            BeanUtils.copyBeanProp(vo, item);
            vo.setVideoLogId(item.getVideoLogId().toString());
            vo.setDuration(item.getDuration().toString());
            vo.setCreateTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, item.getCreateTime()));
            return vo;
        }).collect(Collectors.toList());
        ExcelUtil<VmsVideoLogExcelVo> util = new ExcelUtil<>(VmsVideoLogExcelVo.class);
        util.exportExcel(ServletUtils.getResponse(), list, "视频日志");
    }
}
