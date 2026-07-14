package com.sellinghouses.salescontrol.module.room.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "销控楼盘统计")
public class EstateControlVO {

    @Schema(description = "楼盘ID")
    private Long id;

    @Schema(description = "楼盘名称")
    private String name;

    @Schema(description = "封面图")
    private String cover;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "总房源数")
    private Long totalCount;

    @Schema(description = "待售数")
    private Long availableCount;

    @Schema(description = "已售数")
    private Long soldCount;

    @Schema(description = "锁定数")
    private Long lockedCount;
}
