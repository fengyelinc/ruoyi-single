package com.iuiga.web.controller.qa;

import com.iuiga.common.core.controller.BaseController;
import com.iuiga.common.core.domain.AjaxResult;
import com.iuiga.common.core.page.TableDataInfo;
import com.iuiga.qa.domain.QaSubject;
import com.iuiga.qa.domain.QaSubjectInfo;
import com.iuiga.qa.service.QaSubjectInfoService;
import com.iuiga.qa.service.QaSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qa/subject")
@Api(tags = {"问答管理"})
public class QaSubjectController extends BaseController {
    @Autowired
    private QaSubjectService qaSubjectService;

    @Autowired
    private QaSubjectInfoService qaSubjectInfoService;

    @ApiOperation("查询问答列表")
    @PreAuthorize("@ss.hasPermi('qa:subject:list')")
    @GetMapping("/list")
    public TableDataInfo list(QaSubject qaSubject) {
        startPage();
        return getDataTable(qaSubjectService.listSubject(qaSubject));
    }

    @ApiOperation("查询问答详情")
    @PreAuthorize("@ss.hasPermi('qa:subject:query')")
    @GetMapping("/{subjectId}")
    public AjaxResult getInfo(@PathVariable Long subjectId) {
        return AjaxResult.success(qaSubjectService.getSubjectById(subjectId));
    }

    @ApiOperation("新增问答")
    @PreAuthorize("@ss.hasPermi('qa:subject:add')")
    @PostMapping
    public AjaxResult add(@RequestBody QaSubject qaSubject) {
        return toAjax(qaSubjectService.insertSubject(qaSubject));
    }

    @ApiOperation("编辑问答")
    @PreAuthorize("@ss.hasPermi('qa:subject:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody QaSubject qaSubject) {
        return toAjax(qaSubjectService.updateSubject(qaSubject));
    }

    @ApiOperation("删除问答")
    @PreAuthorize("@ss.hasPermi('qa:subject:remove')")
    @DeleteMapping("/{subjectIds}")
    public AjaxResult remove(@PathVariable Long[] subjectIds) {
        return toAjax(qaSubjectService.deleteSubjectByIds(subjectIds));
    }

    @ApiOperation("新增问答数据")
    @PreAuthorize("@ss.hasPermi('qa:subject:edit')")
    @PostMapping("/data")
    public AjaxResult addData(@RequestBody QaSubjectInfo qaSubjectInfo) {
        return toAjax(qaSubjectInfoService.insertSubjectInfo(qaSubjectInfo));
    }

    @ApiOperation("编辑问答数据")
    @PreAuthorize("@ss.hasPermi('qa:subject:edit')")
    @PutMapping("/data")
    public AjaxResult editData(@RequestBody QaSubjectInfo qaSubjectInfo) {
        return toAjax(qaSubjectInfoService.updateSubjectInfo(qaSubjectInfo));
    }

    @ApiOperation("删除问答数据")
    @PreAuthorize("@ss.hasPermi('qa:subject:edit')")
    @DeleteMapping("/data/{subjectInfoId}")
    public AjaxResult removeData(@PathVariable Long subjectInfoId){
        return toAjax(qaSubjectInfoService.deleteSubjectInfoById(subjectInfoId));
    }
}
