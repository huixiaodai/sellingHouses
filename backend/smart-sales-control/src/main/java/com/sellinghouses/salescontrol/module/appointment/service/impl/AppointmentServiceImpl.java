package com.sellinghouses.salescontrol.module.appointment.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sellinghouses.salescontrol.common.context.LoginUserContext;
import com.sellinghouses.salescontrol.common.context.LoginUserHolder;
import com.sellinghouses.salescontrol.common.enums.AppointmentStatusEnum;
import com.sellinghouses.salescontrol.common.enums.UserRoleEnum;
import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;
import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.module.appointment.dto.AdminAppointmentPageQueryDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentCancelDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentCreateDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentPageQueryDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentStatusUpdateDTO;
import com.sellinghouses.salescontrol.module.appointment.entity.Appointment;
import com.sellinghouses.salescontrol.module.appointment.mapper.AppointmentMapper;
import com.sellinghouses.salescontrol.module.appointment.service.AppointmentService;
import com.sellinghouses.salescontrol.module.appointment.vo.AppointmentVO;
import com.sellinghouses.salescontrol.module.building.mapper.BuildingMapper;
import com.sellinghouses.salescontrol.module.room.entity.Room;
import com.sellinghouses.salescontrol.module.room.mapper.RoomMapper;
import com.sellinghouses.salescontrol.module.user.entity.User;
import com.sellinghouses.salescontrol.module.user.mapper.UserMapper;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;

    private final BuildingMapper buildingMapper;

    private final RoomMapper roomMapper;

    private final UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long create(AppointmentCreateDTO createDTO) {
        LoginUserContext loginUser = requireCustomer();
        requireBuilding(createDTO.getEstateId());
        if (createDTO.getRoomId() != null) {
            Room room = requireRoom(createDTO.getRoomId());
            if (!createDTO.getEstateId().equals(room.getBuildingId())) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "房源不属于该楼盘");
            }
        }
        if (!createDTO.getAppointmentTime().isAfter(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "预约时间必须晚于当前时间");
        }
        Appointment appointment = new Appointment();
        appointment.setUserId(loginUser.getUserId());
        appointment.setBuildingId(createDTO.getEstateId());
        appointment.setRoomId(createDTO.getRoomId());
        appointment.setAppointmentTime(createDTO.getAppointmentTime());
        appointment.setContactName(createDTO.getContactName());
        appointment.setContactPhone(createDTO.getContactPhone());
        appointment.setRemark(createDTO.getRemark());
        appointment.setStatus(AppointmentStatusEnum.PENDING.getCode());
        appointment.setCreateUser(loginUser.getUserId());
        appointment.setUpdateUser(loginUser.getUserId());
        appointmentMapper.insert(appointment);
        return appointment.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancel(AppointmentCancelDTO cancelDTO) {
        LoginUserContext loginUser = requireCustomer();
        Appointment appointment = requireAppointment(cancelDTO.getId());
        if (!loginUser.getUserId().equals(appointment.getUserId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        if (AppointmentStatusEnum.CANCELED.getCode().equals(appointment.getStatus())) {
            throw new BusinessException(ErrorCode.CONFLICT, "预约已取消");
        }
        appointmentMapper.cancel(cancelDTO.getId(), cancelDTO.getCancelReason(), loginUser.getUserId());
    }

    @Override
    public PageResult<AppointmentVO> userPage(AppointmentPageQueryDTO queryDTO) {
        LoginUserContext loginUser = requireCustomer();
        validateStatusIfPresent(queryDTO.getStatus());
        int pageNo = queryDTO.getPageNo() == null ? 1 : queryDTO.getPageNo();
        int pageSize = queryDTO.getPageSize() == null ? 10 : queryDTO.getPageSize();
        PageHelper.startPage(pageNo, pageSize);
        List<Appointment> appointments = appointmentMapper.selectUserPage(queryDTO, loginUser.getUserId());
        PageInfo<Appointment> pageInfo = new PageInfo<>(appointments);
        List<AppointmentVO> records = appointments.stream().map(this::toVO).toList();
        return new PageResult<>(records, pageNo, pageSize, pageInfo.getTotal());
    }

    @Override
    public PageResult<AppointmentVO> adminPage(AdminAppointmentPageQueryDTO queryDTO) {
        requireAdmin();
        validateStatusIfPresent(queryDTO.getStatus());
        int pageNo = queryDTO.getPageNo() == null ? 1 : queryDTO.getPageNo();
        int pageSize = queryDTO.getPageSize() == null ? 10 : queryDTO.getPageSize();
        PageHelper.startPage(pageNo, pageSize);
        List<Appointment> appointments = appointmentMapper.selectAdminPage(queryDTO);
        PageInfo<Appointment> pageInfo = new PageInfo<>(appointments);
        List<AppointmentVO> records = appointments.stream().map(this::toVO).toList();
        return new PageResult<>(records, pageNo, pageSize, pageInfo.getTotal());
    }

    @Override
    public PageResult<AppointmentVO> salesPage(AppointmentPageQueryDTO queryDTO) {
        LoginUserContext loginUser = requireSales();
        validateStatusIfPresent(queryDTO.getStatus());
        int pageNo = queryDTO.getPageNo() == null ? 1 : queryDTO.getPageNo();
        int pageSize = queryDTO.getPageSize() == null ? 10 : queryDTO.getPageSize();
        PageHelper.startPage(pageNo, pageSize);
        List<Appointment> appointments = appointmentMapper.selectSalesPage(queryDTO, loginUser.getUserId());
        PageInfo<Appointment> pageInfo = new PageInfo<>(appointments);
        List<AppointmentVO> records = appointments.stream().map(this::toVO).toList();
        return new PageResult<>(records, pageNo, pageSize, pageInfo.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(AppointmentStatusUpdateDTO updateDTO) {
        LoginUserContext loginUser = requireAdmin();
        AppointmentStatusEnum.validate(updateDTO.getStatus());
        requireAppointment(updateDTO.getId());
        if (updateDTO.getSalesUserId() != null) {
            requireUserRole(updateDTO.getSalesUserId(), UserRoleEnum.SALES);
        }
        appointmentMapper.updateStatus(updateDTO.getId(), updateDTO.getStatus(), updateDTO.getSalesUserId(), loginUser.getUserId());
    }

    private void validateStatusIfPresent(Integer status) {
        if (status != null) {
            AppointmentStatusEnum.validate(status);
        }
    }

    private void requireBuilding(Long buildingId) {
        if (buildingMapper.selectById(buildingId) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "楼盘不存在");
        }
    }

    private Room requireRoom(Long roomId) {
        Room room = roomMapper.selectById(roomId);
        if (room == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "房源不存在");
        }
        return room;
    }

    private Appointment requireAppointment(Long id) {
        Appointment appointment = appointmentMapper.selectById(id);
        if (appointment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "预约不存在");
        }
        return appointment;
    }

    private User requireUserRole(Long userId, UserRoleEnum roleEnum) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        if (!roleEnum.getCode().equals(user.getPrimaryRoleCode())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "用户角色不合法");
        }
        return user;
    }

    private LoginUserContext requireAdmin() {
        return requireRole(UserRoleEnum.ADMIN);
    }

    private LoginUserContext requireSales() {
        return requireRole(UserRoleEnum.SALES);
    }

    private LoginUserContext requireCustomer() {
        return requireRole(UserRoleEnum.CUSTOMER);
    }

    private LoginUserContext requireRole(UserRoleEnum roleEnum) {
        LoginUserContext loginUser = LoginUserHolder.getRequired();
        if (!roleEnum.getCode().equals(loginUser.getRoleCode())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        return loginUser;
    }

    private AppointmentVO toVO(Appointment appointment) {
        return AppointmentVO.builder()
                .id(appointment.getId())
                .userId(appointment.getUserId())
                .salesUserId(appointment.getSalesUserId())
                .estateId(appointment.getBuildingId())
                .roomId(appointment.getRoomId())
                .appointmentTime(appointment.getAppointmentTime())
                .contactName(appointment.getContactName())
                .contactPhone(appointment.getContactPhone())
                .remark(appointment.getRemark())
                .status(appointment.getStatus())
                .cancelReason(appointment.getCancelReason())
                .createTime(appointment.getCreateTime())
                .build();
    }
}
