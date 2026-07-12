package com.sellinghouses.salescontrol.module.building.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "修改楼栋请求")
public class BuildingUnitUpdateDTO {

    @NotNull(message = "楼栋ID不能为空")
    @Schema(description = "楼栋ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotNull(message = "楼盘ID不能为空")
    @Schema(description = "楼盘ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long buildingId;

    @NotBlank(message = "楼栋名称不能为空")
    @Schema(description = "楼栋名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Min(value = 0, message = "排序号不能小于0")
    @Schema(description = "排序号")
    private Integer sortNo = 0;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态不合法")
    @Max(value = 1, message = "状态不合法")
    @Schema(description = "状态：1启用，0禁用", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
}
