package com.sellinghouses.salescontrol.common.enums;

import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum NoticeTargetRoleEnum {

    ALL("ALL", "全部"),
    ADMIN("ADMIN", "超级管理员"),
    SALES("SALES", "销售"),
    CUSTOMER("CUSTOMER", "购房用户");

    private final String code;

    private final String desc;

    NoticeTargetRoleEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static void validate(String code) {
        for (NoticeTargetRoleEnum value : values()) {
            if (value.getCode().equals(code)) {
                return;
            }
        }
        throw new BusinessException(ErrorCode.BAD_REQUEST, "公告可见角色不合法");
    }
}
