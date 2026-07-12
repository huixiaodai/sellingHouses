package com.sellinghouses.salescontrol.module.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "取消预约请求")
public class AppointmentCancelDTO {

    @NotNull(message = "预约ID不能为空")
    @Schema(description = "预约ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "取消原因")
    private String cancelReason;
}
