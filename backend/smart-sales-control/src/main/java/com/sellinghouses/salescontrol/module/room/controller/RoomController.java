package com.sellinghouses.salescontrol.module.room.controller;

import com.sellinghouses.salescontrol.common.dto.IdDTO;
import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.common.result.Result;
import com.sellinghouses.salescontrol.module.room.dto.RoomAddDTO;
import com.sellinghouses.salescontrol.module.room.dto.RoomPageQueryDTO;
import com.sellinghouses.salescontrol.module.room.dto.RoomPriceUpdateDTO;
import com.sellinghouses.salescontrol.module.room.dto.RoomStatusUpdateDTO;
import com.sellinghouses.salescontrol.module.room.dto.RoomUpdateDTO;
import com.sellinghouses.salescontrol.module.room.service.RoomService;
import com.sellinghouses.salescontrol.module.room.vo.RoomVO;
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
@RequestMapping("/api/admin/room")
@Tag(name = "管理后台-房源管理")
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/create")
    @Operation(summary = "新增房源")
    public Result<Long> create(@RequestBody @Valid RoomAddDTO addDTO) {
        return Result.success(roomService.add(addDTO));
    }

    @PostMapping("/update")
    @Operation(summary = "修改房源")
    public Result<Void> update(@RequestBody @Valid RoomUpdateDTO updateDTO) {
        roomService.update(updateDTO);
        return Result.success();
    }

    @PostMapping("/delete")
    @Operation(summary = "删除房源")
    public Result<Void> delete(@RequestBody @Valid IdDTO idDTO) {
        roomService.delete(idDTO);
        return Result.success();
    }

    @PostMapping("/update-price")
    @Operation(summary = "修改房源价格")
    public Result<Void> updatePrice(@RequestBody @Valid RoomPriceUpdateDTO updateDTO) {
        roomService.updatePrice(updateDTO);
        return Result.success();
    }

    @PostMapping("/update-status")
    @Operation(summary = "修改房源状态")
    public Result<Void> updateStatus(@RequestBody @Valid RoomStatusUpdateDTO updateDTO) {
        roomService.updateStatus(updateDTO);
        return Result.success();
    }

    @GetMapping("/detail")
    @Operation(summary = "房源详情")
    public Result<RoomVO> detail(@Parameter(description = "房源ID", required = true) @Valid IdDTO idDTO) {
        return Result.success(roomService.detail(idDTO));
    }

    @GetMapping("/page")
    @Operation(summary = "房源分页")
    public Result<PageResult<RoomVO>> page(@Valid RoomPageQueryDTO queryDTO) {
        return Result.success(roomService.page(queryDTO));
    }
}
