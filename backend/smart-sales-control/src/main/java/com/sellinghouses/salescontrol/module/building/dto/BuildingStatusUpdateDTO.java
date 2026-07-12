package com.sellinghouses.salescontrol.module.building.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "修改楼盘状态请求")
public class BuildingStatusUpdateDTO {

    @NotNull(message = "楼盘ID不能为空")
    @Schema(description = "楼盘ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态不合法")
    @Max(value = 1, message = "状态不合法")
    @Schema(description = "状态：1启用，0禁用", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
}
