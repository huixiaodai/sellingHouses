package com.sellinghouses.salescontrol.module.user.service.impl;

import com.sellinghouses.salescontrol.common.context.LoginUserContext;
import com.sellinghouses.salescontrol.common.context.LoginUserHolder;
import com.sellinghouses.salescontrol.common.enums.UserRoleEnum;
import com.sellinghouses.salescontrol.common.enums.UserStatusEnum;
import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;
import com.sellinghouses.salescontrol.common.util.PasswordUtil;
import com.sellinghouses.salescontrol.module.user.dto.SalesUserCreateDTO;
import com.sellinghouses.salescontrol.module.user.entity.User;
import com.sellinghouses.salescontrol.module.user.mapper.UserMapper;
import com.sellinghouses.salescontrol.module.user.mapper.UserRoleMapper;
import com.sellinghouses.salescontrol.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRoleMapper userRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSalesUser(SalesUserCreateDTO createDTO) {
        LoginUserContext loginUser = requireAdmin();
        validateStatus(createDTO.getStatus());
        if (userMapper.selectByUsername(createDTO.getUsername()) != null) {
            throw new BusinessException(ErrorCode.CONFLICT, "账号已存在");
        }
        if (userMapper.selectByPhone(createDTO.getPhone()) != null) {
            throw new BusinessException(ErrorCode.CONFLICT, "手机号已存在");
        }

        User user = new User();
        user.setUsername(createDTO.getUsername());
        user.setPassword(PasswordUtil.encode(createDTO.getPassword()));
        user.setRealName(createDTO.getRealName());
        user.setPhone(createDTO.getPhone());
        user.setPrimaryRoleCode(UserRoleEnum.SALES.getCode());
        user.setStatus(createDTO.getStatus());
        user.setCreateUser(loginUser.getUserId());
        user.setUpdateUser(loginUser.getUserId());
        userMapper.insert(user);

        int rows = userRoleMapper.insertByRoleCode(user.getId(), UserRoleEnum.SALES.getCode(), loginUser.getUserId());
        if (rows == 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "销售角色不存在");
        }
        return user.getId();
    }

    private LoginUserContext requireAdmin() {
        LoginUserContext loginUser = LoginUserHolder.getRequired();
        if (!UserRoleEnum.ADMIN.getCode().equals(loginUser.getRoleCode())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        return loginUser;
    }

    private void validateStatus(Integer status) {
        if (UserStatusEnum.ENABLED.getCode().equals(status) || UserStatusEnum.DISABLED.getCode().equals(status)) {
            return;
        }
        throw new BusinessException(ErrorCode.BAD_REQUEST, "状态不合法");
    }
}
