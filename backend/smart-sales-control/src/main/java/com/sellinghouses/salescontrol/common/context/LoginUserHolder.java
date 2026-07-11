package com.sellinghouses.salescontrol.common.context;

import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;

public final class LoginUserHolder {

    private static final ThreadLocal<LoginUserContext> LOGIN_USER = new ThreadLocal<>();

    private LoginUserHolder() {
    }

    public static void set(LoginUserContext loginUserContext) {
        LOGIN_USER.set(loginUserContext);
    }

    public static LoginUserContext getRequired() {
        LoginUserContext loginUserContext = LOGIN_USER.get();
        if (loginUserContext == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return loginUserContext;
    }

    public static void clear() {
        LOGIN_USER.remove();
    }
}
