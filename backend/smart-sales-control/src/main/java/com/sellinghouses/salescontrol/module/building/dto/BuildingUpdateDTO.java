package com.sellinghouses.salescontrol.module.building.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "修改楼盘请求")
public class BuildingUpdateDTO {

    @NotNull(message = "楼盘ID不能为空")
    @Schema(description = "楼盘ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotBlank(message = "楼盘名称不能为空")
    @Schema(description = "楼盘名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "封面图")
    private String cover;

    @Schema(description = "轮播图JSON数组")
    private String bannerImages;

    @Schema(description = "开发商")
    private String developer;

    @NotBlank(message = "楼盘地址不能为空")
    @Schema(description = "楼盘地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    @Schema(description = "楼盘介绍")
    private String description;

    @Schema(description = "开盘时间")
    private LocalDateTime openingTime;

    @Schema(description = "交房时间")
    private LocalDateTime deliveryTime;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态不合法")
    @Max(value = 1, message = "状态不合法")
    @Schema(description = "状态：1启用，0禁用", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
}
