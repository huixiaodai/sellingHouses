package com.sellinghouses.salescontrol.module.room.controller;

import com.sellinghouses.salescontrol.common.result.Result;
import com.sellinghouses.salescontrol.module.room.dto.SaleControlQueryDTO;
import com.sellinghouses.salescontrol.module.room.dto.SaleControlSearchDTO;
import com.sellinghouses.salescontrol.module.room.service.RoomService;
import com.sellinghouses.salescontrol.module.room.vo.EstateControlVO;
import com.sellinghouses.salescontrol.module.room.vo.FloorControlVO;
import com.sellinghouses.salescontrol.module.room.vo.RoomVO;
import com.sellinghouses.salescontrol.module.room.vo.UnitControlVO;
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
@RequestMapping("/api")
@Tag(name = "Mobile-Sale Control")
public class SaleControlController {

    private final RoomService roomService;

    @GetMapping("/estate/list")
    @Operation(summary = "Sale control estate list")
    public Result<List<EstateControlVO>> estateList() {
        return Result.success(roomService.listEstateControls());
    }

    @GetMapping("/building/list")
    @Operation(summary = "Sale control building unit list")
    public Result<List<UnitControlVO>> buildingList(
            @Parameter(description = "Building ID", required = true)
            @RequestParam("estateId") @NotNull(message = "楼盘ID不能为空") Long estateId) {
        return Result.success(roomService.listUnitControls(estateId));
    }

    @GetMapping("/floor/list")
    @Operation(summary = "Sale control floor list")
    public Result<List<FloorControlVO>> floorList(
            @Parameter(description = "Building unit ID", required = true)
            @RequestParam @NotNull(message = "楼栋ID不能为空") Long unitId) {
        return Result.success(roomService.listFloorControls(unitId));
    }

    @GetMapping("/room/list")
    @Operation(summary = "Sale control room list")
    public Result<List<RoomVO>> roomList(@Valid SaleControlQueryDTO queryDTO) {
        return Result.success(roomService.listSaleControlRooms(queryDTO));
    }

    @GetMapping("/room/search")
    @Operation(summary = "Sale control room search")
    public Result<List<RoomVO>> roomSearch(@Valid SaleControlSearchDTO searchDTO) {
        return Result.success(roomService.searchSaleControlRooms(searchDTO));
    }

    @PostMapping("/room/filter")
    @Operation(summary = "Sale control room filter")
    public Result<List<RoomVO>> roomFilter(@RequestBody @Valid SaleControlQueryDTO queryDTO) {
        return Result.success(roomService.listSaleControlRooms(queryDTO));
    }
}
