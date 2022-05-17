package com.iuiga.web.controller.qa;

import com.iuiga.common.core.controller.BaseController;
import com.iuiga.common.core.domain.AjaxResult;
import com.iuiga.common.core.page.TableDataInfo;
import com.iuiga.qa.domain.Subject;
import com.iuiga.qa.domain.SubjectInfo;
import com.iuiga.qa.service.SubjectInfoService;
import com.iuiga.qa.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qa/subject")
@Api(tags = {"问答管理"})
public class SubjectController extends BaseController {
    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubjectInfoService subjectInfoService;

    @ApiOperation("查询问答列表")
    @PreAuthorize("@ss.hasPermi('qa:subject:list')")
    @GetMapping("/list")
    public TableDataInfo list(Subject subject) {
        startPage();
        return getDataTable(subjectService.listSubject(subject));
    }

    @ApiOperation("查询问答详情")
    @PreAuthorize("@ss.hasPermi('qa:subject:query')")
    @GetMapping("/{subjectId}")
    public AjaxResult getInfo(@PathVariable Long subjectId) {
        return AjaxResult.success(subjectService.getSubjectById(subjectId));
    }

    @ApiOperation("新增问答")
    @PreAuthorize("@ss.hasPermi('qa:subject:add')")
    @PostMapping
    public AjaxResult add(@RequestBody Subject subject) {
        return toAjax(subjectService.insertSubject(subject));
    }

    @ApiOperation("编辑问答")
    @PreAuthorize("@ss.hasPermi('qa:subject:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody Subject subject) {
        return toAjax(subjectService.updateSubject(subject));
    }

    @ApiOperation("删除问答")
    @PreAuthorize("@ss.hasPermi('qa:subject:remove')")
    @DeleteMapping("/{subjectIds}")
    public AjaxResult remove(@PathVariable Long[] subjectIds) {
        return toAjax(subjectService.deleteSubjectByIds(subjectIds));
    }

    @ApiOperation("新增问答数据")
    @PreAuthorize("@ss.hasPermi('qa:subject:edit')")
    @PostMapping("/data")
    public AjaxResult addData(@RequestBody SubjectInfo subjectInfo) {
        return toAjax(subjectInfoService.insertSubjectInfo(subjectInfo));
    }

    @ApiOperation("编辑问答数据")
    @PreAuthorize("@ss.hasPermi('qa:subject:edit')")
    @PutMapping("/data")
    public AjaxResult editData(@RequestBody SubjectInfo subjectInfo) {
        return toAjax(subjectInfoService.updateSubjectInfo(subjectInfo));
    }

    @ApiOperation("删除问答数据")
    @PreAuthorize("@ss.hasPermi('qa:subject:edit')")
    @DeleteMapping("/data/{subjectInfoId}")
    public AjaxResult removeData(@PathVariable Long subjectInfoId){
        return toAjax(subjectInfoService.deleteSubjectInfoById(subjectInfoId));
    }
}
