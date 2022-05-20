package com.iuiga.qa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iuiga.common.utils.SecurityUtils;
import com.iuiga.common.utils.StringUtils;
import com.iuiga.common.utils.uuid.UUID;
import com.iuiga.qa.domain.QaSubject;
import com.iuiga.qa.domain.QaSubjectInfo;
import com.iuiga.qa.mapper.QaSubjectMapper;
import com.iuiga.qa.service.QaSubjectHisService;
import com.iuiga.qa.service.QaSubjectInfoService;
import com.iuiga.qa.service.QaSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 问题Service
 */
@Service
public class QaSubjectServiceImpl extends ServiceImpl<QaSubjectMapper, QaSubject> implements QaSubjectService {

    @Autowired
    private QaSubjectInfoService qaSubjectInfoService;

    @Autowired
    private QaSubjectHisService qaSubjectHisService;

    /**
     * 查询问答列表
     *
     * @param qaSubject
     * @return
     */
    @Override
    public List<QaSubject> listSubject(QaSubject qaSubject) {
        return list(new LambdaQueryWrapper<QaSubject>()
                .like(StringUtils.isNotBlank(qaSubject.getTitle()), QaSubject::getTitle, qaSubject.getTitle()));
    }

    /**
     * 初始化详情数据
     * @param list
     */
    private void initDetailData(List<QaSubject> list) {
        if(list==null || list.size()==0) {
            return;
        }
        List<Long> subjectIdList = list.stream().map(QaSubject::getSubjectId).collect(Collectors.toList());
        List<QaSubjectInfo> infoList = qaSubjectInfoService.listByIds(subjectIdList);
        Map<Long, List<QaSubjectInfo>> subjectIdMap = infoList.stream().collect(Collectors.groupingBy(QaSubjectInfo::getSubjectId));
        for (QaSubject qaSubject : list) {
            if(subjectIdMap.containsKey(qaSubject.getSubjectId())) {
                qaSubject.setInfoList(subjectIdMap.get(qaSubject.getSubjectId()));
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
    public QaSubject getSubjectById(Long subjectId) {
        QaSubject qaSubject = getById(subjectId);
        qaSubject.setInfoTreeList(qaSubjectInfoService.getTreeBySubjectId(subjectId));
        return qaSubject;
    }

    /**
     * 插入问答
     *
     * @param qaSubject
     * @return
     */
    @Override
    public boolean insertSubject(QaSubject qaSubject) {
        String uuid = UUID.randomUUID(true).toString();
        qaSubject.setCreateBy(SecurityUtils.getUsername());
        qaSubject.setCreateTime(new Date());
        qaSubject.setSubjectUuid(uuid);
        try {
            return save(qaSubject);
        } finally {
            qaSubjectHisService.generateHistory(Collections.singletonList(qaSubject));
        }
    }

    /**
     * 更新问答
     *
     * @param qaSubject
     * @return
     */
    @Override
    public boolean updateSubject(QaSubject qaSubject) {
        qaSubject.setUpdateBy(SecurityUtils.getUsername());
        qaSubject.setUpdateTime(new Date());
        try {
            return updateById(qaSubject);
        } finally {
            initDetailData(Collections.singletonList(qaSubject));
            qaSubjectHisService.generateHistory(Collections.singletonList(qaSubject));
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
        List<QaSubject> qaSubjectList = listByIds(Arrays.asList(subjectIds));
        initDetailData(qaSubjectList);
        qaSubjectHisService.generateHistory(qaSubjectList);
        return removeByIds(Arrays.asList(subjectIds)) && qaSubjectInfoService.remove(new LambdaQueryWrapper<QaSubjectInfo>().in(QaSubjectInfo::getSubjectId, Arrays.asList(subjectIds)));
    }
}
