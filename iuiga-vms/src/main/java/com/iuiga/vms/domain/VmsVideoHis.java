package com.iuiga.vms.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 视频历史
 */
@Data
@TableName("vms_video_his")
public class VmsVideoHis {
    /** 视频历史ID */
    @TableId
    private Long videoHisId;
    /** 视频UUID */
    private String videoUuid;
    /** 视频快照 */
    private String snapShot;
    /** 创建者 */
    private String createBy;
    /** 创建时间 */
    private Date createTime;
}
