package com.iuiga.web.controller.qa;

import com.iuiga.common.core.controller.BaseController;
import com.iuiga.common.core.domain.AjaxResult;
import com.iuiga.common.core.page.TableDataInfo;
import com.iuiga.qa.domain.QaSubjectLog;
import com.iuiga.qa.service.QaSubjectLogService;
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
@Api(tags = {"问答日志"})
public class QaSubjectLogController extends BaseController {

    @Autowired
    private QaSubjectLogService qaSubjectLogService;

    @ApiOperation("查询问答日志列表")
    @PreAuthorize("@ss.hasPermi('qa:subjectLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(QaSubjectLog qaSubjectLog) {
        startPage();
        return getDataTable(qaSubjectLogService.listSubjectLog(qaSubjectLog));
    }

    @ApiOperation("查询问答日志详情")
    @PreAuthorize("@ss.hasPermi('qa:subjectLog:query')")
    @GetMapping("/{subjectLogId}")
    public AjaxResult getInfo(@PathVariable Long subjectLogId) {
        return AjaxResult.success(qaSubjectLogService.getSubjectLogById(subjectLogId));
    }

}
