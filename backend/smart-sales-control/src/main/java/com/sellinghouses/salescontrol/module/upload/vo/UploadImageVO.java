package com.sellinghouses.salescontrol.module.upload.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "图片上传结果")
public class UploadImageVO {

    @Schema(description = "图片访问地址")
    private final String url;

    @Schema(description = "OSS对象Key")
    private final String objectKey;

    @Schema(description = "原始文件名")
    private final String originalFilename;
}
