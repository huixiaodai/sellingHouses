package com.sellinghouses.salescontrol.module.appointment.controller;

import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.common.result.Result;
import com.sellinghouses.salescontrol.module.appointment.dto.AdminAppointmentPageQueryDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentStatusUpdateDTO;
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
@RequestMapping("/api/admin/appointment")
@Tag(name = "管理后台-预约管理")
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/page")
    @Operation(summary = "管理员预约分页")
    public Result<PageResult<AppointmentVO>> page(@Valid AdminAppointmentPageQueryDTO queryDTO) {
        return Result.success(appointmentService.adminPage(queryDTO));
    }

    @PostMapping("/update-status")
    @Operation(summary = "修改预约状态")
    public Result<Void> updateStatus(@RequestBody @Valid AppointmentStatusUpdateDTO updateDTO) {
        appointmentService.updateStatus(updateDTO);
        return Result.success();
    }
}
