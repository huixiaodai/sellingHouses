package com.sellinghouses.salescontrol.module.building.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "楼栋响应")
public class BuildingUnitVO {

    @Schema(description = "楼栋ID")
    private final Long id;

    @Schema(description = "楼盘ID")
    private final Long buildingId;

    @Schema(description = "楼盘名称")
    private final String buildingName;

    @Schema(description = "楼栋名称")
    private final String name;

    @Schema(description = "排序号")
    private final Integer sortNo;

    @Schema(description = "状态")
    private final Integer status;

    @Schema(description = "创建时间")
    private final LocalDateTime createTime;
}
