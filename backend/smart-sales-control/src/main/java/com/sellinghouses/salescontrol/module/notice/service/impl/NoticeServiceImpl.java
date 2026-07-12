package com.sellinghouses.salescontrol.module.notice.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sellinghouses.salescontrol.common.context.LoginUserContext;
import com.sellinghouses.salescontrol.common.context.LoginUserHolder;
import com.sellinghouses.salescontrol.common.dto.IdDTO;
import com.sellinghouses.salescontrol.common.enums.NoticeTargetRoleEnum;
import com.sellinghouses.salescontrol.common.enums.UserRoleEnum;
import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;
import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.module.notice.dto.NoticeCreateDTO;
import com.sellinghouses.salescontrol.module.notice.dto.NoticePageQueryDTO;
import com.sellinghouses.salescontrol.module.notice.entity.Notice;
import com.sellinghouses.salescontrol.module.notice.mapper.NoticeMapper;
import com.sellinghouses.salescontrol.module.notice.service.NoticeService;
import com.sellinghouses.salescontrol.module.notice.vo.NoticeVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private static final int PUBLISHED_STATUS = 1;

    private final NoticeMapper noticeMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long create(NoticeCreateDTO createDTO) {
        LoginUserContext loginUser = requireAdmin();
        NoticeTargetRoleEnum.validate(createDTO.getTargetRoleCode());
        Notice notice = new Notice();
        notice.setTitle(createDTO.getTitle());
        notice.setContent(createDTO.getContent());
        notice.setPublisherId(loginUser.getUserId());
        notice.setTargetRole(createDTO.getTargetRoleCode());
        notice.setStatus(PUBLISHED_STATUS);
        notice.setCreateUser(loginUser.getUserId());
        notice.setUpdateUser(loginUser.getUserId());
        noticeMapper.insert(notice);
        return notice.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(IdDTO idDTO) {
        LoginUserContext loginUser = requireAdmin();
        if (noticeMapper.selectById(idDTO.getId()) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "公告不存在");
        }
        noticeMapper.deleteById(idDTO.getId());
    }

    @Override
    public NoticeVO detail(IdDTO idDTO) {
        LoginUserContext loginUser = LoginUserHolder.getRequired();
        Notice notice = noticeMapper.selectVisibleById(idDTO.getId(), loginUser.getRoleCode());
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "公告不存在");
        }
        return toVO(notice);
    }

    @Override
    public NoticeVO adminDetail(IdDTO idDTO) {
        requireAdmin();
        Notice notice = noticeMapper.selectPublishedById(idDTO.getId());
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "公告不存在");
        }
        return toVO(notice);
    }

    @Override
    public PageResult<NoticeVO> page(NoticePageQueryDTO queryDTO) {
        LoginUserContext loginUser = LoginUserHolder.getRequired();
        int pageNo = queryDTO.getPageNo() == null ? 1 : queryDTO.getPageNo();
        int pageSize = queryDTO.getPageSize() == null ? 10 : queryDTO.getPageSize();
        PageHelper.startPage(pageNo, pageSize);
        List<Notice> notices = noticeMapper.selectVisiblePage(loginUser.getRoleCode(), queryDTO.getTitle());
        PageInfo<Notice> pageInfo = new PageInfo<>(notices);
        List<NoticeVO> records = notices.stream().map(this::toVO).toList();
        return new PageResult<>(records, pageNo, pageSize, pageInfo.getTotal());
    }

    @Override
    public PageResult<NoticeVO> adminPage(NoticePageQueryDTO queryDTO) {
        requireAdmin();
        int pageNo = queryDTO.getPageNo() == null ? 1 : queryDTO.getPageNo();
        int pageSize = queryDTO.getPageSize() == null ? 10 : queryDTO.getPageSize();
        PageHelper.startPage(pageNo, pageSize);
        List<Notice> notices = noticeMapper.selectPublishedPage(queryDTO.getTitle());
        PageInfo<Notice> pageInfo = new PageInfo<>(notices);
        List<NoticeVO> records = notices.stream().map(this::toVO).toList();
        return new PageResult<>(records, pageNo, pageSize, pageInfo.getTotal());
    }

    private LoginUserContext requireAdmin() {
        LoginUserContext loginUser = LoginUserHolder.getRequired();
        if (!UserRoleEnum.ADMIN.getCode().equals(loginUser.getRoleCode())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        return loginUser;
    }

    private NoticeVO toVO(Notice notice) {
        return NoticeVO.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .publisherId(notice.getPublisherId())
                .targetRole(notice.getTargetRole())
                .status(notice.getStatus())
                .publishTime(notice.getPublishTime())
                .build();
    }
}
