package com.sellinghouses.salescontrol.module.follow.controller;

import com.sellinghouses.salescontrol.common.result.Result;
import com.sellinghouses.salescontrol.module.follow.dto.CustomerFollowCreateDTO;
import com.sellinghouses.salescontrol.module.follow.service.CustomerFollowService;
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
@RequestMapping("/api/sales/customer-follow")
@Tag(name = "销售端-客户跟进")
public class CustomerFollowController {

    private final CustomerFollowService customerFollowService;

    @PostMapping("/create")
    @Operation(summary = "新增客户跟进")
    public Result<Long> create(@RequestBody @Valid CustomerFollowCreateDTO createDTO) {
        return Result.success(customerFollowService.create(createDTO));
    }
}
