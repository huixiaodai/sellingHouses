package com.sellinghouses.salescontrol.module.building.service;

import com.sellinghouses.salescontrol.common.dto.IdDTO;
import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.module.building.dto.BuildingAddDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingPageQueryDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingStatusUpdateDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingUpdateDTO;
import com.sellinghouses.salescontrol.module.building.vo.BuildingVO;

public interface BuildingService {

    /**
     * 新增楼盘。
     *
     * @param addDTO 新增楼盘请求
     * @return 楼盘ID
     */
    Long add(BuildingAddDTO addDTO);

    /**
     * 修改楼盘。
     *
     * @param updateDTO 修改楼盘请求
     */
    void update(BuildingUpdateDTO updateDTO);

    /**
     * 逻辑删除楼盘。
     *
     * @param idDTO 主键请求
     */
    void delete(IdDTO idDTO);

    /**
     * 修改楼盘状态。
     *
     * @param updateDTO 修改状态请求
     */
    void updateStatus(BuildingStatusUpdateDTO updateDTO);

    /**
     * 查询楼盘详情。
     *
     * @param idDTO 主键请求
     * @return 楼盘详情
     */
    BuildingVO detail(IdDTO idDTO);

    /**
     * 分页查询楼盘。
     *
     * @param queryDTO 分页查询请求
     * @return 楼盘分页结果
     */
    PageResult<BuildingVO> page(BuildingPageQueryDTO queryDTO);
}
