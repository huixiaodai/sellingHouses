package com.sellinghouses.salescontrol.module.user.mapper;

import com.sellinghouses.salescontrol.module.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    User selectById(@Param("id") Long id);

    User selectByUsername(@Param("username") String username);

    User selectByPhone(@Param("phone") String phone);

    User selectByWxOpenid(@Param("wxOpenid") String wxOpenid);

    int insert(User user);

    int updateLastLoginTime(@Param("id") Long id);
}
