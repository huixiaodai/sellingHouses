package com.sellinghouses.salescontrol.module.room.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Room response")
public class RoomVO {

    @Schema(description = "Room ID")
    private final Long id;

    @Schema(description = "Building ID")
    private final Long buildingId;

    @Schema(description = "Building name")
    private final String buildingName;

    @Schema(description = "Building unit ID")
    private final Long unitId;

    @Schema(description = "Building unit name")
    private final String unitName;

    @Schema(description = "Room number")
    private final String roomNo;

    @Schema(description = "Floor number")
    private final Integer floorNo;

    @Schema(description = "Area")
    private final BigDecimal area;

    @Schema(description = "Price")
    private final BigDecimal price;

    @Schema(description = "Cover image")
    private final String cover;

    @Schema(description = "Room image JSON array")
    private final String images;

    @Schema(description = "Layout")
    private final String layout;

    @Schema(description = "Orientation")
    private final String orientation;

    @Schema(description = "Decoration")
    private final String decoration;

    @Schema(description = "Status")
    private final Integer status;

    @Schema(description = "Remark")
    private final String remark;

    @Schema(description = "Create time")
    private final LocalDateTime createTime;
}
