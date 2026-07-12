package com.sellinghouses.salescontrol.module.follow.service;

import com.sellinghouses.salescontrol.module.follow.dto.CustomerFollowCreateDTO;

public interface CustomerFollowService {

    /**
     * 销售创建客户跟进记录。
     *
     * @param createDTO 跟进创建请求
     * @return 跟进记录ID
     */
    Long create(CustomerFollowCreateDTO createDTO);
}
