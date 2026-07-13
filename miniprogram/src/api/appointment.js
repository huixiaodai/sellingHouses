import { request } from '../utils/request';

export function createAppointment(data) {
  return request({
    url: '/api/user/appointment/create',
    method: 'POST',
    data
  });
}

export function getMyAppointmentPage(params) {
  return request({
    url: '/api/user/appointment/page',
    data: params
  });
}

export function cancelAppointment(data) {
  return request({
    url: '/api/user/appointment/cancel',
    method: 'POST',
    data
  });
}
