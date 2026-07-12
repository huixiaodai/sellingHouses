package com.sellinghouses.salescontrol.module.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "预约看房请求")
public class AppointmentCreateDTO {

    @NotNull(message = "楼盘ID不能为空")
    @Schema(description = "楼盘ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long estateId;

    @Schema(description = "意向房源ID")
    private Long roomId;

    @NotNull(message = "预约时间不能为空")
    @Schema(description = "预约时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime appointmentTime;

    @NotBlank(message = "联系人不能为空")
    @Schema(description = "联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactName;

    @NotBlank(message = "联系电话不能为空")
    @Schema(description = "联系电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactPhone;

    @Schema(description = "备注")
    private String remark;
}
