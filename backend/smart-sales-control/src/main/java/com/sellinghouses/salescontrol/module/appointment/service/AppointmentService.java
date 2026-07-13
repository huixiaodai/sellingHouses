package com.sellinghouses.salescontrol.module.appointment.service;

import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.module.appointment.dto.AdminAppointmentPageQueryDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentCancelDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentCreateDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentPageQueryDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentStatusUpdateDTO;
import com.sellinghouses.salescontrol.module.appointment.vo.AppointmentVO;
import com.sellinghouses.salescontrol.module.user.vo.SalesUserVO;
import java.util.List;

public interface AppointmentService {

    /**
     * Create a viewing appointment.
     *
     * @param createDTO appointment creation request
     * @return appointment ID
     */
    Long create(AppointmentCreateDTO createDTO);

    /**
     * Cancel current customer's own appointment.
     *
     * @param cancelDTO appointment cancel request
     */
    void cancel(AppointmentCancelDTO cancelDTO);

    /**
     * Query current customer's own appointments.
     *
     * @param queryDTO page query request
     * @return appointment page
     */
    PageResult<AppointmentVO> userPage(AppointmentPageQueryDTO queryDTO);

    /**
     * Query admin-visible appointments.
     *
     * @param queryDTO admin page query request
     * @return appointment page
     */
    PageResult<AppointmentVO> adminPage(AdminAppointmentPageQueryDTO queryDTO);

    /**
     * Query appointments assigned to current sales user.
     *
     * @param queryDTO page query request
     * @return appointment page
     */
    PageResult<AppointmentVO> salesPage(AppointmentPageQueryDTO queryDTO);

    /**
     * Update appointment status and assigned sales user.
     *
     * @param updateDTO status update request
     */
    void updateStatus(AppointmentStatusUpdateDTO updateDTO);

    /**
     * Query active sales users for appointment assignment.
     *
     * @return sales user list
     */
    List<SalesUserVO> listSalesUsers();
}
