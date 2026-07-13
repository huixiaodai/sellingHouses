import { request } from '../utils/request';

export function getRoomPage(params) {
  return request({
    url: '/api/room/page',
    data: params
  });
}

export function getRoomDetail(id) {
  return request({
    url: '/api/room/detail',
    data: { id }
  });
}
