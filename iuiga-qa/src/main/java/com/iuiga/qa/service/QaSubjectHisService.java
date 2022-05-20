package com.iuiga.qa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iuiga.qa.domain.QaSubject;
import com.iuiga.qa.domain.QaSubjectHis;

import java.util.List;

/**
 * 问答历史Service
 */
public interface QaSubjectHisService extends IService<QaSubjectHis> {
    /**
     * 创建历史快照
     * @param qaSubject
     */
    void generateHistory(List<QaSubject> qaSubject);
}
