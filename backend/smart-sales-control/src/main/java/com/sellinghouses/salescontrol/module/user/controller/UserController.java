package com.sellinghouses.salescontrol.module.user.controller;

import com.sellinghouses.salescontrol.common.result.Result;
import com.sellinghouses.salescontrol.module.user.dto.SalesUserCreateDTO;
import com.sellinghouses.salescontrol.module.user.service.UserService;
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
@RequestMapping("/api/admin/user")
@Tag(name = "Admin-User")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create sales user")
    public Result<Long> createSalesUser(@RequestBody @Valid SalesUserCreateDTO createDTO) {
        return Result.success(userService.createSalesUser(createDTO));
    }
}
