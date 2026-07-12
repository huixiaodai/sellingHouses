package com.sellinghouses.salescontrol.module.appointment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Appointment response")
public class AppointmentVO {

    @Schema(description = "Appointment ID")
    private final Long id;

    @Schema(description = "Customer user ID")
    private final Long userId;

    @Schema(description = "Sales user ID")
    private final Long salesUserId;

    @Schema(description = "Sales name")
    private final String salesName;

    @Schema(description = "Building ID")
    private final Long estateId;

    @Schema(description = "Building ID")
    private final Long buildingId;

    @Schema(description = "Building name")
    private final String buildingName;

    @Schema(description = "Room ID")
    private final Long roomId;

    @Schema(description = "Room number")
    private final String roomNo;

    @Schema(description = "Appointment time")
    private final LocalDateTime appointmentTime;

    @Schema(description = "Contact name")
    private final String contactName;

    @Schema(description = "Contact phone")
    private final String contactPhone;

    @Schema(description = "Remark")
    private final String remark;

    @Schema(description = "Appointment status")
    private final Integer status;

    @Schema(description = "Cancel reason")
    private final String cancelReason;

    @Schema(description = "Create time")
    private final LocalDateTime createTime;
}
