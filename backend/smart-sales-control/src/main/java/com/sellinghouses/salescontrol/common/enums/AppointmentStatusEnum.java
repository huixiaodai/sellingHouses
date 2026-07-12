package com.sellinghouses.salescontrol.common.enums;

import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum AppointmentStatusEnum {

    PENDING(1, "待确认"),
    CONFIRMED(2, "已确认"),
    VISITED(3, "已看房"),
    CANCELED(4, "已取消"),
    EXPIRED(5, "已过期");

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
