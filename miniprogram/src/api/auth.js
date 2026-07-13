import { request } from '../utils/request';

export function login(data) {
  return request({
    url: '/api/auth/login',
    method: 'POST',
    data
  });
}

export function register(data) {
  return request({
    url: '/api/auth/register',
    method: 'POST',
    data
  });
}

export function getCurrentUser() {
  return request({
    url: '/api/auth/current'
  });
}
