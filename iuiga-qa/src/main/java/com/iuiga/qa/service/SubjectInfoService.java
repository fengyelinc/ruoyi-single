package com.iuiga.qa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iuiga.qa.domain.SubjectInfo;

import java.util.List;

/**
 * 问题详情Service
 */
public interface SubjectInfoService extends IService<SubjectInfo> {
    /**
     * 根据问答ID查询问答详情
     * @param subjectId
     * @return
     */
    List<SubjectInfo> listBySubjectId(Long subjectId);

    /**
     * 根据问答ID找到问答数据树状列表
     * @param subjectId
     * @return
     */
    List<SubjectInfo> getTreeBySubjectId(Long subjectId);

    /**
     * 插入数据
     * @param subjectInfo
     * @return
     */
    boolean insertSubjectInfo(SubjectInfo subjectInfo);

    /**
     * 编辑数据
     * @param subjectInfo
     * @return
     */
    boolean updateSubjectInfo(SubjectInfo subjectInfo);

    /**
     * 删除数据
     * @param subjectInfoId
     * @return
     */
    boolean deleteSubjectInfoById(Long subjectInfoId);
}
