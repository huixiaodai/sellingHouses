package com.sellinghouses.salescontrol.module.building.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildingUnit {

    private Long id;

    private Long buildingId;

    private String name;

    private Integer sortNo;

    private Integer status;

    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}
