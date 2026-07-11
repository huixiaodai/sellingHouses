package com.sellinghouses.salescontrol.common.enums;

import lombok.Getter;

@Getter
public enum UserStatusEnum {

    ENABLED(1, "启用"),
    DISABLED(2, "禁用");

    private final Integer code;

    private final String desc;

    UserStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
