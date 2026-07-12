package com.sellinghouses.salescontrol.module.room.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {

    private Long id;

    private Long buildingId;

    private Long unitId;

    private String roomNo;

    private Integer floorNo;

    private BigDecimal area;

    private BigDecimal price;

    private String layout;

    private String orientation;

    private String decoration;

    private Integer status;

    private String remark;

    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}
