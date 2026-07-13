package com.sellinghouses.salescontrol.module.appointment.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentQueryDTO {

    private Long id;

    private Long userId;

    private String customerName;

    private Long salesUserId;

    private String salesName;

    private Long buildingId;

    private String buildingName;

    private Long unitId;

    private String unitName;

    private Long roomId;

    private String roomNo;

    private LocalDateTime appointmentTime;

    private String contactName;

    private String contactPhone;

    private String remark;

    private Integer status;

    private String cancelReason;

    private LocalDateTime createTime;
}
