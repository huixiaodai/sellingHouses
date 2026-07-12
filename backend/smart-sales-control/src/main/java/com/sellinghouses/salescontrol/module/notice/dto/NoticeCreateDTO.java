package com.sellinghouses.salescontrol.module.notice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "发布公告请求")
public class NoticeCreateDTO {

    @NotBlank(message = "公告标题不能为空")
    @Schema(description = "公告标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @NotBlank(message = "公告内容不能为空")
    @Schema(description = "公告内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @NotBlank(message = "可见角色不能为空")
    @Schema(description = "可见角色：ALL、ADMIN、SALES、CUSTOMER", requiredMode = Schema.RequiredMode.REQUIRED)
    private String targetRoleCode;
}
