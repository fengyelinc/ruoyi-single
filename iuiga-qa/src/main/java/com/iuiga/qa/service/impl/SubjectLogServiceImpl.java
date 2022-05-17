package com.iuiga.qa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iuiga.common.utils.StringUtils;
import com.iuiga.qa.domain.SubjectLog;
import com.iuiga.qa.mapper.SubjectLogMapper;
import com.iuiga.qa.service.SubjectLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 问答日志Service
 */
@Service
public class SubjectLogServiceImpl extends ServiceImpl<SubjectLogMapper, SubjectLog> implements SubjectLogService {
    /**
     * 查询问答日志列表
     *
     * @param subjectLog
     * @return
     */
    @Override
    public List<SubjectLog> listSubjectLog(SubjectLog subjectLog) {
        return list(new LambdaQueryWrapper<SubjectLog>()
                .like(StringUtils.isNotBlank(subjectLog.getTitle()), SubjectLog::getTitle, subjectLog.getTitle())
                .ge(subjectLog.getBeginTime()!=null, SubjectLog::getCreateTime, subjectLog.getBeginTime())
                .le(subjectLog.getEndTime()!=null, SubjectLog::getEndTime, subjectLog.getEndTime()));
    }

    /**
     * 根据ID查询问答日志详情
     *
     * @param subjectLogId
     * @return
     */
    @Override
    public SubjectLog getSubjectLogById(Long subjectLogId) {
        return getById(subjectLogId);
    }
}
