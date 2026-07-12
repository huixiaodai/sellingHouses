package com.sellinghouses.salescontrol.module.building.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "楼栋分页查询请求")
public class BuildingUnitPageQueryDTO {

    @Min(value = 1, message = "页码必须大于0")
    @Schema(description = "页码")
    private Integer pageNo = 1;

    @Min(value = 1, message = "每页条数必须大于0")
    @Schema(description = "每页条数")
    private Integer pageSize = 10;

    @Schema(description = "楼盘ID")
    private Long buildingId;

    @Schema(description = "楼栋名称")
    private String name;

    @Schema(description = "状态：1启用，0禁用")
    private Integer status;
}
