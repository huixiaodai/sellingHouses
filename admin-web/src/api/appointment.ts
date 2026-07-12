import { request } from '@/utils/request';
import type { PageResult } from '@/types/common';
import type { AppointmentPageQuery, AppointmentStatusUpdate, AppointmentVO } from '@/types/appointment';

export function getAdminAppointmentPageApi(params: AppointmentPageQuery) {
  return request<PageResult<AppointmentVO>>({ url: '/api/admin/appointment/page', method: 'get', params });
}

export function updateAppointmentStatusApi(data: AppointmentStatusUpdate) {
  return request<void>({ url: '/api/admin/appointment/update-status', method: 'post', data });
}
