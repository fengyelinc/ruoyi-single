package com.iuiga.qa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iuiga.qa.domain.SubjectLog;

import java.util.List;

/**
 * 问答日志Service
 */
public interface SubjectLogService extends IService<SubjectLog> {
    /**
     * 查询问答日志列表
     * @param subjectLog
     * @return
     */
    List<SubjectLog> listSubjectLog(SubjectLog subjectLog);

    /**
     * 根据ID查询问答日志详情
     * @param subjectLogId
     * @return
     */
    SubjectLog getSubjectLogById(Long subjectLogId);
}
