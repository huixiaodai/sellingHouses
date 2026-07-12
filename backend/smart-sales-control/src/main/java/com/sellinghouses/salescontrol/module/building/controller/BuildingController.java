package com.sellinghouses.salescontrol.module.building.controller;

import com.sellinghouses.salescontrol.common.dto.IdDTO;
import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.common.result.Result;
import com.sellinghouses.salescontrol.module.building.dto.BuildingAddDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingPageQueryDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingStatusUpdateDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingUpdateDTO;
import com.sellinghouses.salescontrol.module.building.service.BuildingService;
import com.sellinghouses.salescontrol.module.building.vo.BuildingVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/building")
@Tag(name = "管理后台-楼盘管理")
public class BuildingController {

    private final BuildingService buildingService;

    @PostMapping("/create")
    @Operation(summary = "新增楼盘")
    public Result<Long> create(@RequestBody @Valid BuildingAddDTO addDTO) {
        return Result.success(buildingService.add(addDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "修改楼盘")
    public Result<Void> update(@RequestBody @Valid BuildingUpdateDTO updateDTO) {
        buildingService.update(updateDTO);
        return Result.success();
    }

    @PostMapping("/delete")
    @Operation(summary = "删除楼盘")
    public Result<Void> delete(@RequestBody @Valid IdDTO idDTO) {
        buildingService.delete(idDTO);
        return Result.success();
    }

    @PostMapping("/update-status")
    @Operation(summary = "修改楼盘状态")
    public Result<Void> updateStatus(@RequestBody @Valid BuildingStatusUpdateDTO updateDTO) {
        buildingService.updateStatus(updateDTO);
        return Result.success();
    }

    @GetMapping("/detail")
    @Operation(summary = "楼盘详情")
    public Result<BuildingVO> detail(@Parameter(description = "楼盘ID", required = true) @Valid IdDTO idDTO) {
        return Result.success(buildingService.detail(idDTO));
    }

    @GetMapping("/page")
    @Operation(summary = "楼盘分页")
    public Result<PageResult<BuildingVO>> page(@Valid BuildingPageQueryDTO queryDTO) {
        return Result.success(buildingService.page(queryDTO));
    }
}
