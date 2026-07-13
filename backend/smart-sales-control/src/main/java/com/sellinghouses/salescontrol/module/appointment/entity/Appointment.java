package com.sellinghouses.salescontrol.module.appointment.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Appointment {

    private Long id;

    private Long userId;

    private Long salesUserId;

    private Long roomId;

    private LocalDateTime appointmentTime;

    private String contactName;

    private String contactPhone;

    private String remark;

    private Integer status;

    private String cancelReason;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}
