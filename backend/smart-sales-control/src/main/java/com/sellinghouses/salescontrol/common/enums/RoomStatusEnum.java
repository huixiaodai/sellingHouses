package com.sellinghouses.salescontrol.common.enums;

import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum RoomStatusEnum {

    AVAILABLE(0, "待售"),
    SOLD(1, "已售"),
    LOCKED(2, "锁定");

    private final Integer code;

    private final String desc;

    RoomStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static void validate(Integer code) {
        for (RoomStatusEnum value : values()) {
            if (value.getCode().equals(code)) {
                return;
            }
        }
        throw new BusinessException(ErrorCode.BAD_REQUEST, "房源状态不合法");
    }
}
