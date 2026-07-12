package com.sellinghouses.salescontrol.module.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "管理员预约分页查询请求")
public class AdminAppointmentPageQueryDTO {

    @Min(value = 1, message = "页码必须大于0")
    @Schema(description = "页码")
    private Integer pageNo = 1;

    @Min(value = 1, message = "每页条数必须大于0")
    @Schema(description = "每页条数")
    private Integer pageSize = 10;

    @Schema(description = "楼盘ID")
    private Long estateId;

    @Schema(description = "销售ID")
    private Long salesUserId;

    @Schema(description = "预约状态")
    private Integer status;
}
