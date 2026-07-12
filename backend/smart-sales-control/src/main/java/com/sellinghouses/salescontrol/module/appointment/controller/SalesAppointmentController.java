package com.sellinghouses.salescontrol.module.appointment.controller;

import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.common.result.Result;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentPageQueryDTO;
import com.sellinghouses.salescontrol.module.appointment.service.AppointmentService;
import com.sellinghouses.salescontrol.module.appointment.vo.AppointmentVO;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/sales/appointment")
@Tag(name = "销售端-预约管理")
public class SalesAppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/page")
    @Operation(summary = "销售预约分页")
    public Result<PageResult<AppointmentVO>> page(@Valid AppointmentPageQueryDTO queryDTO) {
        return Result.success(appointmentService.salesPage(queryDTO));
    }
}
