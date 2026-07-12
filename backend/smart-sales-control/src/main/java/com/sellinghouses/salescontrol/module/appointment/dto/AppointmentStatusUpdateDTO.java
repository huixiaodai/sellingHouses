package com.sellinghouses.salescontrol.module.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "修改预约状态请求")
public class AppointmentStatusUpdateDTO {

    @NotNull(message = "预约ID不能为空")
    @Schema(description = "预约ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotNull(message = "预约状态不能为空")
    @Schema(description = "预约状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    @Schema(description = "分配销售ID")
    private Long salesUserId;
}
