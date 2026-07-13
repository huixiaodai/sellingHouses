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
import com.sellinghouses.salescontrol.module.building.dto.BuildingAddDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingPageQueryDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingStatusUpdateDTO;
import com.sellinghouses.salescontrol.module.building.dto.BuildingUpdateDTO;
import com.sellinghouses.salescontrol.module.building.entity.Building;
import com.sellinghouses.salescontrol.module.building.mapper.BuildingMapper;
import com.sellinghouses.salescontrol.module.building.mapper.BuildingUnitMapper;
import com.sellinghouses.salescontrol.module.building.service.BuildingService;
import com.sellinghouses.salescontrol.module.building.vo.BuildingVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {

    private final BuildingMapper buildingMapper;

    private final BuildingUnitMapper buildingUnitMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long add(BuildingAddDTO addDTO) {
        LoginUserContext loginUser = requireAdmin();
        BuildingStatusEnum.validate(addDTO.getStatus());
        checkNameUnique(addDTO.getName(), null);
        Building building = new Building();
        building.setName(addDTO.getName());
        building.setCover(addDTO.getCover());
        building.setBannerImages(addDTO.getBannerImages());
        building.setDeveloper(addDTO.getDeveloper());
        building.setAddress(addDTO.getAddress());
        building.setDescription(addDTO.getDescription());
        building.setOpeningTime(addDTO.getOpeningTime());
        building.setDeliveryTime(addDTO.getDeliveryTime());
        building.setStatus(addDTO.getStatus());
        building.setCreateUser(loginUser.getUserId());
        building.setUpdateUser(loginUser.getUserId());
        buildingMapper.insert(building);
        return building.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(BuildingUpdateDTO updateDTO) {
        LoginUserContext loginUser = requireAdmin();
        BuildingStatusEnum.validate(updateDTO.getStatus());
        requireBuilding(updateDTO.getId());
        checkNameUnique(updateDTO.getName(), updateDTO.getId());
        Building building = new Building();
        building.setId(updateDTO.getId());
        building.setName(updateDTO.getName());
        building.setCover(updateDTO.getCover());
        building.setBannerImages(updateDTO.getBannerImages());
        building.setDeveloper(updateDTO.getDeveloper());
        building.setAddress(updateDTO.getAddress());
        building.setDescription(updateDTO.getDescription());
        building.setOpeningTime(updateDTO.getOpeningTime());
        building.setDeliveryTime(updateDTO.getDeliveryTime());
        building.setStatus(updateDTO.getStatus());
        building.setUpdateUser(loginUser.getUserId());
        buildingMapper.updateById(building);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(IdDTO idDTO) {
        LoginUserContext loginUser = requireAdmin();
        requireBuilding(idDTO.getId());
        if (buildingUnitMapper.countByBuildingId(idDTO.getId()) > 0) {
            throw new BusinessException(ErrorCode.CONFLICT, "楼盘下存在楼栋，不能删除");
        }
        buildingMapper.logicalDelete(idDTO.getId(), loginUser.getUserId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(BuildingStatusUpdateDTO updateDTO) {
        LoginUserContext loginUser = requireAdmin();
        BuildingStatusEnum.validate(updateDTO.getStatus());
        requireBuilding(updateDTO.getId());
        buildingMapper.updateStatus(updateDTO.getId(), updateDTO.getStatus(), loginUser.getUserId());
    }

    @Override
    public BuildingVO detail(IdDTO idDTO) {
        requireAdmin();
        return toVO(requireBuilding(idDTO.getId()));
    }

    @Override
    public PageResult<BuildingVO> page(BuildingPageQueryDTO queryDTO) {
        requireAdmin();
        int pageNo = queryDTO.getPageNo() == null ? 1 : queryDTO.getPageNo();
        int pageSize = queryDTO.getPageSize() == null ? 10 : queryDTO.getPageSize();
        PageHelper.startPage(pageNo, pageSize);
        List<Building> buildings = buildingMapper.selectPage(queryDTO);
        PageInfo<Building> pageInfo = new PageInfo<>(buildings);
        List<BuildingVO> records = buildings.stream().map(this::toVO).toList();
        return new PageResult<>(records, pageNo, pageSize, pageInfo.getTotal());
    }

    @Override
    public BuildingVO mobileDetail(IdDTO idDTO) {
        requireLogin();
        Building building = buildingMapper.selectVisibleById(idDTO.getId());
        if (building == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "楼盘不存在");
        }
        return toVO(building);
    }

    @Override
    public PageResult<BuildingVO> mobilePage(BuildingPageQueryDTO queryDTO) {
        requireLogin();
        if (queryDTO.getStatus() != null) {
            BuildingStatusEnum.validate(queryDTO.getStatus());
        }
        int pageNo = queryDTO.getPageNo() == null ? 1 : queryDTO.getPageNo();
        int pageSize = queryDTO.getPageSize() == null ? 10 : queryDTO.getPageSize();
        PageHelper.startPage(pageNo, pageSize);
        List<Building> buildings = buildingMapper.selectVisiblePage(queryDTO);
        PageInfo<Building> pageInfo = new PageInfo<>(buildings);
        List<BuildingVO> records = buildings.stream().map(this::toVO).toList();
        return new PageResult<>(records, pageNo, pageSize, pageInfo.getTotal());
    }

    private Building requireBuilding(Long id) {
        Building building = buildingMapper.selectById(id);
        if (building == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "楼盘不存在");
        }
        return building;
    }

    private void checkNameUnique(String name, Long excludeId) {
        if (buildingMapper.countByName(name, excludeId) > 0) {
            throw new BusinessException(ErrorCode.CONFLICT, "楼盘名称已存在");
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

    private BuildingVO toVO(Building building) {
        return BuildingVO.builder()
                .id(building.getId())
                .name(building.getName())
                .cover(building.getCover())
                .bannerImages(building.getBannerImages())
                .developer(building.getDeveloper())
                .address(building.getAddress())
                .description(building.getDescription())
                .openingTime(building.getOpeningTime())
                .deliveryTime(building.getDeliveryTime())
                .status(building.getStatus())
                .createTime(building.getCreateTime())
                .build();
    }
}
