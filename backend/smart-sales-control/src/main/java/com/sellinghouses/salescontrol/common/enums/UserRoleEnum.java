package com.sellinghouses.salescontrol.common.enums;

import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum UserRoleEnum {

    ADMIN("ADMIN", "超级管理员", "/dashboard"),
    SALES("SALES", "销售", "/pages/estate/index"),
    CUSTOMER("CUSTOMER", "购房用户", "/pages/home/home");

    private final String code;

    private final String desc;

    private final String homePath;

    UserRoleEnum(String code, String desc, String homePath) {
        this.code = code;
        this.desc = desc;
        this.homePath = homePath;
    }

    public static UserRoleEnum fromCode(String code) {
        for (UserRoleEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new BusinessException(ErrorCode.BAD_REQUEST, "用户角色不合法");
    }
}
