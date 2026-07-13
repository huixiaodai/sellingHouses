package com.sellinghouses.salescontrol.module.appointment.mapper;

import com.sellinghouses.salescontrol.module.appointment.dto.AdminAppointmentPageQueryDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentPageQueryDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentQueryDTO;
import com.sellinghouses.salescontrol.module.appointment.entity.Appointment;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AppointmentMapper {

    int insert(Appointment appointment);

    int cancel(@Param("id") Long id, @Param("cancelReason") String cancelReason, @Param("updateUser") Long updateUser);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status,
                     @Param("salesUserId") Long salesUserId, @Param("updateUser") Long updateUser);

    Appointment selectById(@Param("id") Long id);

    List<AppointmentQueryDTO> selectUserPage(@Param("query") AppointmentPageQueryDTO queryDTO, @Param("userId") Long userId);

    List<AppointmentQueryDTO> selectSalesPage(@Param("query") AppointmentPageQueryDTO queryDTO, @Param("salesUserId") Long salesUserId);

    List<AppointmentQueryDTO> selectAdminPage(AdminAppointmentPageQueryDTO queryDTO);
}
