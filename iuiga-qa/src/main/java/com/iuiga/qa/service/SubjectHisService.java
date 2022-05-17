package com.iuiga.qa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iuiga.qa.domain.Subject;
import com.iuiga.qa.domain.SubjectHis;

/**
 * 问答历史Service
 */
public interface SubjectHisService extends IService<SubjectHis> {
    /**
     * 创建历史快照
     * @param subject
     */
    void generateHistory(Subject subject);
}
