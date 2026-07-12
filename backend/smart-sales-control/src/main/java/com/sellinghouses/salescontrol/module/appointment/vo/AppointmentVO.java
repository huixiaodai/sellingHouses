package com.sellinghouses.salescontrol.module.appointment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "预约响应")
public class AppointmentVO {

    @Schema(description = "预约ID")
    private final Long id;

    @Schema(description = "购房用户ID")
    private final Long userId;

    @Schema(description = "销售ID")
    private final Long salesUserId;

    @Schema(description = "楼盘ID")
    private final Long estateId;

    @Schema(description = "房源ID")
    private final Long roomId;

    @Schema(description = "预约时间")
    private final LocalDateTime appointmentTime;

    @Schema(description = "联系人")
    private final String contactName;

    @Schema(description = "联系电话")
    private final String contactPhone;

    @Schema(description = "备注")
    private final String remark;

    @Schema(description = "预约状态")
    private final Integer status;

    @Schema(description = "取消原因")
    private final String cancelReason;

    @Schema(description = "创建时间")
    private final LocalDateTime createTime;
}
