package com.sellinghouses.salescontrol.module.building.mapper;

import com.sellinghouses.salescontrol.module.building.dto.BuildingPageQueryDTO;
import com.sellinghouses.salescontrol.module.building.entity.Building;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BuildingMapper {

    int insert(Building building);

    int updateById(Building building);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updateUser") Long updateUser);

    int logicalDelete(@Param("id") Long id, @Param("updateUser") Long updateUser);

    Building selectById(@Param("id") Long id);

    Building selectVisibleById(@Param("id") Long id);

    int countByName(@Param("name") String name, @Param("excludeId") Long excludeId);

    List<Building> selectPage(BuildingPageQueryDTO queryDTO);

    List<Building> selectVisiblePage(BuildingPageQueryDTO queryDTO);
}
