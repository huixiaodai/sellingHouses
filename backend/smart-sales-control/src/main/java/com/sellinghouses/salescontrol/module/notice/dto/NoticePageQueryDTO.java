package com.sellinghouses.salescontrol.module.notice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "公告分页查询请求")
public class NoticePageQueryDTO {

    @Schema(description = "公告标题")
    private String title;

    @Min(value = 1, message = "页码必须大于0")
    @Schema(description = "页码")
    private Integer pageNo = 1;

    @Min(value = 1, message = "每页条数必须大于0")
    @Schema(description = "每页条数")
    private Integer pageSize = 10;
}
