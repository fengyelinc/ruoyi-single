package com.iuiga.qa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iuiga.common.utils.SecurityUtils;
import com.iuiga.common.utils.StringUtils;
import com.iuiga.common.utils.uuid.UUID;
import com.iuiga.qa.domain.Subject;
import com.iuiga.qa.domain.SubjectInfo;
import com.iuiga.qa.mapper.SubjectMapper;
import com.iuiga.qa.service.SubjectHisService;
import com.iuiga.qa.service.SubjectInfoService;
import com.iuiga.qa.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 问题Service
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Autowired
    private SubjectInfoService subjectInfoService;

    @Autowired
    private SubjectHisService subjectHisService;

    /**
     * 查询问答列表
     *
     * @param subject
     * @return
     */
    @Override
    public List<Subject> listSubject(Subject subject) {
        return list(new LambdaQueryWrapper<Subject>()
                .like(StringUtils.isNotBlank(subject.getTitle()), Subject::getTitle, subject.getTitle()));
    }

    /**
     * 初始化详情数据
     * @param list
     */
    private void initDetailData(List<Subject> list) {
        if(list==null || list.size()==0) {
            return;
        }
        List<Long> subjectIdList = list.stream().map(Subject::getSubjectId).collect(Collectors.toList());
        List<SubjectInfo> infoList = subjectInfoService.listByIds(subjectIdList);
        Map<Long, List<SubjectInfo>> subjectIdMap = infoList.stream().collect(Collectors.groupingBy(SubjectInfo::getSubjectId));
        for (Subject subject : list) {
            if(subjectIdMap.containsKey(subject.getSubjectId())) {
                subject.setInfoList(subjectIdMap.get(subject.getSubjectId()));
            }
        }
    }

    /**
     * 查询问答详情
     *
     * @param subjectId
     * @return
     */
    @Override
    public Subject getSubjectById(Long subjectId) {
        Subject subject = getById(subjectId);
        subject.setInfoTreeList(subjectInfoService.getTreeBySubjectId(subjectId));
        return subject;
    }

    /**
     * 插入问答
     *
     * @param subject
     * @return
     */
    @Override
    public boolean insertSubject(Subject subject) {
        String uuid = UUID.randomUUID(true).toString();
        try {
            subject.setCreateBy(SecurityUtils.getUsername());
            subject.setCreateTime(new Date());
            subject.setSubjectUuid(uuid);
            return save(subject);
        } finally {
            subjectHisService.generateHistory(subject);
        }
    }

    /**
     * 更新问答
     *
     * @param subject
     * @return
     */
    @Override
    public boolean updateSubject(Subject subject) {
        try {
            subject.setUpdateBy(SecurityUtils.getUsername());
            subject.setUpdateTime(new Date());
            return updateById(subject);
        } finally {
            subjectHisService.generateHistory(subject);
        }
    }

    /**
     * 删除问答
     *
     * @param subjectIds
     * @return
     */
    @Override
    @Transactional
    public boolean deleteSubjectByIds(Long[] subjectIds) {
        return removeByIds(Arrays.asList(subjectIds)) && subjectInfoService.remove(new LambdaQueryWrapper<SubjectInfo>().in(SubjectInfo::getSubjectId, Arrays.asList(subjectIds)));
    }
}
