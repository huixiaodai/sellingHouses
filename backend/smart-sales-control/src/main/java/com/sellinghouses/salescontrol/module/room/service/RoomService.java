package com.sellinghouses.salescontrol.module.room.service;

import com.sellinghouses.salescontrol.common.dto.IdDTO;
import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.module.room.dto.RoomAddDTO;
import com.sellinghouses.salescontrol.module.room.dto.RoomPageQueryDTO;
import com.sellinghouses.salescontrol.module.room.dto.RoomPriceUpdateDTO;
import com.sellinghouses.salescontrol.module.room.dto.SaleControlQueryDTO;
import com.sellinghouses.salescontrol.module.room.dto.SaleControlSearchDTO;
import com.sellinghouses.salescontrol.module.room.dto.RoomStatusUpdateDTO;
import com.sellinghouses.salescontrol.module.room.dto.RoomUpdateDTO;
import com.sellinghouses.salescontrol.module.room.vo.EstateControlVO;
import com.sellinghouses.salescontrol.module.room.vo.FloorControlVO;
import com.sellinghouses.salescontrol.module.room.vo.RoomVO;
import com.sellinghouses.salescontrol.module.room.vo.UnitControlVO;
import java.util.List;

public interface RoomService {

    /**
     * 新增房源。
     *
     * @param addDTO 新增房源请求
     * @return 房源ID
     */
    Long add(RoomAddDTO addDTO);

    /**
     * 修改房源。
     *
     * @param updateDTO 修改房源请求
     */
    void update(RoomUpdateDTO updateDTO);

    /**
     * 逻辑删除房源。
     *
     * @param idDTO 主键请求
     */
    void delete(IdDTO idDTO);

    /**
     * 修改房源价格。
     *
     * @param updateDTO 修改价格请求
     */
    void updatePrice(RoomPriceUpdateDTO updateDTO);

    /**
     * 修改房源状态。
     *
     * @param updateDTO 修改状态请求
     */
    void updateStatus(RoomStatusUpdateDTO updateDTO);

    /**
     * 查询房源详情。
     *
     * @param idDTO 主键请求
     * @return 房源详情
     */
    RoomVO detail(IdDTO idDTO);

    /**
     * 分页查询房源。
     *
     * @param queryDTO 分页查询请求
     * @return 房源分页结果
     */
    PageResult<RoomVO> page(RoomPageQueryDTO queryDTO);

    /**
     * Query visible room detail for mobile clients.
     *
     * @param idDTO primary key request
     * @return visible room detail
     */
    RoomVO mobileDetail(IdDTO idDTO);

    /**
     * Query visible rooms for mobile clients.
     *
     * @param queryDTO page query request
     * @return visible room page result
     */
    PageResult<RoomVO> mobilePage(RoomPageQueryDTO queryDTO);

    /**
     * Query estate summaries for sale control.
     *
     * @return estate summaries with status counts
     */
    List<EstateControlVO> listEstateControls();

    /**
     * Query building unit summaries for sale control.
     *
     * @param buildingId building ID
     * @return building unit summaries with status counts
     */
    List<UnitControlVO> listUnitControls(Long buildingId);

    /**
     * Query floor summaries for sale control.
     *
     * @param unitId building unit ID
     * @return floor summaries with status counts
     */
    List<FloorControlVO> listFloorControls(Long unitId);

    /**
     * Query rooms for the sale control grid.
     *
     * @param queryDTO query request
     * @return room list
     */
    List<RoomVO> listSaleControlRooms(SaleControlQueryDTO queryDTO);

    /**
     * Search rooms by room number for sale control.
     *
     * @param searchDTO search request
     * @return matched rooms
     */
    List<RoomVO> searchSaleControlRooms(SaleControlSearchDTO searchDTO);
}
