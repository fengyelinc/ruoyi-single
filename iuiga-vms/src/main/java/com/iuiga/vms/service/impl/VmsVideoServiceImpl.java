package com.iuiga.vms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iuiga.common.utils.SecurityUtils;
import com.iuiga.common.utils.StringUtils;
import com.iuiga.vms.domain.VmsVideo;
import com.iuiga.vms.mapper.VmsVideoMapper;
import com.iuiga.vms.service.VmsVideoHisService;
import com.iuiga.vms.service.VmsVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

/**
 * 视频Service
 */
@Service
public class VmsVideoServiceImpl extends ServiceImpl<VmsVideoMapper, VmsVideo> implements VmsVideoService {

    @Autowired
    private VmsVideoHisService vmsVideoHisService;

    /**
     * 查询视频列表
     *
     * @param vmsVideo
     * @return
     */
    @Override
    public List<VmsVideo> listVideo(VmsVideo vmsVideo) {
        return list(new LambdaQueryWrapper<VmsVideo>()
                .like(StringUtils.isNotBlank(vmsVideo.getTitle()), VmsVideo::getTitle, vmsVideo.getTitle())
                .like(StringUtils.isNotBlank(vmsVideo.getDescription()), VmsVideo::getDescription, vmsVideo.getDescription())
                .like(StringUtils.isNotBlank(vmsVideo.getStatus()), VmsVideo::getStatus, vmsVideo.getStatus())
                .orderByDesc(VmsVideo::getVideoId));
    }

    /**
     * 获取视频信息
     *
     * @param videoId
     * @return
     */
    @Override
    public VmsVideo getVideoInfo(Long videoId) {
        return getById(videoId);
    }

    /**
     * 创建视频
     *
     * @param vmsVideo
     * @return
     */
    @Override
    public boolean insertVideo(VmsVideo vmsVideo) {
        vmsVideo.setVideoUuid(UUID.randomUUID().toString());
        vmsVideo.setCreateBy(SecurityUtils.getUsername());
        vmsVideo.setCreateTime(new Date());
        try {
            return save(vmsVideo);
        } finally {
            vmsVideoHisService.generateVideoHistory(Collections.singletonList(vmsVideo));
        }
    }

    /**
     * 更新视频
     *
     * @param vmsVideo
     * @return
     */
    @Override
    public boolean updateVideo(VmsVideo vmsVideo) {
        vmsVideo.setUpdateBy(SecurityUtils.getUsername());
        vmsVideo.setUpdateTime(new Date());
        try {
            return updateById(vmsVideo);
        } finally {
            vmsVideoHisService.generateVideoHistory(Collections.singletonList(vmsVideo));
        }
    }

    /**
     * 删除视频
     *
     * @param videoIds
     * @return
     */
    @Override
    public boolean deleteVideo(Long[] videoIds) {
        List<VmsVideo> vmsVideoList = listByIds(Arrays.asList(videoIds));
        vmsVideoHisService.generateVideoHistory(vmsVideoList);
        return removeByIds(Arrays.asList(videoIds));
    }

    /**
     * 上传视频
     *
     * @param file
     */
    @Override
    public void uploadVideo(File file) {

    }
}
