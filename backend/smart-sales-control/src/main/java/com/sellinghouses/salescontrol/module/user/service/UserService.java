package com.sellinghouses.salescontrol.module.user.service;

import com.sellinghouses.salescontrol.module.user.dto.SalesUserCreateDTO;

public interface UserService {

    /**
     * Create a sales user by admin.
     *
     * @param createDTO create sales user request
     * @return created user id
     */
    Long createSalesUser(SalesUserCreateDTO createDTO);
}
