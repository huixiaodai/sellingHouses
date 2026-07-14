import { request } from '../utils/request';

export function getBuildingPage(params) {
  return request({
    url: '/api/building/page',
    data: params
  });
}

export function getBuildingDetail(id) {
  return request({
    url: '/api/building/detail',
    data: { id }
  });
}

export function getControlBuildingList(estateId) {
  return request({
    url: '/api/building/list',
    data: { estateId }
  });
}

export function getFloorList(params) {
  return request({
    url: '/api/floor/list',
    data: params
  });
}
