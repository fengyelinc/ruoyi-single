package com.iuiga.qa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iuiga.common.utils.SecurityUtils;
import com.iuiga.qa.domain.SubjectInfo;
import com.iuiga.qa.mapper.SubjectInfoMapper;
import com.iuiga.qa.service.SubjectInfoService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 问题详情Service
 */
@Service
public class SubjectInfoServiceImpl extends ServiceImpl<SubjectInfoMapper, SubjectInfo> implements SubjectInfoService {
    /**
     * 根据问答ID查询问答详情
     *
     * @param subjectId
     * @return
     */
    @Override
    public List<SubjectInfo> listBySubjectId(Long subjectId) {
        return list(new LambdaQueryWrapper<SubjectInfo>().eq(SubjectInfo::getSubjectId, subjectId));
    }

    /**
     * 根据问答ID找到问答数据树状列表
     *
     * @param subjectId
     * @return
     */
    @Override
    public List<SubjectInfo> getTreeBySubjectId(Long subjectId) {
        List<SubjectInfo> infoList = list(new LambdaQueryWrapper<SubjectInfo>().eq(SubjectInfo::getSubjectId, subjectId));
        return convertTreeList(infoList);
    }

    /**
     * 转成树形结构
     * @param list
     * @return
     */
    private List<SubjectInfo> convertTreeList(List<SubjectInfo> list) {
        return getChildren(list, -1L);
    }

    /**
     * 获取子集
     * @param list
     * @param parentId
     * @return
     */
    private List<SubjectInfo> getChildren(List<SubjectInfo> list, Long parentId) {
        List<SubjectInfo> result = new ArrayList<>();
        List<SubjectInfo> infoList = list.stream().filter(item -> parentId<=0?item.getParentId()<=0:item.getParentId().equals(parentId)).collect(Collectors.toList());
        if(infoList.size() == 0) {
            return result;
        }
        for (SubjectInfo info : infoList) {
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
    private List<SubjectInfo> treeToList(List<SubjectInfo> list) {
        List<SubjectInfo> result = new ArrayList<>();
        for (SubjectInfo info : list) {
            result.add(info);
            if(info.getChildren()!=null && info.getChildren().size()>0) {
                List<SubjectInfo> children = treeToList(info.getChildren());
                result.addAll(children);
            }
        }
        return result;
    }

    /**
     * 插入数据
     *
     * @param subjectInfo
     * @return
     */
    @Override
    public boolean insertSubjectInfo(SubjectInfo subjectInfo) {
        subjectInfo.setCreateBy(SecurityUtils.getUsername());
        subjectInfo.setCreateTime(new Date());
        return save(subjectInfo);
    }

    /**
     * 编辑数据
     *
     * @param subjectInfo
     * @return
     */
    @Override
    public boolean updateSubjectInfo(SubjectInfo subjectInfo) {
        subjectInfo.setUpdateBy(SecurityUtils.getUsername());
        subjectInfo.setUpdateTime(new Date());
        return updateById(subjectInfo);
    }

    /**
     * 删除数据
     *
     * @param subjectInfoId
     * @return
     */
    @Override
    public boolean deleteSubjectInfoById(Long subjectInfoId) {
        SubjectInfo info = getById(subjectInfoId);
        // 删除所有子节点
        List<SubjectInfo> infoList = list(new LambdaQueryWrapper<SubjectInfo>().eq(SubjectInfo::getSubjectId, info.getSubjectId()));
        List<SubjectInfo> removeTargetChildrenList = treeToList(getChildren(infoList, info.getSubjectId()));
        List<Long> removeIds = removeTargetChildrenList.stream().map(SubjectInfo::getSubjectId).collect(Collectors.toList());
        return removeByIds(removeIds);
    }
}
