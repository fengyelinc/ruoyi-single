package com.iuiga.web.controller.qa;

import com.iuiga.common.core.controller.BaseController;
import com.iuiga.common.core.domain.AjaxResult;
import com.iuiga.common.core.page.TableDataInfo;
import com.iuiga.qa.domain.SubjectLog;
import com.iuiga.qa.service.SubjectLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qa/subjectLog")
@Api(tags = {"问答日志管理"})
public class SubjectLogController extends BaseController {

    @Autowired
    private SubjectLogService subjectLogService;

    @ApiOperation("查询问答日志列表")
    @PreAuthorize("@ss.hasPermi('qa:subjectLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SubjectLog subjectLog) {
        startPage();
        return getDataTable(subjectLogService.listSubjectLog(subjectLog));
    }

    @ApiOperation("查询问答日志详情")
    @PreAuthorize("@ss.hasPermi('qa:subjectLog:query')")
    @GetMapping("/{subjectLogId}")
    public AjaxResult getInfo(@PathVariable Long subjectLogId) {
        return AjaxResult.success(subjectLogService.getSubjectLogById(subjectLogId));
    }

}
