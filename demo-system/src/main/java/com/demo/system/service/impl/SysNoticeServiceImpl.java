package com.demo.system.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.system.domain.SysNotice;
import com.demo.system.mapper.SysNoticeMapper;
import com.demo.system.service.ISysNoticeService;

/**
 * 公告 服务层实现
 * 
 * @author guiguzi
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService
{

    /**
     * 查询公告信息
     * 
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice selectNoticeById(Long noticeId)
    {
        return getById(noticeId);
    }

    /**
     * 查询公告列表
     * 
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice)
    {
        return list(new LambdaQueryWrapper<SysNotice>()
                .eq(StringUtils.isNotBlank(notice.getNoticeType()), SysNotice::getNoticeType, notice.getNoticeType())
                .like(StringUtils.isNotBlank(notice.getNoticeTitle()), SysNotice::getNoticeTitle, notice.getNoticeTitle())
                .like(StringUtils.isNotBlank(notice.getCreateBy()), SysNotice::getCreateBy, notice.getCreateBy()));
    }

    /**
     * 新增公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(SysNotice notice)
    {
        notice.setCreateTime(new Date());
        return baseMapper.insert(notice);
    }

    /**
     * 修改公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(SysNotice notice)
    {
        notice.setUpdateTime(new Date());
        return baseMapper.updateById(notice);
    }

    /**
     * 删除公告对象
     * 
     * @param noticeId 公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeById(Long noticeId)
    {
        return baseMapper.deleteById(noticeId);
    }

    /**
     * 批量删除公告信息
     * 
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(Long[] noticeIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(noticeIds));
    }
}
