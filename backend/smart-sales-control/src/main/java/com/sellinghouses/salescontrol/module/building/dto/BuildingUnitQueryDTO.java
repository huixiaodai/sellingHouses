package com.sellinghouses.salescontrol.module.building.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildingUnitQueryDTO {

    private Long id;

    private Long buildingId;

    private String buildingName;

    private String name;

    private Integer sortNo;

    private Integer status;

    private LocalDateTime createTime;
}
