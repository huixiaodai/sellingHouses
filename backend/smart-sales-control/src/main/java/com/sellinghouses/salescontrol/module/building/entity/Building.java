package com.sellinghouses.salescontrol.module.building.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Building {

    private Long id;

    private String name;

    private String cover;

    private String bannerImages;

    private String developer;

    private String address;

    private String description;

    private LocalDateTime openingTime;

    private LocalDateTime deliveryTime;

    private Integer status;

    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}
