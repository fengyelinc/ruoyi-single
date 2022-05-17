package com.iuiga.qa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iuiga.qa.domain.Subject;

import java.util.List;

/**
 * 问题Service
 */
public interface SubjectService extends IService<Subject> {
    /**
     * 查询问答列表
     * @param subject
     * @return
     */
    List<Subject> listSubject(Subject subject);

    /**
     * 查询问答详情
     * @param subjectId
     * @return
     */
    Subject getSubjectById(Long subjectId);

    /**
     * 插入问答
     * @param subject
     * @return
     */
    boolean insertSubject(Subject subject);

    /**
     * 更新问答
     * @param subject
     * @return
     */
    boolean updateSubject(Subject subject);

    /**
     * 删除问答
     * @param subjectIds
     * @return
     */
    boolean deleteSubjectByIds(Long[] subjectIds);
}
