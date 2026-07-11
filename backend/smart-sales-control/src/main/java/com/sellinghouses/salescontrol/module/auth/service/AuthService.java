package com.sellinghouses.salescontrol.module.auth.service;

import com.sellinghouses.salescontrol.module.auth.dto.LoginRequest;
import com.sellinghouses.salescontrol.module.auth.dto.RegisterRequest;
import com.sellinghouses.salescontrol.module.auth.vo.CurrentUserVO;
import com.sellinghouses.salescontrol.module.auth.vo.LoginVO;

public interface AuthService {

    /**
     * 用户登录并返回 JWT 与角色首页信息。
     *
     * @param request 登录请求
     * @return 登录响应
     */
    LoginVO login(LoginRequest request);

    /**
     * 注册购房用户账号。
     *
     * @param request 注册请求
     */
    void register(RegisterRequest request);

    /**
     * 查询当前登录用户信息。
     *
     * @return 当前登录用户信息
     */
    CurrentUserVO currentUser();

    /**
     * 退出登录并记录业务入口。
     */
    void logout();
}
