package com.iuiga.qa.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * IUIGA问答
 */
@Data
@TableName("qa_subject")
public class Subject {
    /** 问答ID */
    @TableId
    private Long subjectId;
    /** 题目UUID */
    private String subjectUuid;
    /** 版本号 */
    private String version;
    /** 题目 */
    private String title;
    /** 问答类型，1-正误型，2-选择型 */
    private String type;
    /** 问答描述 */
    private String content;
    /** 备注 */
    private String remark;
    /** 创建者 */
    private String createBy;
    /** 创建时间 */
    private Date createTime;
    /** 更新者 */
    private String updateBy;
    /** 更新时间 */
    private Date updateTime;

    /** 数据列表 */
    @TableField(exist = false)
    private List<SubjectInfo> infoList;

    /** 数据树状列表 */
    @TableField(exist = false)
    private List<SubjectInfo> infoTreeList;
}
