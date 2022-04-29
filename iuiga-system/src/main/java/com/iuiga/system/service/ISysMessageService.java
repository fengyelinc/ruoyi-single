package com.iuiga.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iuiga.system.domain.SysMessage;

import java.util.List;

/**
 * 消息Service
 */
public interface ISysMessageService extends IService<SysMessage> {

    /**
     * 查询消息列表
     * @param message
     * @return
     */
    List<SysMessage> listMessage(SysMessage message);

    /**
     * 查询消息详情
     * @param messageId
     * @return
     */
    SysMessage getInfo(Long messageId);

    /**
     * 新增消息
     * @param message
     * @return
     */
    boolean addMessage(SysMessage message);

    /**
     * 编辑消息
     * @param message
     * @return
     */
    boolean editMessage(SysMessage message);

    /**
     * 删除消息
     * @param messageId
     * @return
     */
    boolean deleteMessage(String messageId);

    /**
     * 加载所有消息模板到缓存中
     */
    void loadingMessageCache();

    /**
     * 清空所有消息缓存
     */
    void clearMessageCache();

    /**
     * 重置消息缓存
     */
    void resetMessageCache();

    /**
     * 校验消息唯一性
     * @param message
     * @return
     */
    String checkMessageKeyUnique(SysMessage message);

}
