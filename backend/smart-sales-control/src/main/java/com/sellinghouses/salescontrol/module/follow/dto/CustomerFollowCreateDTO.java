package com.sellinghouses.salescontrol.module.follow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "新增客户跟进请求")
public class CustomerFollowCreateDTO {

    @Schema(description = "预约ID")
    private Long appointmentId;

    @NotNull(message = "客户ID不能为空")
    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long customerUserId;

    @NotNull(message = "跟进方式不能为空")
    @Schema(description = "跟进方式", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer followType;

    @NotBlank(message = "跟进内容不能为空")
    @Schema(description = "跟进内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String followContent;

    @Schema(description = "下次跟进时间")
    private LocalDateTime nextFollowTime;
}
