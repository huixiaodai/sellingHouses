package com.sellinghouses.salescontrol.module.follow.mapper;

import com.sellinghouses.salescontrol.module.follow.entity.CustomerFollow;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerFollowMapper {

    int insert(CustomerFollow customerFollow);
}
