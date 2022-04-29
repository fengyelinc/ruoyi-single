package com.iuiga.system.service.impl;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iuiga.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iuiga.system.domain.SysOperLog;
import com.iuiga.system.mapper.SysOperLogMapper;
import com.iuiga.system.service.ISysOperLogService;

/**
 * 操作日志 服务层处理
 * 
 * @author guiguzi
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements ISysOperLogService
{

    /**
     * 新增操作日志
     * 
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(SysOperLog operLog)
    {
        operLog.setOperTime(new Date());
        save(operLog);
    }

    /**
     * 查询系统操作日志集合
     * 
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog)
    {
        Map<String, Object> params = operLog.getParams();
        return list(new LambdaQueryWrapper<SysOperLog>()
                .ge(params.get("beginTime")!=null, SysOperLog::getOperTime, params.get("beginTime"))
                .ge(params.get("endTime")!=null, SysOperLog::getOperTime, params.get("endTime"))
                .eq(StringUtils.isNotBlank(operLog.getOperName()), SysOperLog::getOperName, operLog.getOperName())
                .in(operLog.getBusinessTypes()!=null && operLog.getBusinessTypes().length>0, SysOperLog::getBusinessType, operLog.getBusinessTypes()==null||operLog.getBusinessTypes().length==0?new ArrayList<>():Arrays.asList(operLog.getBusinessTypes()))
                .eq(operLog.getBusinessType()!=null, SysOperLog::getBusinessType, operLog.getBusinessType())
                .like(StringUtils.isNotBlank(operLog.getTitle()), SysOperLog::getTitle, operLog.getTitle())
                .orderByDesc(SysOperLog::getOperId));
    }

    /**
     * 批量删除系统操作日志
     * 
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(operIds));
    }

    /**
     * 查询操作日志详细
     * 
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperLog selectOperLogById(Long operId)
    {
        return getById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog()
    {
        remove(new LambdaQueryWrapper<>());
    }
}
