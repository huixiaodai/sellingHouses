package com.sellinghouses.salescontrol.common.context;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUserContext {

    private final Long userId;

    private final String username;

    private final String realName;

    private final String roleCode;
}
