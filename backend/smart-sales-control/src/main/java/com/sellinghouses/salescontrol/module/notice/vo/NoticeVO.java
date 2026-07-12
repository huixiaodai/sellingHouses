package com.sellinghouses.salescontrol.module.notice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "公告响应")
public class NoticeVO {

    @Schema(description = "公告ID")
    private final Long id;

    @Schema(description = "公告标题")
    private final String title;

    @Schema(description = "公告内容")
    private final String content;

    @Schema(description = "发布人ID")
    private final Long publisherId;

    @Schema(description = "可见角色")
    private final String targetRole;

    @Schema(description = "状态")
    private final Integer status;

    @Schema(description = "发布时间")
    private final LocalDateTime publishTime;
}
