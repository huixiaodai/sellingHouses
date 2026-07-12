package com.sellinghouses.salescontrol.module.notice.controller;

import com.sellinghouses.salescontrol.common.dto.IdDTO;
import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.common.result.Result;
import com.sellinghouses.salescontrol.module.notice.dto.NoticePageQueryDTO;
import com.sellinghouses.salescontrol.module.notice.service.NoticeService;
import com.sellinghouses.salescontrol.module.notice.vo.NoticeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
@Tag(name = "公告查询")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/page")
    @Operation(summary = "公告分页")
    public Result<PageResult<NoticeVO>> page(@Valid NoticePageQueryDTO queryDTO) {
        return Result.success(noticeService.page(queryDTO));
    }

    @GetMapping("/detail")
    @Operation(summary = "公告详情")
    public Result<NoticeVO> detail(@Parameter(description = "公告ID", required = true) @Valid IdDTO idDTO) {
        return Result.success(noticeService.detail(idDTO));
    }
}
