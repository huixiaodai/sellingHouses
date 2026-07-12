package com.sellinghouses.salescontrol.module.follow.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerFollow {

    private Long id;

    private Long appointmentId;

    private Long customerUserId;

    private Long salesUserId;

    private Integer followType;

    private String followContent;

    private LocalDateTime nextFollowTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}
