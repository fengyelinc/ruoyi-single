package com.iuiga.vms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iuiga.common.utils.StringUtils;
import com.iuiga.vms.domain.VmsVideoLog;
import com.iuiga.vms.mapper.VmsVideoLogMapper;
import com.iuiga.vms.service.VmsVideoLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 视频日志Service
 */
@Service
public class VmsVideoLogServiceImpl extends ServiceImpl<VmsVideoLogMapper, VmsVideoLog> implements VmsVideoLogService {

    /**
     * 查询视频日志
     *
     * @param log
     * @return
     */
    @Override
    public List<VmsVideoLog> listVideoLog(VmsVideoLog log) {
        return list(new LambdaQueryWrapper<VmsVideoLog>()
                .like(StringUtils.isNotBlank(log.getTitle()), VmsVideoLog::getTitle, log.getTitle())
                .ge(log.getBeginTime()!=null, VmsVideoLog::getCreateTime, log.getBeginTime())
                .le(log.getEndTime()!=null, VmsVideoLog::getCreateTime, log.getEndTime())
                .in(log.getExportIdList()!=null && log.getExportIdList().size()>0, VmsVideoLog::getVideoLogId, log.getExportIdList())
                .orderByDesc(VmsVideoLog::getCreateTime));
    }

    /**
     * 查询视频日志ID
     *
     * @param videoLogId
     * @return
     */
    @Override
    public VmsVideoLog getVideoLogInfo(Long videoLogId) {
        return getById(videoLogId);
    }
}
