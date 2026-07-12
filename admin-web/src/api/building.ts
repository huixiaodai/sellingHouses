import { request } from '@/utils/request';
import type { IdPayload, PageResult } from '@/types/common';
import type { BuildingForm, BuildingPageQuery, BuildingStatusUpdate, BuildingVO } from '@/types/building';

export function createBuildingApi(data: BuildingForm) {
  return request<number>({ url: '/api/admin/building/create', method: 'post', data });
}

export function updateBuildingApi(data: BuildingForm) {
  return request<void>({ url: '/api/admin/building/update', method: 'post', data });
}

export function deleteBuildingApi(data: IdPayload) {
  return request<void>({ url: '/api/admin/building/delete', method: 'post', data });
}

export function updateBuildingStatusApi(data: BuildingStatusUpdate) {
  return request<void>({ url: '/api/admin/building/update-status', method: 'post', data });
}

export function getBuildingDetailApi(params: IdPayload) {
  return request<BuildingVO>({ url: '/api/admin/building/detail', method: 'get', params });
}

export function getBuildingPageApi(params: BuildingPageQuery) {
  return request<PageResult<BuildingVO>>({ url: '/api/admin/building/page', method: 'get', params });
}
