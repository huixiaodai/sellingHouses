package com.sellinghouses.salescontrol.module.room.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "房源响应")
public class RoomVO {

    @Schema(description = "房源ID")
    private final Long id;

    @Schema(description = "楼盘ID")
    private final Long buildingId;

    @Schema(description = "楼栋ID")
    private final Long unitId;

    @Schema(description = "房号")
    private final String roomNo;

    @Schema(description = "楼层")
    private final Integer floorNo;

    @Schema(description = "面积")
    private final BigDecimal area;

    @Schema(description = "价格")
    private final BigDecimal price;

    @Schema(description = "户型")
    private final String layout;

    @Schema(description = "朝向")
    private final String orientation;

    @Schema(description = "装修")
    private final String decoration;

    @Schema(description = "状态")
    private final Integer status;

    @Schema(description = "备注")
    private final String remark;

    @Schema(description = "创建时间")
    private final LocalDateTime createTime;
}
