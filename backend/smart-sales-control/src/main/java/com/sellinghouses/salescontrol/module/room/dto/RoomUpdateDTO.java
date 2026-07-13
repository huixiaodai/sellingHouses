package com.sellinghouses.salescontrol.module.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "修改房源请求")
public class RoomUpdateDTO {

    @NotNull(message = "房源ID不能为空")
    @Schema(description = "房源ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotNull(message = "楼盘ID不能为空")
    @Schema(description = "楼盘ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long buildingId;

    @NotNull(message = "楼栋ID不能为空")
    @Schema(description = "楼栋ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long unitId;

    @NotBlank(message = "房号不能为空")
    @Schema(description = "房号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roomNo;

    @NotNull(message = "楼层不能为空")
    @Schema(description = "楼层", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer floorNo;

    @NotNull(message = "面积不能为空")
    @DecimalMin(value = "0.01", message = "面积必须大于0")
    @Schema(description = "面积", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal area;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.00", message = "价格不能小于0")
    @Schema(description = "价格", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @Schema(description = "封面图")
    private String cover;

    @Schema(description = "房源图库JSON数组")
    private String images;

    @Schema(description = "户型")
    private String layout;

    @Schema(description = "朝向")
    private String orientation;

    @Schema(description = "装修")
    private String decoration;

    @NotNull(message = "房源状态不能为空")
    @Min(value = 0, message = "房源状态不合法")
    @Max(value = 1, message = "房源状态不合法")
    @Schema(description = "状态：0待售，1已售", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
