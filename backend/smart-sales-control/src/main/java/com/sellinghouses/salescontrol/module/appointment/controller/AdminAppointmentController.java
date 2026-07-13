package com.sellinghouses.salescontrol.module.appointment.controller;

import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.common.result.Result;
import com.sellinghouses.salescontrol.module.appointment.dto.AdminAppointmentPageQueryDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentStatusUpdateDTO;
import com.sellinghouses.salescontrol.module.appointment.service.AppointmentService;
import com.sellinghouses.salescontrol.module.appointment.vo.AppointmentVO;
import com.sellinghouses.salescontrol.module.user.vo.SalesUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
@Tag(name = "Admin appointment management")
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/page")
    @Operation(summary = "Admin appointment page")
    public Result<PageResult<AppointmentVO>> page(@Valid AdminAppointmentPageQueryDTO queryDTO) {
        return Result.success(appointmentService.adminPage(queryDTO));
    }

    @PostMapping("/update-status")
    @Operation(summary = "Update appointment status")
    public Result<Void> updateStatus(@RequestBody @Valid AppointmentStatusUpdateDTO updateDTO) {
        appointmentService.updateStatus(updateDTO);
        return Result.success();
    }

    @GetMapping("/sales-list")
    @Operation(summary = "Sales user list")
    public Result<List<SalesUserVO>> salesList() {
        return Result.success(appointmentService.listSalesUsers());
    }
}
