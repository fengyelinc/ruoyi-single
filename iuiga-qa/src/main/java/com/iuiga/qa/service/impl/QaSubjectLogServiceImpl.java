package com.iuiga.qa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iuiga.common.utils.StringUtils;
import com.iuiga.qa.domain.QaSubjectLog;
import com.iuiga.qa.mapper.QaSubjectLogMapper;
import com.iuiga.qa.service.QaSubjectLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 问答日志Service
 */
@Service
public class QaSubjectLogServiceImpl extends ServiceImpl<QaSubjectLogMapper, QaSubjectLog> implements QaSubjectLogService {
    /**
     * 查询问答日志列表
     *
     * @param qaSubjectLog
     * @return
     */
    @Override
    public List<QaSubjectLog> listSubjectLog(QaSubjectLog qaSubjectLog) {
        return list(new LambdaQueryWrapper<QaSubjectLog>()
                .like(StringUtils.isNotBlank(qaSubjectLog.getTitle()), QaSubjectLog::getTitle, qaSubjectLog.getTitle())
                .ge(qaSubjectLog.getBeginTime()!=null, QaSubjectLog::getCreateTime, qaSubjectLog.getBeginTime())
                .le(qaSubjectLog.getEndTime()!=null, QaSubjectLog::getEndTime, qaSubjectLog.getEndTime()));
    }

    /**
     * 根据ID查询问答日志详情
     *
     * @param subjectLogId
     * @return
     */
    @Override
    public QaSubjectLog getSubjectLogById(Long subjectLogId) {
        return getById(subjectLogId);
    }
}
