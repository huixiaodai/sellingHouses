package com.sellinghouses.salescontrol.module.room.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "销控楼栋统计")
public class UnitControlVO {

    @Schema(description = "楼栋ID")
    private Long id;

    @Schema(description = "楼盘ID")
    private Long buildingId;

    @Schema(description = "楼栋名称")
    private String name;

    @Schema(description = "排序号")
    private Integer sortNo;

    @Schema(description = "总房源数")
    private Long totalCount;

    @Schema(description = "待售数")
    private Long availableCount;

    @Schema(description = "已售数")
    private Long soldCount;

    @Schema(description = "锁定数")
    private Long lockedCount;
}
