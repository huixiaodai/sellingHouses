package com.sellinghouses.salescontrol.module.building.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sellinghouses.salescontrol.common.context.LoginUserContext;
import com.sellinghouses.salescontrol.common.context.LoginUserHolder;
import com.sellinghouses.salescontrol.common.dto.IdDTO;
import com.sellinghouses.salescontrol.common.enums.BuildingStatusEnum;
import com.sellinghouses.salescontrol.common.enums.UserRoleEnum;
import com.sellinghouses.salescontrol.common.exception.BusinessException;
import com.sellinghouses.salescontrol.common.exception.ErrorCode;
import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.module.building.dto.BuildingUnitAddDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingUnitPageQueryDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingUnitQueryDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingUnitStatusUpdateDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingUnitUpdateDTO;
import com.sellinghouses.salescontrol.module.building.entity.BuildingUnit;
import com.sellinghouses.salescontrol.module.building.mapper.BuildingMapper;
import com.sellinghouses.salescontrol.module.building.mapper.BuildingUnitMapper;
import com.sellinghouses.salescontrol.module.building.service.BuildingUnitService;
import com.sellinghouses.salescontrol.module.building.vo.BuildingUnitVO;
import com.sellinghouses.salescontrol.module.room.mapper.RoomMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuildingUnitServiceImpl implements BuildingUnitService {

    private final BuildingMapper buildingMapper;

    private final BuildingUnitMapper buildingUnitMapper;

    private final RoomMapper roomMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long add(BuildingUnitAddDTO addDTO) {
        LoginUserContext loginUser = requireAdmin();
        BuildingStatusEnum.validate(addDTO.getStatus());
        requireBuilding(addDTO.getBuildingId());
        checkNameUnique(addDTO.getBuildingId(), addDTO.getName(), null);
        BuildingUnit unit = new BuildingUnit();
        unit.setBuildingId(addDTO.getBuildingId());
        unit.setName(addDTO.getName());
        unit.setSortNo(addDTO.getSortNo());
        unit.setStatus(addDTO.getStatus());
        unit.setCreateUser(loginUser.getUserId());
        unit.setUpdateUser(loginUser.getUserId());
        buildingUnitMapper.insert(unit);
        return unit.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(BuildingUnitUpdateDTO updateDTO) {
        LoginUserContext loginUser = requireAdmin();
        BuildingStatusEnum.validate(updateDTO.getStatus());
        requireUnit(updateDTO.getId());
        requireBuilding(updateDTO.getBuildingId());
        checkNameUnique(updateDTO.getBuildingId(), updateDTO.getName(), updateDTO.getId());
        BuildingUnit unit = new BuildingUnit();
        unit.setId(updateDTO.getId());
        unit.setBuildingId(updateDTO.getBuildingId());
        unit.setName(updateDTO.getName());
        unit.setSortNo(updateDTO.getSortNo());
        unit.setStatus(updateDTO.getStatus());
        unit.setUpdateUser(loginUser.getUserId());
        buildingUnitMapper.updateById(unit);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(IdDTO idDTO) {
        LoginUserContext loginUser = requireAdmin();
        requireUnit(idDTO.getId());
        if (roomMapper.countByUnitId(idDTO.getId()) > 0) {
            throw new BusinessException(ErrorCode.CONFLICT, "楼栋下存在房源，不能删除");
        }
        buildingUnitMapper.logicalDelete(idDTO.getId(), loginUser.getUserId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(BuildingUnitStatusUpdateDTO updateDTO) {
        LoginUserContext loginUser = requireAdmin();
        BuildingStatusEnum.validate(updateDTO.getStatus());
        requireUnit(updateDTO.getId());
        buildingUnitMapper.updateStatus(updateDTO.getId(), updateDTO.getStatus(), loginUser.getUserId());
    }

    @Override
    public PageResult<BuildingUnitVO> page(BuildingUnitPageQueryDTO queryDTO) {
        requireAdmin();
        int pageNo = queryDTO.getPageNo() == null ? 1 : queryDTO.getPageNo();
        int pageSize = queryDTO.getPageSize() == null ? 10 : queryDTO.getPageSize();
        PageHelper.startPage(pageNo, pageSize);
        List<BuildingUnitQueryDTO> units = buildingUnitMapper.selectPage(queryDTO);
        PageInfo<BuildingUnitQueryDTO> pageInfo = new PageInfo<>(units);
        List<BuildingUnitVO> records = units.stream().map(this::toVO).toList();
        return new PageResult<>(records, pageNo, pageSize, pageInfo.getTotal());
    }

    @Override
    public List<BuildingUnitVO> listByBuilding(Long buildingId) {
        requireAdmin();
        requireBuilding(buildingId);
        return buildingUnitMapper.selectByBuildingId(buildingId).stream().map(this::toVO).toList();
    }

    private void requireBuilding(Long buildingId) {
        if (buildingMapper.selectById(buildingId) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "楼盘不存在");
        }
    }

    private BuildingUnit requireUnit(Long id) {
        BuildingUnit unit = buildingUnitMapper.selectById(id);
        if (unit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "楼栋不存在");
        }
        return unit;
    }

    private void checkNameUnique(Long buildingId, String name, Long excludeId) {
        if (buildingUnitMapper.countByName(buildingId, name, excludeId) > 0) {
            throw new BusinessException(ErrorCode.CONFLICT, "楼栋名称已存在");
        }
    }

    private LoginUserContext requireAdmin() {
        LoginUserContext loginUser = LoginUserHolder.getRequired();
        if (!UserRoleEnum.ADMIN.getCode().equals(loginUser.getRoleCode())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        return loginUser;
    }

    private BuildingUnitVO toVO(BuildingUnitQueryDTO unit) {
        return BuildingUnitVO.builder()
                .id(unit.getId())
                .buildingId(unit.getBuildingId())
                .buildingName(unit.getBuildingName())
                .name(unit.getName())
                .sortNo(unit.getSortNo())
                .status(unit.getStatus())
                .createTime(unit.getCreateTime())
                .build();
    }
}
