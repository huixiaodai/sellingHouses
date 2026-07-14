package com.sellinghouses.salescontrol.module.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "销控房源查询请求")
public class SaleControlQueryDTO {

    @Schema(description = "楼盘ID")
    private Long buildingId;

    @Schema(description = "楼栋ID")
    private Long unitId;

    @Schema(description = "楼层")
    private Integer floorNo;

    @Schema(description = "房号关键字")
    private String keyword;

    @Schema(description = "最低总价")
    private BigDecimal minPrice;

    @Schema(description = "最高总价")
    private BigDecimal maxPrice;

    @Schema(description = "最小面积")
    private BigDecimal minArea;

    @Schema(description = "最大面积")
    private BigDecimal maxArea;

    @Schema(description = "户型")
    private String layout;

    @Schema(description = "朝向")
    private String orientation;

    @Schema(description = "装修")
    private String decoration;

    @Schema(description = "状态：0待售，1已售，2锁定")
    private Integer status;
}
