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
