package com.iuiga.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iuiga.common.constant.UserConstants;
import com.iuiga.common.utils.MessageUtils;
import com.iuiga.common.utils.SecurityUtils;
import com.iuiga.common.utils.StringUtils;
import com.iuiga.system.domain.SysMessage;
import com.iuiga.system.mapper.SysMessageMapper;
import com.iuiga.system.service.ISysMessageService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 消息Service
 */
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {

    /**
     * 查询消息列表
     * @param message
     * @return
     */
    @Override
    public List<SysMessage> listMessage(SysMessage message) {
        return list(new LambdaQueryWrapper<SysMessage>()
                .like(StringUtils.isNotBlank(message.getMessageGroup()), SysMessage::getMessageGroup, message.getMessageGroup())
                .like(StringUtils.isNotBlank(message.getMessageContent()), SysMessage::getMessageContent, message.getMessageContent()));
    }

    /**
     * 查询消息详情
     * @param messageId
     * @return
     */
    @Override
    public SysMessage getInfo(Long messageId) {
        return getById(messageId);
    }

    /**
     * 新增消息
     * @param message
     * @return
     */
    @Override
    public boolean addMessage(SysMessage message) {
        message.setCreateBy(SecurityUtils.getUsername());
        message.setCreateTime(new Date());
        boolean result = save(message);
        if(result) {
            MessageUtils.setMessageCache(message.getMessageCode(), message.getMessageContent());
        }
        return result;
    }

    /**
     * 编辑消息
     * @param message
     * @return
     */
    @Override
    public boolean editMessage(SysMessage message) {
        message.setUpdateBy(SecurityUtils.getUsername());
        message.setUpdateTime(new Date());
        boolean result = updateById(message);
        if(result) {
            MessageUtils.setMessageCache(message.getMessageCode(), message.getMessageContent());
        }
        return result;
    }

    /**
     * 删除消息
     * @param messageId
     * @return
     */
    @Override
    public boolean deleteMessage(String messageId) {
        List<Long> messageIds = StringUtils.isBlank(messageId)?new ArrayList<>() : Arrays.stream(messageId.split(",")).map(Long::valueOf).collect(Collectors.toList());
        if(messageIds.size()>0) {
            List<SysMessage> messages = listByIds(messageIds);
            for (SysMessage message: messages) {
                removeById(message.getMessageId());
                MessageUtils.removeMessageCache(message.getMessageCode());
            }
        }
        return true;
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingMessageCache()
    {
        List<SysMessage> messagesList = list();
        for (SysMessage message : messagesList)
        {
            MessageUtils.setMessageCache(message.getMessageCode(), message.getMessageContent());
        }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearMessageCache()
    {
        MessageUtils.clearMessageCache();
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetMessageCache()
    {
        clearMessageCache();
        loadingMessageCache();
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param message 参数配置信息
     * @return 结果
     */
    @Override
    public String checkMessageKeyUnique(SysMessage message)
    {
        long messageId = StringUtils.isNull(message.getMessageId()) ? -1L : message.getMessageId();
        SysMessage info = getOne(new LambdaQueryWrapper<SysMessage>().eq(SysMessage::getMessageCode, message.getMessageCode()).last(" limit 1"));
        if (StringUtils.isNotNull(info) && info.getMessageId() != messageId)
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

}
