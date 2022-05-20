package com.iuiga.qa.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 问答日志
 */
@Data
@TableName("qa_subject_log")
public class QaSubjectLog {
    /** 日志ID */
    @TableId
    private Long subjectLogId;
    /** 问答ID */
    private Long subjectUuid;
    /** 版本号 */
    private String version;
    /** 问答标题 */
    private String title;
    /** 答案 */
    private String answer;
    /** 用户名 */
    private String username;
    /** 电话号码 */
    private String phone;
    /** 用户标识 */
    private String wxOpenId;
    /** 用户唯一标识  */
    private String wxUnionId;
    /** 分数 */
    private Double score;
    /** 备注 */
    private String remark;
    /** 创建时间 */
    private Date createTime;

    /** 开始时间 */
    @TableField(exist = false)
    private Date beginTime;
    /** 结束时间 */
    @TableField(exist = false)
    private Date endTime;
}
