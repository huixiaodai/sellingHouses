package com.sellinghouses.salescontrol.module.room.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sellinghouses.salescontrol.common.context.LoginUserContext;
import com.sellinghouses.salescontrol.common.context.LoginUserHolder;
import com.sellinghouses.salescontrol.common.dto.IdDTO;
import com.sellinghouses.salescontrol.common.enums.RoomStatusEnum;
import com.sellinghouses.salescontrol.common.enums.UserRoleEnum;
import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;
import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.module.building.entity.BuildingUnit;
import com.sellinghouses.salescontrol.module.building.mapper.BuildingMapper;
import com.sellinghouses.salescontrol.module.building.mapper.BuildingUnitMapper;
import com.sellinghouses.salescontrol.module.room.dto.RoomAddDTO;
import com.sellinghouses.salescontrol.module.room.dto.RoomPageQueryDTO;
import com.sellinghouses.salescontrol.module.room.dto.RoomPriceUpdateDTO;
import com.sellinghouses.salescontrol.module.room.dto.RoomQueryDTO;
import com.sellinghouses.salescontrol.module.room.dto.RoomStatusUpdateDTO;
import com.sellinghouses.salescontrol.module.room.dto.RoomUpdateDTO;
import com.sellinghouses.salescontrol.module.room.entity.Room;
import com.sellinghouses.salescontrol.module.room.mapper.RoomMapper;
import com.sellinghouses.salescontrol.module.room.service.RoomService;
import com.sellinghouses.salescontrol.module.room.vo.RoomVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final BuildingMapper buildingMapper;

    private final BuildingUnitMapper buildingUnitMapper;

    private final RoomMapper roomMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long add(RoomAddDTO addDTO) {
        LoginUserContext loginUser = requireAdmin();
        RoomStatusEnum.validate(addDTO.getStatus());
        requireBuildingAndUnit(addDTO.getBuildingId(), addDTO.getUnitId());
        checkRoomNoUnique(addDTO.getUnitId(), addDTO.getRoomNo(), null);
        Room room = new Room();
        fillRoom(room, addDTO.getBuildingId(), addDTO.getUnitId(), addDTO.getRoomNo(), addDTO.getFloorNo(),
                addDTO.getArea(), addDTO.getPrice(), addDTO.getCover(), addDTO.getImages(), addDTO.getLayout(),
                addDTO.getOrientation(), addDTO.getDecoration(), addDTO.getStatus(), addDTO.getRemark());
        room.setCreateUser(loginUser.getUserId());
        room.setUpdateUser(loginUser.getUserId());
        roomMapper.insert(room);
        return room.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(RoomUpdateDTO updateDTO) {
        LoginUserContext loginUser = requireAdmin();
        RoomStatusEnum.validate(updateDTO.getStatus());
        requireRoom(updateDTO.getId());
        requireBuildingAndUnit(updateDTO.getBuildingId(), updateDTO.getUnitId());
        checkRoomNoUnique(updateDTO.getUnitId(), updateDTO.getRoomNo(), updateDTO.getId());
        Room room = new Room();
        room.setId(updateDTO.getId());
        fillRoom(room, updateDTO.getBuildingId(), updateDTO.getUnitId(), updateDTO.getRoomNo(), updateDTO.getFloorNo(),
                updateDTO.getArea(), updateDTO.getPrice(), updateDTO.getCover(), updateDTO.getImages(),
                updateDTO.getLayout(), updateDTO.getOrientation(), updateDTO.getDecoration(), updateDTO.getStatus(),
                updateDTO.getRemark());
        room.setUpdateUser(loginUser.getUserId());
        roomMapper.updateById(room);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(IdDTO idDTO) {
        LoginUserContext loginUser = requireAdmin();
        requireRoom(idDTO.getId());
        roomMapper.logicalDelete(idDTO.getId(), loginUser.getUserId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePrice(RoomPriceUpdateDTO updateDTO) {
        LoginUserContext loginUser = requireAdmin();
        requireRoom(updateDTO.getId());
        roomMapper.updatePrice(updateDTO.getId(), updateDTO.getPrice(), loginUser.getUserId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(RoomStatusUpdateDTO updateDTO) {
        LoginUserContext loginUser = requireAdmin();
        RoomStatusEnum.validate(updateDTO.getStatus());
        requireRoom(updateDTO.getId());
        roomMapper.updateStatus(updateDTO.getId(), updateDTO.getStatus(), loginUser.getUserId());
    }

    @Override
    public RoomVO detail(IdDTO idDTO) {
        requireAdmin();
        RoomQueryDTO room = roomMapper.selectDetailById(idDTO.getId());
        if (room == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "鎴挎簮涓嶅瓨鍦?");
        }
        return toVO(room);
    }

    @Override
    public PageResult<RoomVO> page(RoomPageQueryDTO queryDTO) {
        requireAdmin();
        if (queryDTO.getStatus() != null) {
            RoomStatusEnum.validate(queryDTO.getStatus());
        }
        int pageNo = queryDTO.getPageNo() == null ? 1 : queryDTO.getPageNo();
        int pageSize = queryDTO.getPageSize() == null ? 10 : queryDTO.getPageSize();
        PageHelper.startPage(pageNo, pageSize);
        List<RoomQueryDTO> rooms = roomMapper.selectPage(queryDTO);
        PageInfo<RoomQueryDTO> pageInfo = new PageInfo<>(rooms);
        List<RoomVO> records = rooms.stream().map(this::toVO).toList();
        return new PageResult<>(records, pageNo, pageSize, pageInfo.getTotal());
    }

    @Override
    public RoomVO mobileDetail(IdDTO idDTO) {
        requireLogin();
        RoomQueryDTO room = roomMapper.selectVisibleDetailById(idDTO.getId());
        if (room == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "房源不存在");
        }
        return toVO(room);
    }

    @Override
    public PageResult<RoomVO> mobilePage(RoomPageQueryDTO queryDTO) {
        requireLogin();
        if (queryDTO.getStatus() != null) {
            RoomStatusEnum.validate(queryDTO.getStatus());
        }
        int pageNo = queryDTO.getPageNo() == null ? 1 : queryDTO.getPageNo();
        int pageSize = queryDTO.getPageSize() == null ? 10 : queryDTO.getPageSize();
        PageHelper.startPage(pageNo, pageSize);
        List<RoomQueryDTO> rooms = roomMapper.selectVisiblePage(queryDTO);
        PageInfo<RoomQueryDTO> pageInfo = new PageInfo<>(rooms);
        List<RoomVO> records = rooms.stream().map(this::toVO).toList();
        return new PageResult<>(records, pageNo, pageSize, pageInfo.getTotal());
    }

    private void fillRoom(Room room, Long buildingId, Long unitId, String roomNo, Integer floorNo,
                          java.math.BigDecimal area, java.math.BigDecimal price, String cover, String images,
                          String layout, String orientation, String decoration, Integer status, String remark) {
        room.setBuildingId(buildingId);
        room.setUnitId(unitId);
        room.setRoomNo(roomNo);
        room.setFloorNo(floorNo);
        room.setArea(area);
        room.setPrice(price);
        room.setCover(cover);
        room.setImages(images);
        room.setLayout(layout);
        room.setOrientation(orientation);
        room.setDecoration(decoration);
        room.setStatus(status);
        room.setRemark(remark);
    }

    private void requireBuildingAndUnit(Long buildingId, Long unitId) {
        if (buildingMapper.selectById(buildingId) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "楼盘不存在");
        }
        BuildingUnit unit = buildingUnitMapper.selectById(unitId);
        if (unit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "楼栋不存在");
        }
        if (!buildingId.equals(unit.getBuildingId())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "楼栋不属于该楼盘");
        }
    }

    private Room requireRoom(Long id) {
        Room room = roomMapper.selectById(id);
        if (room == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "房源不存在");
        }
        return room;
    }

    private void checkRoomNoUnique(Long unitId, String roomNo, Long excludeId) {
        if (roomMapper.countByRoomNo(unitId, roomNo, excludeId) > 0) {
            throw new BusinessException(ErrorCode.CONFLICT, "同一楼栋下房号已存在");
        }
    }

    private LoginUserContext requireAdmin() {
        LoginUserContext loginUser = LoginUserHolder.getRequired();
        if (!UserRoleEnum.ADMIN.getCode().equals(loginUser.getRoleCode())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        return loginUser;
    }

    private void requireLogin() {
        LoginUserHolder.getRequired();
    }

    private RoomVO toVO(Room room) {
        return RoomVO.builder()
                .id(room.getId())
                .buildingId(room.getBuildingId())
                .unitId(room.getUnitId())
                .roomNo(room.getRoomNo())
                .floorNo(room.getFloorNo())
                .area(room.getArea())
                .price(room.getPrice())
                .cover(room.getCover())
                .images(room.getImages())
                .layout(room.getLayout())
                .orientation(room.getOrientation())
                .decoration(room.getDecoration())
                .status(room.getStatus())
                .remark(room.getRemark())
                .createTime(room.getCreateTime())
                .build();
    }

    private RoomVO toVO(RoomQueryDTO room) {
        return RoomVO.builder()
                .id(room.getId())
                .buildingId(room.getBuildingId())
                .buildingName(room.getBuildingName())
                .unitId(room.getUnitId())
                .unitName(room.getUnitName())
                .roomNo(room.getRoomNo())
                .floorNo(room.getFloorNo())
                .area(room.getArea())
                .price(room.getPrice())
                .cover(room.getCover())
                .images(room.getImages())
                .layout(room.getLayout())
                .orientation(room.getOrientation())
                .decoration(room.getDecoration())
                .status(room.getStatus())
                .remark(room.getRemark())
                .createTime(room.getCreateTime())
                .build();
    }
}
