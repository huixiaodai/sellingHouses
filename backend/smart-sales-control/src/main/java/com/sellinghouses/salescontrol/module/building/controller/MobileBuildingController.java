package com.sellinghouses.salescontrol.module.building.controller;

import com.sellinghouses.salescontrol.common.dto.IdDTO;
import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.common.result.Result;
import com.sellinghouses.salescontrol.module.building.dto.BuildingPageQueryDTO;
import com.sellinghouses.salescontrol.module.building.service.BuildingService;
import com.sellinghouses.salescontrol.module.building.vo.BuildingVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/building")
@Tag(name = "Mobile-Building Query")
public class MobileBuildingController {

    private final BuildingService buildingService;

    @GetMapping("/detail")
    @Operation(summary = "Mobile building detail")
    public Result<BuildingVO> detail(
            @Parameter(description = "Building ID", required = true) @Valid IdDTO idDTO) {
        return Result.success(buildingService.mobileDetail(idDTO));
    }

    @GetMapping("/page")
    @Operation(summary = "Mobile building page")
    public Result<PageResult<BuildingVO>> page(@Valid BuildingPageQueryDTO queryDTO) {
        return Result.success(buildingService.mobilePage(queryDTO));
    }
}
