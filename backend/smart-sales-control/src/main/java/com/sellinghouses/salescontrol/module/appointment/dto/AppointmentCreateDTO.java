package com.sellinghouses.salescontrol.module.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Appointment create request")
public class AppointmentCreateDTO {

    @NotNull(message = "Room ID is required")
    @Schema(description = "Room ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long roomId;

    @NotNull(message = "Appointment time is required")
    @Schema(description = "Appointment time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime appointmentTime;

    @NotBlank(message = "Contact name is required")
    @Schema(description = "Contact name", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactName;

    @NotBlank(message = "Contact phone is required")
    @Schema(description = "Contact phone", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactPhone;

    @Schema(description = "Remark")
    private String remark;
}
