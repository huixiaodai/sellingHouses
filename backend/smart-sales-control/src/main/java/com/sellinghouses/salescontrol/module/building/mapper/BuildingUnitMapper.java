package com.sellinghouses.salescontrol.module.building.mapper;

import com.sellinghouses.salescontrol.module.building.dto.BuildingUnitPageQueryDTO;
import com.sellinghouses.salescontrol.module.building.entity.BuildingUnit;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BuildingUnitMapper {

    int insert(BuildingUnit buildingUnit);

    int updateById(BuildingUnit buildingUnit);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updateUser") Long updateUser);

    int logicalDelete(@Param("id") Long id, @Param("updateUser") Long updateUser);

    BuildingUnit selectById(@Param("id") Long id);

    int countByName(@Param("buildingId") Long buildingId, @Param("name") String name, @Param("excludeId") Long excludeId);

    int countByBuildingId(@Param("buildingId") Long buildingId);

    List<BuildingUnit> selectPage(BuildingUnitPageQueryDTO queryDTO);

    List<BuildingUnit> selectByBuildingId(@Param("buildingId") Long buildingId);
}
