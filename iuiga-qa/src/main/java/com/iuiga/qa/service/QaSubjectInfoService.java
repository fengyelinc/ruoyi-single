package com.iuiga.qa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iuiga.qa.domain.QaSubjectInfo;

import java.util.List;

/**
 * 问题详情Service
 */
public interface QaSubjectInfoService extends IService<QaSubjectInfo> {
    /**
     * 根据问答ID查询问答详情
     * @param subjectId
     * @return
     */
    List<QaSubjectInfo> listBySubjectId(Long subjectId);

    /**
     * 根据问答ID找到问答数据树状列表
     * @param subjectId
     * @return
     */
    List<QaSubjectInfo> getTreeBySubjectId(Long subjectId);

    /**
     * 插入数据
     * @param qaSubjectInfo
     * @return
     */
    boolean insertSubjectInfo(QaSubjectInfo qaSubjectInfo);

    /**
     * 编辑数据
     * @param qaSubjectInfo
     * @return
     */
    boolean updateSubjectInfo(QaSubjectInfo qaSubjectInfo);

    /**
     * 删除数据
     * @param subjectInfoId
     * @return
     */
    boolean deleteSubjectInfoById(Long subjectInfoId);
}
