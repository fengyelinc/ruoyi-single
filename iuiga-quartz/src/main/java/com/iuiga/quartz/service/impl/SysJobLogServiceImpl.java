package com.iuiga.quartz.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iuiga.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iuiga.quartz.domain.SysJobLog;
import com.iuiga.quartz.mapper.SysJobLogMapper;
import com.iuiga.quartz.service.ISysJobLogService;

/**
 * 定时任务调度日志信息 服务层
 * 
 * @author guiguzi
 */
@Service
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogMapper, SysJobLog> implements ISysJobLogService
{

    /**
     * 获取quartz调度器日志的计划任务
     * 
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    @Override
    public List<SysJobLog> selectJobLogList(SysJobLog jobLog)
    {
        Map<String, Object> params = jobLog.getParams();
        return list(new LambdaQueryWrapper<SysJobLog>()
                .ge(params.get("beginTime")!=null, SysJobLog::getCreateTime, params.get("beginTime"))
                .le(params.get("endTime")!=null, SysJobLog::getCreateTime, params.get("endTime"))
                .like(StringUtils.isNotBlank(jobLog.getInvokeTarget()), SysJobLog::getInvokeTarget, jobLog.getInvokeTarget())
                .eq(StringUtils.isNotBlank(jobLog.getStatus()), SysJobLog::getStatus, jobLog.getStatus())
                .eq(StringUtils.isNotBlank(jobLog.getJobGroup()), SysJobLog::getJobGroup, jobLog.getJobGroup())
                .like(StringUtils.isNotBlank(jobLog.getJobName()), SysJobLog::getJobName, jobLog.getJobName())
                .orderByDesc(SysJobLog::getCreateTime));
    }

    /**
     * 通过调度任务日志ID查询调度信息
     * 
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    @Override
    public SysJobLog selectJobLogById(Long jobLogId)
    {
        return getById(jobLogId);
    }

    /**
     * 新增任务日志
     * 
     * @param jobLog 调度日志信息
     */
    @Override
    public void addJobLog(SysJobLog jobLog)
    {
        save(jobLog);
    }

    /**
     * 批量删除调度日志信息
     * 
     * @param logIds 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteJobLogByIds(Long[] logIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(logIds));
    }

    /**
     * 删除任务日志
     * 
     * @param jobId 调度日志ID
     */
    @Override
    public int deleteJobLogById(Long jobId)
    {
        return baseMapper.deleteById(jobId);
    }

    /**
     * 清空任务日志
     */
    @Override
    public void cleanJobLog()
    {
        remove(new LambdaQueryWrapper<>());
    }
}
