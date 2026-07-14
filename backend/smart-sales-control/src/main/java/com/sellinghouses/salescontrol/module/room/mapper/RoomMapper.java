package com.sellinghouses.salescontrol.module.room.mapper;

import com.sellinghouses.salescontrol.module.room.dto.RoomPageQueryDTO;
import com.sellinghouses.salescontrol.module.room.dto.RoomQueryDTO;
import com.sellinghouses.salescontrol.module.room.dto.SaleControlQueryDTO;
import com.sellinghouses.salescontrol.module.room.entity.Room;
import com.sellinghouses.salescontrol.module.room.vo.EstateControlVO;
import com.sellinghouses.salescontrol.module.room.vo.FloorControlVO;
import com.sellinghouses.salescontrol.module.room.vo.UnitControlVO;
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

    RoomQueryDTO selectDetailById(@Param("id") Long id);

    RoomQueryDTO selectVisibleDetailById(@Param("id") Long id);

    int countByRoomNo(@Param("unitId") Long unitId, @Param("roomNo") String roomNo, @Param("excludeId") Long excludeId);

    int countByUnitId(@Param("unitId") Long unitId);

    List<RoomQueryDTO> selectPage(RoomPageQueryDTO queryDTO);

    List<RoomQueryDTO> selectVisiblePage(RoomPageQueryDTO queryDTO);

    List<EstateControlVO> selectEstateControlList();

    List<UnitControlVO> selectUnitControlList(@Param("buildingId") Long buildingId);

    List<FloorControlVO> selectFloorControlList(@Param("unitId") Long unitId);

    List<RoomQueryDTO> selectSaleControlRooms(SaleControlQueryDTO queryDTO);
}
