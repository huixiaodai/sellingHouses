package com.sellinghouses.salescontrol.module.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "修改房源状态请求")
public class RoomStatusUpdateDTO {

    @NotNull(message = "房源ID不能为空")
    @Schema(description = "房源ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotNull(message = "房源状态不能为空")
    @Min(value = 0, message = "房源状态不合法")
    @Max(value = 3, message = "房源状态不合法")
    @Schema(description = "状态：0待售，1已预订，2已售，3不可售", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
}
