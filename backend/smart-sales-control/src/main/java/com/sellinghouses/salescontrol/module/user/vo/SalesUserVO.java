package com.sellinghouses.salescontrol.module.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Sales user response")
public class SalesUserVO {

    @Schema(description = "User ID")
    private Long id;

    @Schema(description = "Username")
    private String username;

    @Schema(description = "Real name")
    private String realName;

    @Schema(description = "Phone")
    private String phone;
}
