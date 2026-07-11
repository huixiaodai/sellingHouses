package com.sellinghouses.salescontrol.module.user.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private Long id;

    private String username;

    private String password;

    private String realName;

    private String phone;

    private String wxOpenid;

    private String avatarUrl;

    private String primaryRoleCode;

    private Integer status;

    private LocalDateTime lastLoginTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
