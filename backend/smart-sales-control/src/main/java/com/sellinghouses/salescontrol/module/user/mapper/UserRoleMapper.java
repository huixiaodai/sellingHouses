package com.sellinghouses.salescontrol.module.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRoleMapper {

    int insertByRoleCode(@Param("userId") Long userId,
                         @Param("roleCode") String roleCode,
                         @Param("operatorId") Long operatorId);
}
