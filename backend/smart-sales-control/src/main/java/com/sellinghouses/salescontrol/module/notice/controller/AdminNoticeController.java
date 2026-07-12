package com.sellinghouses.salescontrol.module.notice.controller;

import com.sellinghouses.salescontrol.common.dto.IdDTO;
import com.sellinghouses.salescontrol.common.result.Result;
import com.sellinghouses.salescontrol.module.notice.dto.NoticeCreateDTO;
import com.sellinghouses.salescontrol.module.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notice")
@Tag(name = "管理后台-公告管理")
public class AdminNoticeController {

    private final NoticeService noticeService;

    @PostMapping("/create")
    @Operation(summary = "发布公告")
    public Result<Long> create(@RequestBody @Valid NoticeCreateDTO createDTO) {
        return Result.success(noticeService.create(createDTO));
    }

    @PostMapping("/delete")
    @Operation(summary = "删除公告")
    public Result<Void> delete(@RequestBody @Valid IdDTO idDTO) {
        noticeService.delete(idDTO);
        return Result.success();
    }
}
