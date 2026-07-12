package com.sellinghouses.salescontrol.module.building.controller;

import com.sellinghouses.salescontrol.common.dto.IdDTO;
import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.common.result.Result;
import com.sellinghouses.salescontrol.module.building.dto.BuildingUnitAddDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingUnitPageQueryDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingUnitStatusUpdateDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingUnitUpdateDTO;
import com.sellinghouses.salescontrol.module.building.service.BuildingUnitService;
import com.sellinghouses.salescontrol.module.building.vo.BuildingUnitVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/building-unit")
@Tag(name = "管理后台-楼栋管理")
public class BuildingUnitController {

    private final BuildingUnitService buildingUnitService;

    @PostMapping("/create")
    @Operation(summary = "新增楼栋")
    public Result<Long> create(@RequestBody @Valid BuildingUnitAddDTO addDTO) {
        return Result.success(buildingUnitService.add(addDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "修改楼栋")
    public Result<Void> update(@RequestBody @Valid BuildingUnitUpdateDTO updateDTO) {
        buildingUnitService.update(updateDTO);
        return Result.success();
    }

    @PostMapping("/delete")
    @Operation(summary = "删除楼栋")
    public Result<Void> delete(@RequestBody @Valid IdDTO idDTO) {
        buildingUnitService.delete(idDTO);
        return Result.success();
    }

    @PostMapping("/update-status")
    @Operation(summary = "修改楼栋状态")
    public Result<Void> updateStatus(@RequestBody @Valid BuildingUnitStatusUpdateDTO updateDTO) {
        buildingUnitService.updateStatus(updateDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "楼栋分页")
    public Result<PageResult<BuildingUnitVO>> page(@Valid BuildingUnitPageQueryDTO queryDTO) {
        return Result.success(buildingUnitService.page(queryDTO));
    }

    @GetMapping("/list-by-building")
    @Operation(summary = "根据楼盘查询楼栋")
    public Result<List<BuildingUnitVO>> listByBuilding(
            @Parameter(description = "楼盘ID", required = true)
            @RequestParam @NotNull(message = "楼盘ID不能为空") Long buildingId) {
        return Result.success(buildingUnitService.listByBuilding(buildingId));
    }
}
