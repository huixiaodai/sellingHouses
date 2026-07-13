const TOKEN_KEY = 'SELLING_HOUSES_TOKEN';
const USER_INFO_KEY = 'SELLING_HOUSES_USER_INFO';

export function getToken() {
  return uni.getStorageSync(TOKEN_KEY) || '';
}

export function setToken(token) {
  uni.setStorageSync(TOKEN_KEY, token);
}

export function getUserInfo() {
  return uni.getStorageSync(USER_INFO_KEY) || null;
}

export function setUserInfo(userInfo) {
  uni.setStorageSync(USER_INFO_KEY, userInfo);
}

export function clearLoginState() {
  uni.removeStorageSync(TOKEN_KEY);
  uni.removeStorageSync(USER_INFO_KEY);
}
