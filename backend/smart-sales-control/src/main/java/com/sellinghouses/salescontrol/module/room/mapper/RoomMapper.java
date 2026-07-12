package com.sellinghouses.salescontrol.module.room.mapper;

import com.sellinghouses.salescontrol.module.room.dto.RoomPageQueryDTO;
import com.sellinghouses.salescontrol.module.room.entity.Room;
import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoomMapper {

    int insert(Room room);

    int updateById(Room room);

    int updatePrice(@Param("id") Long id, @Param("price") BigDecimal price, @Param("updateUser") Long updateUser);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updateUser") Long updateUser);

    int logicalDelete(@Param("id") Long id, @Param("updateUser") Long updateUser);

    Room selectById(@Param("id") Long id);

    int countByRoomNo(@Param("unitId") Long unitId, @Param("roomNo") String roomNo, @Param("excludeId") Long excludeId);

    int countByUnitId(@Param("unitId") Long unitId);

    List<Room> selectPage(RoomPageQueryDTO queryDTO);
}
