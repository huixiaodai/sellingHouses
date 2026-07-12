package com.sellinghouses.salescontrol.module.appointment.controller;

import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.common.result.Result;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentCancelDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentCreateDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentPageQueryDTO;
import com.sellinghouses.salescontrol.module.appointment.service.AppointmentService;
import com.sellinghouses.salescontrol.module.appointment.vo.AppointmentVO;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/user/appointment")
@Tag(name = "用户端-预约管理")
public class UserAppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/create")
    @Operation(summary = "预约看房")
    public Result<Long> create(@RequestBody @Valid AppointmentCreateDTO createDTO) {
        return Result.success(appointmentService.create(createDTO));
    }

    @PostMapping("/cancel")
    @Operation(summary = "取消预约")
    public Result<Void> cancel(@RequestBody @Valid AppointmentCancelDTO cancelDTO) {
        appointmentService.cancel(cancelDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "我的预约")
    public Result<PageResult<AppointmentVO>> page(@Valid AppointmentPageQueryDTO queryDTO) {
        return Result.success(appointmentService.userPage(queryDTO));
    }
}
