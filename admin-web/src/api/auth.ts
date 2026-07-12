import { request } from '@/utils/request';
import type { LoginRequest, LoginVO, UserInfo } from '@/types/auth';

export function loginApi(data: LoginRequest) {
  return request<LoginVO>({
    url: '/api/auth/login',
    method: 'post',
    data
  });
}

export function currentUserApi() {
  return request<UserInfo>({
    url: '/api/auth/current',
    method: 'get'
  });
}

export function logoutApi() {
  return request<void>({
    url: '/api/auth/logout',
    method: 'post'
  });
}
