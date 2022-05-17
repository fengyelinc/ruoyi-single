package com.iuiga.qa.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 问答历史
 */
@Data
@TableName("qa_subject_his")
public class SubjectHis {
    /** 问答历史ID */
    @TableId
    private Long subjectHisId;
    /** 问答UUID */
    private String subjectUuid;
    /** 版本 */
    private String version;
    /** 问答快照 */
    private String snapShot;
    /** 创建者 */
    private String createBy;
    /** 创建时间 */
    private Date createTime;
}
