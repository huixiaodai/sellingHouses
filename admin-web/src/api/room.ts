import { request } from '@/utils/request';
import type { IdPayload, PageResult } from '@/types/common';
import type { RoomForm, RoomPageQuery, RoomPriceUpdate, RoomStatusUpdate, RoomVO } from '@/types/room';

export function createRoomApi(data: RoomForm) {
  return request<number>({ url: '/api/admin/room/create', method: 'post', data });
}

export function updateRoomApi(data: RoomForm) {
  return request<void>({ url: '/api/admin/room/update', method: 'post', data });
}

export function deleteRoomApi(data: IdPayload) {
  return request<void>({ url: '/api/admin/room/delete', method: 'post', data });
}

export function updateRoomPriceApi(data: RoomPriceUpdate) {
  return request<void>({ url: '/api/admin/room/update-price', method: 'post', data });
}

export function updateRoomStatusApi(data: RoomStatusUpdate) {
  return request<void>({ url: '/api/admin/room/update-status', method: 'post', data });
}

export function getRoomDetailApi(params: IdPayload) {
  return request<RoomVO>({ url: '/api/admin/room/detail', method: 'get', params });
}

export function getRoomPageApi(params: RoomPageQuery) {
  return request<PageResult<RoomVO>>({ url: '/api/admin/room/page', method: 'get', params });
}
