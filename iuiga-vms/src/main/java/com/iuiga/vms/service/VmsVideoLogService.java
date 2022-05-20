package com.iuiga.vms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iuiga.vms.domain.VmsVideoLog;

import java.util.List;

/**
 * 视频日志Service
 */
public interface VmsVideoLogService extends IService<VmsVideoLog> {
    /**
     * 查询视频日志
     * @param log
     * @return
     */
    List<VmsVideoLog> listVideoLog(VmsVideoLog log);

    /**
     * 查询视频日志ID
     * @param videoLogId
     * @return
     */
    VmsVideoLog getVideoLogInfo(Long videoLogId);
}
