package com.iuiga.qa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iuiga.qa.domain.QaSubject;

import java.util.List;

/**
 * 问题Service
 */
public interface QaSubjectService extends IService<QaSubject> {
    /**
     * 查询问答列表
     * @param qaSubject
     * @return
     */
    List<QaSubject> listSubject(QaSubject qaSubject);

    /**
     * 查询问答详情
     * @param subjectId
     * @return
     */
    QaSubject getSubjectById(Long subjectId);

    /**
     * 插入问答
     * @param qaSubject
     * @return
     */
    boolean insertSubject(QaSubject qaSubject);

    /**
     * 更新问答
     * @param qaSubject
     * @return
     */
    boolean updateSubject(QaSubject qaSubject);

    /**
     * 删除问答
     * @param subjectIds
     * @return
     */
    boolean deleteSubjectByIds(Long[] subjectIds);
}
