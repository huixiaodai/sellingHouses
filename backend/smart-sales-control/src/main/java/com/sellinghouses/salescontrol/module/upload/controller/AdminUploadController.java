package com.sellinghouses.salescontrol.module.upload.controller;

import com.sellinghouses.salescontrol.common.result.Result;
import com.sellinghouses.salescontrol.module.upload.service.UploadService;
import com.sellinghouses.salescontrol.module.upload.vo.UploadImageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/upload")
@Tag(name = "管理后台-文件上传")
public class AdminUploadController {

    private final UploadService uploadService;

    @PostMapping("/image")
    @Operation(summary = "上传图片")
    public Result<UploadImageVO> uploadImage(@RequestParam("file") MultipartFile file) {
        return Result.success(uploadService.uploadImage(file));
    }
}
