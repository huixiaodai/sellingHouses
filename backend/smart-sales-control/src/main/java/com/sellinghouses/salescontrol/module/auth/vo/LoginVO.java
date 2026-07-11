package com.sellinghouses.salescontrol.module.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "登录响应")
public class LoginVO {

    @Schema(description = "JWT Token")
    private final String token;

    @Schema(description = "用户ID")
    private final Long userId;

    @Schema(description = "登录账号")
    private final String username;

    @Schema(description = "真实姓名")
    private final String realName;

    @Schema(description = "角色")
    private final Integer role;

    @Schema(description = "登录后首页路径")
    private final String homePath;
}
