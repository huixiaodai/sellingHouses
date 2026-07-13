package com.sellinghouses.salescontrol.common.enums;

import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum AppointmentStatusEnum {

    BOOKED(1, "已预约"),
    CANCELED(2, "已取消"),
    EXPIRED(3, "已过期");

    private final Integer code;

    private final String desc;

    AppointmentStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static void validate(Integer code) {
        for (AppointmentStatusEnum value : values()) {
            if (value.getCode().equals(code)) {
                return;
            }
        }
        throw new BusinessException(ErrorCode.BAD_REQUEST, "预约状态不合法");
    }
}
