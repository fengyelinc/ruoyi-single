package com.iuiga.vms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iuiga.vms.domain.VmsVideo;

import java.io.File;
import java.util.List;

/**
 * 视频Service
 */
public interface VmsVideoService extends IService<VmsVideo> {

    /**
     * 查询视频列表
     * @param vmsVideo
     * @return
     */
    List<VmsVideo> listVideo(VmsVideo vmsVideo);

    /**
     * 获取视频信息
     * @param videoId
     * @return
     */
    VmsVideo getVideoInfo(Long videoId);

    /**
     * 创建视频
     * @param vmsVideo
     * @return
     */
    boolean insertVideo(VmsVideo vmsVideo);

    /**
     * 更新视频
     * @param vmsVideo
     * @return
     */
    boolean updateVideo(VmsVideo vmsVideo);

    /**
     * 删除视频
     * @param videoIds
     * @return
     */
    boolean deleteVideo(Long[] videoIds);

    /**
     * 上传视频
     * @param file
     */
    void uploadVideo(File file);
}
