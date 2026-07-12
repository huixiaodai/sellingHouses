package com.sellinghouses.salescontrol.module.notice.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notice {

    private Long id;

    private String title;

    private String content;

    private Long publisherId;

    private String targetRole;

    private Integer status;

    private LocalDateTime publishTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}
