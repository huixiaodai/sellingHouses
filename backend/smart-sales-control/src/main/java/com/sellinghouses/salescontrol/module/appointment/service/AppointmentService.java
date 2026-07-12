package com.sellinghouses.salescontrol.module.appointment.service;

import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.module.appointment.dto.AdminAppointmentPageQueryDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentCancelDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentCreateDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentPageQueryDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentStatusUpdateDTO;
import com.sellinghouses.salescontrol.module.appointment.vo.AppointmentVO;

public interface AppointmentService {

    /**
     * 创建看房预约。
     *
     * @param createDTO 预约创建请求
     * @return 预约ID
     */
    Long create(AppointmentCreateDTO createDTO);

    /**
     * 取消当前用户自己的预约。
     *
     * @param cancelDTO 取消预约请求
     */
    void cancel(AppointmentCancelDTO cancelDTO);

    /**
     * 查询当前购房用户自己的预约。
     *
     * @param queryDTO 分页查询请求
     * @return 预约分页
     */
    PageResult<AppointmentVO> userPage(AppointmentPageQueryDTO queryDTO);

    /**
     * 查询管理员可见的预约。
     *
     * @param queryDTO 管理员分页查询请求
     * @return 预约分页
     */
    PageResult<AppointmentVO> adminPage(AdminAppointmentPageQueryDTO queryDTO);

    /**
     * 查询当前销售分配到的预约。
     *
     * @param queryDTO 分页查询请求
     * @return 预约分页
     */
    PageResult<AppointmentVO> salesPage(AppointmentPageQueryDTO queryDTO);

    /**
     * 管理员修改预约状态和销售分配。
     *
     * @param updateDTO 状态修改请求
     */
    void updateStatus(AppointmentStatusUpdateDTO updateDTO);
}
