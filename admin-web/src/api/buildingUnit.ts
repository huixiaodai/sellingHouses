import { request } from '@/utils/request';
import type { IdPayload, PageResult } from '@/types/common';
import type {
  BuildingUnitForm,
  BuildingUnitPageQuery,
  BuildingUnitStatusUpdate,
  BuildingUnitVO
} from '@/types/buildingUnit';

export function createBuildingUnitApi(data: BuildingUnitForm) {
  return request<number>({ url: '/api/admin/building-unit/create', method: 'post', data });
}

export function updateBuildingUnitApi(data: BuildingUnitForm) {
  return request<void>({ url: '/api/admin/building-unit/update', method: 'post', data });
}

export function deleteBuildingUnitApi(data: IdPayload) {
  return request<void>({ url: '/api/admin/building-unit/delete', method: 'post', data });
}

export function updateBuildingUnitStatusApi(data: BuildingUnitStatusUpdate) {
  return request<void>({ url: '/api/admin/building-unit/update-status', method: 'post', data });
}

export function getBuildingUnitPageApi(params: BuildingUnitPageQuery) {
  return request<PageResult<BuildingUnitVO>>({ url: '/api/admin/building-unit/page', method: 'get', params });
}

export function listBuildingUnitsByBuildingApi(buildingId: number) {
  return request<BuildingUnitVO[]>({
    url: '/api/admin/building-unit/list-by-building',
    method: 'get',
    params: { buildingId }
  });
}
