import { getToken } from './storage';

export function requireLogin() {
  if (getToken()) {
    return true;
  }
  uni.redirectTo({
    url: '/pages/login/login'
  });
  return false;
}
