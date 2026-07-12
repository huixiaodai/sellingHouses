package com.sellinghouses.salescontrol.module.room.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomQueryDTO {

    private Long id;

    private Long buildingId;

    private String buildingName;

    private Long unitId;

    private String unitName;

    private String roomNo;

    private Integer floorNo;

    private BigDecimal area;

    private BigDecimal price;

    private String cover;

    private String images;

    private String layout;

    private String orientation;

    private String decoration;

    private Integer status;

    private String remark;

    private LocalDateTime createTime;
}
