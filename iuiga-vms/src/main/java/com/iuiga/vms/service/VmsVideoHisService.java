package com.iuiga.vms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iuiga.vms.domain.VmsVideo;
import com.iuiga.vms.domain.VmsVideoHis;

import java.util.List;

/**
 * 视频历史Service
 */
public interface VmsVideoHisService extends IService<VmsVideoHis> {
    /**
     * 生成视频历史
     * @param vmsVideo
     */
    void generateVideoHistory(List<VmsVideo> vmsVideo);
}
