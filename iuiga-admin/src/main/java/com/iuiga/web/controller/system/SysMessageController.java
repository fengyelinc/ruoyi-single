package com.iuiga.web.controller.system;

import com.iuiga.common.constant.UserConstants;
import com.iuiga.common.core.controller.BaseController;
import com.iuiga.common.core.domain.AjaxResult;
import com.iuiga.common.core.page.TableDataInfo;
import com.iuiga.common.exception.CustomException;
import com.iuiga.common.utils.poi.ExcelUtil;
import com.iuiga.system.domain.SysMessage;
import com.iuiga.system.service.ISysMessageService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(tags = "消息模板管理")
@RestController
@RequestMapping("/system/message")
public class SysMessageController extends BaseController {

    @Autowired
    private ISysMessageService messageService;

    /**
     * 查看列表
     * @param message
     * @return
     */
    @ApiOperation("查询消息模板列表")
    @PreAuthorize("@ss.hasPermi('system:message:list')")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name="messageCode",value="消息编号",dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name="messageGroup",value="消息分组",dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name="messageContent",value="消息模板",dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name="pageSize",value="分页尺寸",dataTypeClass = Integer.class, paramType = "query"),
            @ApiImplicitParam(name="pageNum",value="分页页码",dataTypeClass = Integer.class, paramType = "query")
    })
    public TableDataInfo list(SysMessage message) {
        startPage();
        return getDataTable(messageService.listMessage(message));
    }

    /**
     * 查看详情
     * @param messageId
     * @return
     */
    @ApiOperation("查询消息模板详情")
    @PreAuthorize("@ss.hasPermi('system:message:query')")
    @GetMapping("/info/{messageId}")
    public AjaxResult info(@ApiParam(name = "消息ID") @PathVariable Long messageId){
        return AjaxResult.success(messageService.getInfo(messageId));
    }

    /**
     * 新增
     * @param message
     * @return
     */
    @ApiOperation("新增消息模板")
    @PreAuthorize("@ss.hasPermi('system:message:add')")
    @ApiImplicitParams({
            @ApiImplicitParam(name="messageCode",value="消息编号",dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name="messageGroup",value="消息分组",dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name="messageContent",value="消息模板",dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name="remark",value="备注",dataTypeClass = String.class, paramType = "query")
    })
    @PostMapping
    public AjaxResult add(@RequestBody SysMessage message){
        if (UserConstants.NOT_UNIQUE.equals(messageService.checkMessageKeyUnique(message)))
        {
            throw new CustomException(100001, message.getMessageCode());
        }
        return toAjax(messageService.addMessage(message));
    }

    /**
     * 编辑
     * @param message
     * @return
     */
    @ApiOperation("编辑消息模板")
    @PreAuthorize("@ss.hasPermi('system:message:edit')")
    @ApiImplicitParams({
            @ApiImplicitParam(name="messageId",value="消息ID",dataTypeClass = Long.class, paramType = "query"),
            @ApiImplicitParam(name="messageCode",value="消息编号",dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name="messageGroup",value="消息分组",dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name="messageContent",value="消息模板",dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name="remark",value="备注",dataTypeClass = String.class, paramType = "query")
    })
    @PutMapping
    public AjaxResult edit(@RequestBody SysMessage message){
        if (UserConstants.NOT_UNIQUE.equals(messageService.checkMessageKeyUnique(message)))
        {
            throw new CustomException(100001, message.getMessageCode());
        }
        return toAjax(messageService.editMessage(message));
    }

    /**
     * 删除
     * @param messageId
     * @return
     */
    @ApiOperation("删除消息模板")
    @PreAuthorize("@ss.hasPermi('system:message:remove')")
    @DeleteMapping("/{messageId}")
    public AjaxResult remove(@ApiParam(name = "消息ID") @PathVariable String messageId){
        return toAjax(messageService.deleteMessage(messageId));
    }

    /**
     * 导出消息模板
     * @param response
     * @param message
     */
    @ApiOperation("导出消息模板")
    @PreAuthorize("@ss.hasPermi('system:message:export')")
    @PostMapping("/export")
    @ApiImplicitParams({
            @ApiImplicitParam(name="messageCode",value="消息编号",dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name="messageGroup",value="消息分组",dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name="messageContent",value="消息模板",dataTypeClass = String.class, paramType = "query")
    })
    public void export(HttpServletResponse response, SysMessage message) {
        List<SysMessage> list = message.getExportIdList()!=null&&message.getExportIdList().size()>0?messageService.listByIds(message.getExportIdList()):messageService.listMessage(message);
        ExcelUtil<SysMessage> util = new ExcelUtil<>(SysMessage.class);
        util.exportExcel(response, list, "消息模板");
    }

    /**
     * 刷新参数缓存
     */
    @ApiOperation("刷新参数缓存")
    @PreAuthorize("@ss.hasPermi('system:message:refresh')")
    @PostMapping("/refreshCache")
    public AjaxResult refreshCache()
    {
        messageService.resetMessageCache();
        return AjaxResult.success();
    }
}
