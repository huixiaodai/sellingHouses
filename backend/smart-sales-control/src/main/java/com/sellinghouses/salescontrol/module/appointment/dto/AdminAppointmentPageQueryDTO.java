package com.sellinghouses.salescontrol.module.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Admin appointment page query request")
public class AdminAppointmentPageQueryDTO {

    @Min(value = 1, message = "pageNo must be greater than 0")
    @Schema(description = "Page number")
    private Integer pageNo = 1;

    @Min(value = 1, message = "pageSize must be greater than 0")
    @Schema(description = "Page size")
    private Integer pageSize = 10;

    @Schema(description = "Building name")
    private String buildingName;

    @Schema(description = "Building ID")
    private Long buildingId;

    @Schema(description = "Building ID, compatible with old estateId parameter")
    private Long estateId;

    @Schema(description = "Sales user ID")
    private Long salesUserId;

    @Schema(description = "Sales name")
    private String salesName;

    @Schema(description = "Contact name")
    private String contactName;

    @Schema(description = "Contact phone")
    private String contactPhone;

    @Schema(description = "Appointment status")
    private Integer status;
}
