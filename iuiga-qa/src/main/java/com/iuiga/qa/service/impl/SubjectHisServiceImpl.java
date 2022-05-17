package com.iuiga.qa.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iuiga.common.utils.SecurityUtils;
import com.iuiga.qa.domain.Subject;
import com.iuiga.qa.domain.SubjectHis;
import com.iuiga.qa.domain.SubjectInfo;
import com.iuiga.qa.mapper.SubjectHisMapper;
import com.iuiga.qa.service.SubjectHisService;
import com.iuiga.qa.service.SubjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 问答历史Service
 */
@Service
public class SubjectHisServiceImpl extends ServiceImpl<SubjectHisMapper, SubjectHis> implements SubjectHisService {

    @Autowired
    private SubjectInfoService infoService;

    /**
     * 创建历史快照
     *
     * @param subject
     */
    @Override
    public void generateHistory(Subject subject) {
        List<SubjectInfo> infoList = infoService.listBySubjectId(subject.getSubjectId());
        subject.setInfoList(infoList);
        SubjectHis his = new SubjectHis();
        his.setSubjectUuid(subject.getSubjectUuid());
        his.setVersion(subject.getVersion());
        his.setSnapShot(JSON.toJSONString(subject));
        his.setCreateBy(SecurityUtils.getUsername());
        his.setCreateTime(new Date());
        save(his);
    }
}
