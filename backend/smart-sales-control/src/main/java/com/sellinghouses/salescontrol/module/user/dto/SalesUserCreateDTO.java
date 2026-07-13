package com.sellinghouses.salescontrol.module.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Create sales user request")
public class SalesUserCreateDTO {

    @NotBlank(message = "账号不能为空")
    @Size(max = 64, message = "账号长度不能超过64位")
    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "姓名不能为空")
    @Size(max = 64, message = "姓名长度不能超过64位")
    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String realName;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    @NotBlank(message = "初始密码不能为空")
    @Size(min = 6, max = 32, message = "初始密码长度必须在6到32位之间")
    @Schema(description = "初始密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态：1启用，2禁用", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
}
