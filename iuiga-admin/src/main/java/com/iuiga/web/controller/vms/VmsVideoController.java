package com.iuiga.web.controller.vms;

import com.iuiga.common.config.ProjectConfig;
import com.iuiga.common.constant.Constants;
import com.iuiga.common.core.controller.BaseController;
import com.iuiga.common.core.domain.AjaxResult;
import com.iuiga.common.core.page.TableDataInfo;
import com.iuiga.common.utils.file.FileUploadUtils;
import com.iuiga.common.utils.file.MimeTypeUtils;
import com.iuiga.framework.config.ServerConfig;
import com.iuiga.vms.domain.VmsVideo;
import com.iuiga.vms.service.VmsVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/vms/video")
@Api(tags = {"视频管理"})
public class VmsVideoController extends BaseController {

    @Autowired
    private VmsVideoService videoService;

    @Autowired
    private ServerConfig serverConfig;

    @ApiOperation("查询视频列表")
    @PreAuthorize("@ss.hasPermi('vms:video:list')")
    @GetMapping("/list")
    public TableDataInfo list(VmsVideo video) {
        startPage();
        return getDataTable(videoService.listVideo(video));
    }

    @ApiOperation("查询视频详情")
    @PreAuthorize("@ss.hasPermi('vms:video:query')")
    @GetMapping("/{videoId}")
    public AjaxResult getVideoInfo(@PathVariable Long videoId) {
        return AjaxResult.success(videoService.getVideoInfo(videoId));
    }

    @ApiOperation("新增视频")
    @PreAuthorize("@ss.hasPermi('vms:video:add')")
    @PostMapping
    public AjaxResult add(@RequestBody VmsVideo video) {
        return toAjax(videoService.insertVideo(video));
    }

    @ApiOperation("编辑视频")
    @PreAuthorize("@ss.hasPermi('vms:video:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody VmsVideo video) {
        return toAjax(videoService.updateVideo(video));
    }

    @ApiOperation("删除视频")
    @PreAuthorize("@ss.hasPermi('vms:video:remove')")
    @DeleteMapping("/{videoIds}")
    public AjaxResult remove(@PathVariable Long[] videoIds) {
        return toAjax(videoService.deleteVideo(videoIds));
    }

    @ApiOperation("上传视频")
    @PreAuthorize("@ss.hasPermi('vms:video:upload')")
    @PostMapping("/videoUpload")
    public AjaxResult videoUpload(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = ProjectConfig.getVmsVideoUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file, MimeTypeUtils.VIDEO_EXTENSION);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("path", filePath + fileName.replace(Constants.RESOURCE_PREFIX + "/", ""));
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("上传封面")
    @PreAuthorize("@ss.hasPermi('vms:video:upload')")
    @PostMapping("/coverUpload")
    public AjaxResult coverUpload(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = ProjectConfig.getVmsCoverUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file, MimeTypeUtils.IMAGE_EXTENSION);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("path", filePath + fileName.replace(Constants.RESOURCE_PREFIX + "/", ""));
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

}
