import { clearLoginState, getToken } from './storage';

const BASE_URL = 'http://localhost:8080';

function redirectToLogin() {
  clearLoginState();
  const pages = getCurrentPages();
  const current = pages[pages.length - 1];
  if (current && current.route === 'pages/login/login') {
    return;
  }
  uni.redirectTo({
    url: '/pages/login/login'
  });
}

export function request(options) {
  const token = getToken();
  const headers = {
    'Content-Type': 'application/json',
    ...(options.header || {})
  };
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  return new Promise((resolve, reject) => {
    uni.request({
      url: `${BASE_URL}${options.url}`,
      method: options.method || 'GET',
      data: options.data || {},
      header: headers,
      success: (response) => {
        const { statusCode, data } = response;
        if (statusCode === 401) {
          redirectToLogin();
          reject(data);
          return;
        }
        if (statusCode < 200 || statusCode >= 300) {
          uni.showToast({
            title: data?.message || '请求失败',
            icon: 'none'
          });
          reject(data);
          return;
        }
        if (data && data.code !== 200) {
          uni.showToast({
            title: data.message || '操作失败',
            icon: 'none'
          });
          reject(data);
          return;
        }
        resolve(data ? data.data : null);
      },
      fail: (error) => {
        uni.showToast({
          title: '网络异常',
          icon: 'none'
        });
        reject(error);
      }
    });
  });
}
