package com.iuiga.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iuiga.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * 消息模板
 */
@Data
@TableName("sys_message")
public class SysMessage extends BaseEntity {
    /** 主键ID */
    @TableId
    private Long messageId;
    /** 消息编码 */
    private String messageCode;
    /** 消息分组 */
    private String messageGroup;
    /** 消息模板 */
    private String messageContent;

    /** 导出ID列表 */
    @TableField(exist = false)
    private List<Long> exportIdList;
}
