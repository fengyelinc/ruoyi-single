package com.iuiga.qa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iuiga.qa.domain.QaSubjectLog;

import java.util.List;

/**
 * 问答日志Service
 */
public interface QaSubjectLogService extends IService<QaSubjectLog> {
    /**
     * 查询问答日志列表
     * @param qaSubjectLog
     * @return
     */
    List<QaSubjectLog> listSubjectLog(QaSubjectLog qaSubjectLog);

    /**
     * 根据ID查询问答日志详情
     * @param subjectLogId
     * @return
     */
    QaSubjectLog getSubjectLogById(Long subjectLogId);
}
