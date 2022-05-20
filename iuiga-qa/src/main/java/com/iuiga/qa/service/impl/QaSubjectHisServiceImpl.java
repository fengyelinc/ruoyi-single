package com.iuiga.qa.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iuiga.common.utils.SecurityUtils;
import com.iuiga.qa.domain.QaSubject;
import com.iuiga.qa.domain.QaSubjectHis;
import com.iuiga.qa.mapper.QaSubjectHisMapper;
import com.iuiga.qa.service.QaSubjectHisService;
import com.iuiga.qa.service.QaSubjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 问答历史Service
 */
@Service
public class QaSubjectHisServiceImpl extends ServiceImpl<QaSubjectHisMapper, QaSubjectHis> implements QaSubjectHisService {

    @Autowired
    private QaSubjectInfoService infoService;

    /**
     * 创建历史快照
     *
     * @param qaSubjectList
     */
    @Override
    public void generateHistory(List<QaSubject> qaSubjectList) {
        List<QaSubjectHis> hisList = new ArrayList<>();
        for (QaSubject qaSubject : qaSubjectList) {
            QaSubjectHis his = new QaSubjectHis();
            his.setSubjectUuid(qaSubject.getSubjectUuid());
            his.setVersion(qaSubject.getVersion());
            his.setSnapShot(JSON.toJSONString(qaSubject));
            his.setCreateBy(SecurityUtils.getUsername());
            his.setCreateTime(new Date());
            hisList.add(his);
        }
        if(hisList.size()>0) {
            saveBatch(hisList);
        }
    }
}
