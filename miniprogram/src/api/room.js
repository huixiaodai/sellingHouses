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

export function getControlRoomList(params) {
  return request({
    url: '/api/room/list',
    data: params
  });
}

export function searchControlRooms(params) {
  return request({
    url: '/api/room/search',
    data: params
  });
}

export function filterControlRooms(data) {
  return request({
    url: '/api/room/filter',
    method: 'POST',
    data
  });
}
