package com.sellinghouses.salescontrol.module.follow.service.impl;

import com.sellinghouses.salescontrol.common.context.LoginUserContext;
import com.sellinghouses.salescontrol.common.context.LoginUserHolder;
import com.sellinghouses.salescontrol.common.enums.UserRoleEnum;
import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;
import com.sellinghouses.salescontrol.module.appointment.entity.Appointment;
import com.sellinghouses.salescontrol.module.appointment.mapper.AppointmentMapper;
import com.sellinghouses.salescontrol.module.follow.dto.CustomerFollowCreateDTO;
import com.sellinghouses.salescontrol.module.follow.entity.CustomerFollow;
import com.sellinghouses.salescontrol.module.follow.mapper.CustomerFollowMapper;
import com.sellinghouses.salescontrol.module.follow.service.CustomerFollowService;
import com.sellinghouses.salescontrol.module.user.entity.User;
import com.sellinghouses.salescontrol.module.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerFollowServiceImpl implements CustomerFollowService {

    private final CustomerFollowMapper customerFollowMapper;

    private final AppointmentMapper appointmentMapper;

    private final UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long create(CustomerFollowCreateDTO createDTO) {
        LoginUserContext loginUser = requireSales();
        requireCustomer(createDTO.getCustomerUserId());
        if (createDTO.getAppointmentId() != null) {
            Appointment appointment = appointmentMapper.selectById(createDTO.getAppointmentId());
            if (appointment == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND, "预约不存在");
            }
            if (!loginUser.getUserId().equals(appointment.getSalesUserId())) {
                throw new BusinessException(ErrorCode.FORBIDDEN);
            }
            if (!createDTO.getCustomerUserId().equals(appointment.getUserId())) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "客户与预约不匹配");
            }
        }
        CustomerFollow customerFollow = new CustomerFollow();
        customerFollow.setAppointmentId(createDTO.getAppointmentId());
        customerFollow.setCustomerUserId(createDTO.getCustomerUserId());
        customerFollow.setSalesUserId(loginUser.getUserId());
        customerFollow.setFollowType(createDTO.getFollowType());
        customerFollow.setFollowContent(createDTO.getFollowContent());
        customerFollow.setNextFollowTime(createDTO.getNextFollowTime());
        customerFollow.setCreateUser(loginUser.getUserId());
        customerFollow.setUpdateUser(loginUser.getUserId());
        customerFollowMapper.insert(customerFollow);
        return customerFollow.getId();
    }

    private void requireCustomer(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "客户不存在");
        }
        if (!UserRoleEnum.CUSTOMER.getCode().equals(user.getPrimaryRoleCode())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "客户角色不合法");
        }
    }

    private LoginUserContext requireSales() {
        LoginUserContext loginUser = LoginUserHolder.getRequired();
        if (!UserRoleEnum.SALES.getCode().equals(loginUser.getRoleCode())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        return loginUser;
    }
}
