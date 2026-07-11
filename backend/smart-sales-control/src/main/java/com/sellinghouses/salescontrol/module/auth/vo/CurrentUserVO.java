package com.sellinghouses.salescontrol.module.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "当前登录用户响应")
public class CurrentUserVO {

    @Schema(description = "用户ID")
    private final Long userId;

    @Schema(description = "登录账号")
    private final String username;

    @Schema(description = "真实姓名")
    private final String realName;

    @Schema(description = "脱敏手机号")
    private final String phone;

    @Schema(description = "角色")
    private final Integer role;

    @Schema(description = "状态")
    private final Integer status;
}
