package com.sellinghouses.salescontrol.module.auth.controller;

import com.sellinghouses.salescontrol.common.result.Result;
import com.sellinghouses.salescontrol.module.auth.dto.LoginRequest;
import com.sellinghouses.salescontrol.module.auth.dto.RegisterRequest;
import com.sellinghouses.salescontrol.module.auth.service.AuthService;
import com.sellinghouses.salescontrol.module.auth.vo.CurrentUserVO;
import com.sellinghouses.salescontrol.module.auth.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "登录模块")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<LoginVO> login(@RequestBody @Valid LoginRequest request) {
        return Result.success(authService.login(request));
    }

    @PostMapping("/register")
    @Operation(summary = "购房用户注册")
    public Result<Void> register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return Result.success();
    }

    @GetMapping("/current")
    @Operation(summary = "当前登录用户")
    public Result<CurrentUserVO> currentUser() {
        return Result.success(authService.currentUser());
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public Result<Void> logout() {
        authService.logout();
        return Result.success();
    }
}
