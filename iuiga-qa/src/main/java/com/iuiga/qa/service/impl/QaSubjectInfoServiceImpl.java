package com.iuiga.qa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iuiga.common.utils.SecurityUtils;
import com.iuiga.qa.domain.QaSubjectInfo;
import com.iuiga.qa.mapper.QaSubjectInfoMapper;
import com.iuiga.qa.service.QaSubjectInfoService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 问题详情Service
 */
@Service
public class QaSubjectInfoServiceImpl extends ServiceImpl<QaSubjectInfoMapper, QaSubjectInfo> implements QaSubjectInfoService {
    /**
     * 根据问答ID查询问答详情
     *
     * @param subjectId
     * @return
     */
    @Override
    public List<QaSubjectInfo> listBySubjectId(Long subjectId) {
        return list(new LambdaQueryWrapper<QaSubjectInfo>().eq(QaSubjectInfo::getSubjectId, subjectId));
    }

    /**
     * 根据问答ID找到问答数据树状列表
     *
     * @param subjectId
     * @return
     */
    @Override
    public List<QaSubjectInfo> getTreeBySubjectId(Long subjectId) {
        List<QaSubjectInfo> infoList = list(new LambdaQueryWrapper<QaSubjectInfo>().eq(QaSubjectInfo::getSubjectId, subjectId));
        return convertTreeList(infoList);
    }

    /**
     * 转成树形结构
     * @param list
     * @return
     */
    private List<QaSubjectInfo> convertTreeList(List<QaSubjectInfo> list) {
        return getChildren(list, -1L);
    }

    /**
     * 获取子集
     * @param list
     * @param parentId
     * @return
     */
    private List<QaSubjectInfo> getChildren(List<QaSubjectInfo> list, Long parentId) {
        List<QaSubjectInfo> result = new ArrayList<>();
        List<QaSubjectInfo> infoList = list.stream().filter(item -> parentId<=0?item.getParentId()<=0:item.getParentId().equals(parentId)).collect(Collectors.toList());
        if(infoList.size() == 0) {
            return result;
        }
        for (QaSubjectInfo info : infoList) {
            info.setChildren(getChildren(list, info.getSubjectInfoId()));
            result.add(info);
        }
        return result;
    }

    /**
     * 树形转列表
     * @param list
     * @return
     */
    private List<QaSubjectInfo> treeToList(List<QaSubjectInfo> list) {
        List<QaSubjectInfo> result = new ArrayList<>();
        for (QaSubjectInfo info : list) {
            result.add(info);
            if(info.getChildren()!=null && info.getChildren().size()>0) {
                List<QaSubjectInfo> children = treeToList(info.getChildren());
                result.addAll(children);
            }
        }
        return result;
    }

    /**
     * 插入数据
     *
     * @param qaSubjectInfo
     * @return
     */
    @Override
    public boolean insertSubjectInfo(QaSubjectInfo qaSubjectInfo) {
        qaSubjectInfo.setCreateBy(SecurityUtils.getUsername());
        qaSubjectInfo.setCreateTime(new Date());
        return save(qaSubjectInfo);
    }

    /**
     * 编辑数据
     *
     * @param qaSubjectInfo
     * @return
     */
    @Override
    public boolean updateSubjectInfo(QaSubjectInfo qaSubjectInfo) {
        qaSubjectInfo.setUpdateBy(SecurityUtils.getUsername());
        qaSubjectInfo.setUpdateTime(new Date());
        return updateById(qaSubjectInfo);
    }

    /**
     * 删除数据
     *
     * @param subjectInfoId
     * @return
     */
    @Override
    public boolean deleteSubjectInfoById(Long subjectInfoId) {
        QaSubjectInfo info = getById(subjectInfoId);
        // 删除所有子节点
        List<QaSubjectInfo> infoList = list(new LambdaQueryWrapper<QaSubjectInfo>().eq(QaSubjectInfo::getSubjectId, info.getSubjectId()));
        List<QaSubjectInfo> removeTargetChildrenList = treeToList(getChildren(infoList, info.getSubjectId()));
        List<Long> removeIds = removeTargetChildrenList.stream().map(QaSubjectInfo::getSubjectId).collect(Collectors.toList());
        return removeByIds(removeIds);
    }
}
