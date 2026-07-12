package com.sellinghouses.salescontrol.module.building.service;

import com.sellinghouses.salescontrol.common.dto.IdDTO;
import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.module.building.dto.BuildingUnitAddDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingUnitPageQueryDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingUnitStatusUpdateDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingUnitUpdateDTO;
import com.sellinghouses.salescontrol.module.building.vo.BuildingUnitVO;
import java.util.List;

public interface BuildingUnitService {

    /**
     * 新增楼栋。
     *
     * @param addDTO 新增楼栋请求
     * @return 楼栋ID
     */
    Long add(BuildingUnitAddDTO addDTO);

    /**
     * 修改楼栋。
     *
     * @param updateDTO 修改楼栋请求
     */
    void update(BuildingUnitUpdateDTO updateDTO);

    /**
     * 逻辑删除楼栋。
     *
     * @param idDTO 主键请求
     */
    void delete(IdDTO idDTO);

    /**
     * 修改楼栋状态。
     *
     * @param updateDTO 修改状态请求
     */
    void updateStatus(BuildingUnitStatusUpdateDTO updateDTO);

    /**
     * 分页查询楼栋。
     *
     * @param queryDTO 分页查询请求
     * @return 楼栋分页结果
     */
    PageResult<BuildingUnitVO> page(BuildingUnitPageQueryDTO queryDTO);

    /**
     * 根据楼盘查询楼栋列表。
     *
     * @param buildingId 楼盘ID
     * @return 楼栋列表
     */
    List<BuildingUnitVO> listByBuilding(Long buildingId);
}
