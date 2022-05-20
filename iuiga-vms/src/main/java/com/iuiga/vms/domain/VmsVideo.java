package com.iuiga.vms.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 视频
 */
@Data
@TableName("vms_video")
public class VmsVideo {
    /** 视频ID */
    @TableId
    private Long videoId;
    /** 视频UUID */
    private String videoUuid;
    /** 标题 */
    private String title;
    /** 描述 */
    private String description;
    /** 作者 */
    private String author;
    /** 视频路径 */
    private String videoPath;
    /** 视频下载地址 */
    private String videoUrl;
    /** 封面路径名称 */
    private String coverPath;
    /** 封面下载地址 */
    private String coverUrl;
    /** 状态，0-启用，1-停用 */
    private String status;
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
}
