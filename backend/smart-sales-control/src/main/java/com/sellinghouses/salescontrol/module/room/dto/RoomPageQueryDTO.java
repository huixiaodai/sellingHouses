package com.sellinghouses.salescontrol.module.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "房源分页查询请求")
public class RoomPageQueryDTO {

    @Min(value = 1, message = "页码必须大于0")
    @Schema(description = "页码")
    private Integer pageNo = 1;

    @Min(value = 1, message = "每页条数必须大于0")
    @Schema(description = "每页条数")
    private Integer pageSize = 10;

    @Schema(description = "楼盘ID")
    private Long buildingId;

    @Schema(description = "楼栋ID")
    private Long unitId;

    @Schema(description = "楼层")
    private Integer floorNo;

    @Schema(description = "房号")
    private String roomNo;

    @Schema(description = "状态：0待售，1已预订，2已售，3不可售")
    private Integer status;
}
