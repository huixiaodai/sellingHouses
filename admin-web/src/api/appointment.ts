import { request } from '@/utils/request';
import type { PageResult } from '@/types/common';
import type { AppointmentPageQuery, AppointmentStatusUpdate, AppointmentVO, SalesUserVO } from '@/types/appointment';

function normalizeAppointmentQuery(params: AppointmentPageQuery) {
  return Object.fromEntries(
    Object.entries(params).filter(([, value]) => value !== '' && value !== undefined && value !== null)
  );
}

export function getAdminAppointmentPageApi(params: AppointmentPageQuery) {
  return request<PageResult<AppointmentVO>>({
    url: '/api/admin/appointment/page',
    method: 'get',
    params: normalizeAppointmentQuery(params)
  });
}

export function updateAppointmentStatusApi(data: AppointmentStatusUpdate) {
  return request<void>({ url: '/api/admin/appointment/update-status', method: 'post', data });
}

export function getSalesUserListApi() {
  return request<SalesUserVO[]>({ url: '/api/admin/appointment/sales-list', method: 'get' });
}
