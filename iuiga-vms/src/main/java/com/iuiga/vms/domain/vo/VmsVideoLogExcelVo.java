package com.iuiga.vms.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.iuiga.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 视频日志ExcelVo
 */
@Data
public class VmsVideoLogExcelVo {
    @Excel(name = "视频日志ID")
    private String videoLogId;
    @Excel(name = "视频唯一标识")
    private String videoUuid;
    @Excel(name = "视频标题")
    private String title;
    @Excel(name = "视频名称")
    private String username;
    @Excel(name = "用户电话")
    private String phone;
    @Excel(name = "持续时间(s)")
    private String duration;
    @Excel(name = "用户标识")
    private String wxOpenId;
    @Excel(name = "用户唯一标识")
    private String wxUnionId;
    @Excel(name = "备注")
    private String remark;
    @Excel(name = "创建时间")
    private String createTime;
}
