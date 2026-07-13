package com.sellinghouses.salescontrol.module.room.controller;

import com.sellinghouses.salescontrol.common.dto.IdDTO;
import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.common.result.Result;
import com.sellinghouses.salescontrol.module.room.dto.RoomPageQueryDTO;
import com.sellinghouses.salescontrol.module.room.service.RoomService;
import com.sellinghouses.salescontrol.module.room.vo.RoomVO;
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
@RequestMapping("/api/room")
@Tag(name = "Mobile-Room Query")
public class MobileRoomController {

    private final RoomService roomService;

    @GetMapping("/detail")
    @Operation(summary = "Mobile room detail")
    public Result<RoomVO> detail(
            @Parameter(description = "Room ID", required = true) @Valid IdDTO idDTO) {
        return Result.success(roomService.mobileDetail(idDTO));
    }

    @GetMapping("/page")
    @Operation(summary = "Mobile room page")
    public Result<PageResult<RoomVO>> page(@Valid RoomPageQueryDTO queryDTO) {
        return Result.success(roomService.mobilePage(queryDTO));
    }
}
