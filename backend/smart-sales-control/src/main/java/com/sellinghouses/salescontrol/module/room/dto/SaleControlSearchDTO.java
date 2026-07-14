package com.sellinghouses.salescontrol.module.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "销控房源搜索请求")
public class SaleControlSearchDTO {

    @NotBlank(message = "搜索关键字不能为空")
    @Schema(description = "房号关键字", requiredMode = Schema.RequiredMode.REQUIRED)
    private String keyword;

    @Schema(description = "楼盘ID")
    private Long buildingId;

    @Schema(description = "楼栋ID")
    private Long unitId;
}
