package com.iuiga.vms.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 视频日志
 */
@Data
@TableName("vms_video_log")
public class VmsVideoLog {
    /** 视频日志ID */
    @TableId
    private Long videoLogId;
    /** 视频UUID */
    private String videoUuid;
    /** 视频标题 */
    private String title;
    /** 视频名称 */
    private String username;
    /** 用户电话 */
    private String phone;
    /** 持续时间 */
    private Long duration;
    /** 用户标识 */
    private String wxOpenId;
    /** 用户唯一标识  */
    private String wxUnionId;
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
