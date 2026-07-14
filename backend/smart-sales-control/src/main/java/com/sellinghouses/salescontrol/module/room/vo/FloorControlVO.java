package com.sellinghouses.salescontrol.module.room.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "销控楼层统计")
public class FloorControlVO {

    @Schema(description = "楼栋ID")
    private Long unitId;

    @Schema(description = "楼层")
    private Integer floorNo;

    @Schema(description = "总房源数")
    private Long totalCount;

    @Schema(description = "待售数")
    private Long availableCount;

    @Schema(description = "已售数")
    private Long soldCount;

    @Schema(description = "锁定数")
    private Long lockedCount;
}
