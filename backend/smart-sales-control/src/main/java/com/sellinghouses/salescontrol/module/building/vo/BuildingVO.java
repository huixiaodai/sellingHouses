package com.sellinghouses.salescontrol.module.building.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "楼盘响应")
public class BuildingVO {

    @Schema(description = "楼盘ID")
    private final Long id;

    @Schema(description = "楼盘名称")
    private final String name;

    @Schema(description = "封面图")
    private final String cover;

    @Schema(description = "轮播图JSON数组")
    private final String bannerImages;

    @Schema(description = "开发商")
    private final String developer;

    @Schema(description = "楼盘地址")
    private final String address;

    @Schema(description = "楼盘介绍")
    private final String description;

    @Schema(description = "开盘时间")
    private final LocalDateTime openingTime;

    @Schema(description = "交房时间")
    private final LocalDateTime deliveryTime;

    @Schema(description = "状态")
    private final Integer status;

    @Schema(description = "创建时间")
    private final LocalDateTime createTime;
}
