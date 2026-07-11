package com.sellinghouses.salescontrol.module.auth.service.impl;

import com.sellinghouses.salescontrol.common.context.LoginUserContext;
import com.sellinghouses.salescontrol.common.context.LoginUserHolder;
import com.sellinghouses.salescontrol.common.enums.UserRoleEnum;
import com.sellinghouses.salescontrol.common.enums.UserStatusEnum;
import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;
import com.sellinghouses.salescontrol.common.util.JwtUtil;
import com.sellinghouses.salescontrol.common.util.MaskUtil;
import com.sellinghouses.salescontrol.common.util.PasswordUtil;
import com.sellinghouses.salescontrol.module.auth.dto.LoginRequest;
import com.sellinghouses.salescontrol.module.auth.dto.RegisterRequest;
import com.sellinghouses.salescontrol.module.auth.service.AuthService;
import com.sellinghouses.salescontrol.module.auth.vo.CurrentUserVO;
import com.sellinghouses.salescontrol.module.auth.vo.LoginVO;
import com.sellinghouses.salescontrol.module.user.entity.User;
import com.sellinghouses.salescontrol.module.user.mapper.UserMapper;
import com.sellinghouses.salescontrol.module.user.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;

    private final UserRoleMapper userRoleMapper;

    private final JwtUtil jwtUtil;

    @Override
    public LoginVO login(LoginRequest request) {
        User user = userMapper.selectByUsername(request.getUsername());
        if (user == null || !PasswordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "账号或密码错误");
        }
        if (!UserStatusEnum.ENABLED.getCode().equals(user.getStatus())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "账号已被禁用");
        }
        userMapper.updateLastLoginTime(user.getId());
        UserRoleEnum roleEnum = UserRoleEnum.fromCode(user.getPrimaryRoleCode());
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getPrimaryRoleCode());
        return LoginVO.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .roleCode(user.getPrimaryRoleCode())
                .homePath(roleEnum.getHomePath())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterRequest request) {
        if (userMapper.selectByUsername(request.getUsername()) != null) {
            throw new BusinessException(ErrorCode.CONFLICT, "登录账号已存在");
        }
        if (userMapper.selectByPhone(request.getPhone()) != null) {
            throw new BusinessException(ErrorCode.CONFLICT, "手机号已注册");
        }
        if (StringUtils.hasText(request.getWxOpenid()) && userMapper.selectByWxOpenid(request.getWxOpenid()) != null) {
            throw new BusinessException(ErrorCode.CONFLICT, "微信账号已注册");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(PasswordUtil.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        user.setWxOpenid(request.getWxOpenid());
        user.setPrimaryRoleCode(UserRoleEnum.CUSTOMER.getCode());
        user.setStatus(UserStatusEnum.ENABLED.getCode());
        userMapper.insert(user);
        int rows = userRoleMapper.insertByRoleCode(user.getId(), UserRoleEnum.CUSTOMER.getCode(), user.getId());
        if (rows == 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "默认角色不存在");
        }
    }

    @Override
    public CurrentUserVO currentUser() {
        LoginUserContext loginUserContext = LoginUserHolder.getRequired();
        User user = userMapper.selectById(loginUserContext.getUserId());
        if (user == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return CurrentUserVO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .phone(MaskUtil.maskPhone(user.getPhone()))
                .roleCode(user.getPrimaryRoleCode())
                .status(user.getStatus())
                .build();
    }

    @Override
    public void logout() {
        LoginUserHolder.getRequired();
    }
}
