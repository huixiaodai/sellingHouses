package com.sellinghouses.salescontrol.common.enums;

import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum BuildingStatusEnum {

    DISABLED(0, "禁用"),
    ENABLED(1, "启用");

    private final Integer code;

    private final String desc;

    BuildingStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static void validate(Integer code) {
        for (BuildingStatusEnum value : values()) {
            if (value.getCode().equals(code)) {
                return;
            }
        }
        throw new BusinessException(ErrorCode.BAD_REQUEST, "状态不合法");
    }
}
