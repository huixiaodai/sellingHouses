package com.sellinghouses.salescontrol.module.appointment.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sellinghouses.salescontrol.common.context.LoginUserContext;
import com.sellinghouses.salescontrol.common.context.LoginUserHolder;
import com.sellinghouses.salescontrol.common.enums.AppointmentStatusEnum;
import com.sellinghouses.salescontrol.common.enums.RoomStatusEnum;
import com.sellinghouses.salescontrol.common.enums.UserRoleEnum;
import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;
import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.module.appointment.dto.AdminAppointmentPageQueryDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentCancelDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentCreateDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentPageQueryDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentQueryDTO;
import com.sellinghouses.salescontrol.module.appointment.dto.AppointmentStatusUpdateDTO;
import com.sellinghouses.salescontrol.module.appointment.entity.Appointment;
import com.sellinghouses.salescontrol.module.appointment.mapper.AppointmentMapper;
import com.sellinghouses.salescontrol.module.appointment.service.AppointmentService;
import com.sellinghouses.salescontrol.module.appointment.vo.AppointmentVO;
import com.sellinghouses.salescontrol.module.room.entity.Room;
import com.sellinghouses.salescontrol.module.room.mapper.RoomMapper;
import com.sellinghouses.salescontrol.module.user.entity.User;
import com.sellinghouses.salescontrol.module.user.mapper.UserMapper;
import com.sellinghouses.salescontrol.module.user.vo.SalesUserVO;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;

    private final RoomMapper roomMapper;

    private final UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long create(AppointmentCreateDTO createDTO) {
        LoginUserContext loginUser = requireCustomer();
        requireRoom(createDTO.getRoomId());
        if (!createDTO.getAppointmentTime().isAfter(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Appointment time must be later than now");
        }
        if (appointmentMapper.countActiveByUserAndRoom(loginUser.getUserId(), createDTO.getRoomId()) > 0) {
            throw new BusinessException(ErrorCode.CONFLICT, "You already have an active appointment for this room");
        }
        Appointment appointment = new Appointment();
        appointment.setUserId(loginUser.getUserId());
        appointment.setRoomId(createDTO.getRoomId());
        appointment.setAppointmentTime(createDTO.getAppointmentTime());
        appointment.setContactName(createDTO.getContactName());
        appointment.setContactPhone(createDTO.getContactPhone());
        appointment.setRemark(createDTO.getRemark());
        appointment.setStatus(AppointmentStatusEnum.BOOKED.getCode());
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
        if (!AppointmentStatusEnum.BOOKED.getCode()
                .equals(resolveDisplayStatus(appointment.getStatus(), appointment.getAppointmentTime()))) {
            throw new BusinessException(ErrorCode.CONFLICT, "Only active booked appointments can be canceled");
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
        List<AppointmentQueryDTO> appointments = appointmentMapper.selectUserPage(queryDTO, loginUser.getUserId());
        PageInfo<AppointmentQueryDTO> pageInfo = new PageInfo<>(appointments);
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
        List<AppointmentQueryDTO> appointments = appointmentMapper.selectAdminPage(queryDTO);
        PageInfo<AppointmentQueryDTO> pageInfo = new PageInfo<>(appointments);
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
        List<AppointmentQueryDTO> appointments = appointmentMapper.selectSalesPage(queryDTO, loginUser.getUserId());
        PageInfo<AppointmentQueryDTO> pageInfo = new PageInfo<>(appointments);
        List<AppointmentVO> records = appointments.stream().map(this::toVO).toList();
        return new PageResult<>(records, pageNo, pageSize, pageInfo.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(AppointmentStatusUpdateDTO updateDTO) {
        LoginUserContext loginUser = requireAdmin();
        AppointmentStatusEnum.validate(updateDTO.getStatus());
        Appointment appointment = requireAppointment(updateDTO.getId());
        if (updateDTO.getSalesUserId() != null) {
            requireUserRole(updateDTO.getSalesUserId(), UserRoleEnum.SALES);
        }
        validateStatusTransition(appointment, updateDTO);
        appointmentMapper.updateStatus(updateDTO.getId(), updateDTO.getStatus(), updateDTO.getSalesUserId(),
                loginUser.getUserId());
    }

    @Override
    public List<SalesUserVO> listSalesUsers() {
        requireAdmin();
        return userMapper.selectSalesUsers();
    }

    private void validateStatusIfPresent(Integer status) {
        if (status != null) {
            AppointmentStatusEnum.validate(status);
        }
    }

    private void validateStatusTransition(Appointment appointment, AppointmentStatusUpdateDTO updateDTO) {
        Integer currentStatus = appointment.getStatus();
        Integer targetStatus = updateDTO.getStatus();
        if (currentStatus.equals(targetStatus)) {
            return;
        }
        Integer displayStatus = resolveDisplayStatus(currentStatus, appointment.getAppointmentTime());
        if (!AppointmentStatusEnum.BOOKED.getCode().equals(displayStatus)
                || AppointmentStatusEnum.BOOKED.getCode().equals(targetStatus)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Invalid appointment status transition");
        }
    }

    private Room requireRoom(Long roomId) {
        Room room = roomMapper.selectById(roomId);
        if (room == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Room does not exist");
        }
        if (!RoomStatusEnum.AVAILABLE.getCode().equals(room.getStatus())) {
            throw new BusinessException(ErrorCode.CONFLICT, "Sold rooms cannot be appointed");
        }
        return room;
    }

    private Appointment requireAppointment(Long id) {
        Appointment appointment = appointmentMapper.selectById(id);
        if (appointment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Appointment does not exist");
        }
        return appointment;
    }

    private User requireUserRole(Long userId, UserRoleEnum roleEnum) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "User does not exist");
        }
        if (!roleEnum.getCode().equals(user.getPrimaryRoleCode())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "User role is invalid");
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

    private AppointmentVO toVO(AppointmentQueryDTO appointment) {
        return AppointmentVO.builder()
                .id(appointment.getId())
                .userId(appointment.getUserId())
                .customerName(appointment.getCustomerName())
                .salesUserId(appointment.getSalesUserId())
                .salesName(appointment.getSalesName())
                .estateId(appointment.getBuildingId())
                .buildingId(appointment.getBuildingId())
                .buildingName(appointment.getBuildingName())
                .unitId(appointment.getUnitId())
                .unitName(appointment.getUnitName())
                .roomId(appointment.getRoomId())
                .roomNo(appointment.getRoomNo())
                .appointmentTime(appointment.getAppointmentTime())
                .contactName(appointment.getContactName())
                .contactPhone(appointment.getContactPhone())
                .remark(appointment.getRemark())
                .status(resolveDisplayStatus(appointment.getStatus(), appointment.getAppointmentTime()))
                .cancelReason(appointment.getCancelReason())
                .createTime(appointment.getCreateTime())
                .build();
    }

    private Integer resolveDisplayStatus(Integer status, LocalDateTime appointmentTime) {
        if (AppointmentStatusEnum.BOOKED.getCode().equals(status)
                && appointmentTime != null
                && appointmentTime.isBefore(LocalDateTime.now())) {
            return AppointmentStatusEnum.EXPIRED.getCode();
        }
        return status;
    }
}
