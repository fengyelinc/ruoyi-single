package com.iuiga.qa.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 问答详细内容
 */
@Data
@TableName("qa_subject_info")
public class SubjectInfo {
    /** 问答详情ID */
    @TableId
    private Long subjectInfoId;
    /** 问答ID */
    private Long subjectId;
    /** 父问答详情ID */
    private Long parentId;
    /** 上个问答答案 */
    private String lastAnswer;
    /** 下个问答 */
    private String nextQuestion;
    /** 得分 */
    private Double score;
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

    /** 子数据列表 */
    @TableField(exist = false)
    private List<SubjectInfo> children;
}
