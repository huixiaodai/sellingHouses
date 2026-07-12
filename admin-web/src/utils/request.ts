import axios, { type AxiosError, type AxiosRequestConfig } from 'axios';
import { ElMessage } from 'element-plus';
import router from '@/router';
import { removeToken, getToken } from './token';
import type { ApiResult } from '@/types/common';

const service = axios.create({
  baseURL: '',
  timeout: 15000
});

service.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

service.interceptors.response.use(
  (response) => response,
  (error: AxiosError<ApiResult<unknown>>) => {
    const status = error.response?.status;
    const message = error.response?.data?.message || error.message || '网络异常';
    if (status === 401) {
      removeToken();
      router.replace('/login');
      ElMessage.error('登录已过期，请重新登录');
    } else {
      ElMessage.error(message);
    }
    return Promise.reject(error);
  }
);

export async function request<T>(config: AxiosRequestConfig): Promise<T> {
  const response = await service(config);
  const result = response.data as ApiResult<T>;
  if (result.code === 200) {
    return result.data;
  }
  ElMessage.error(result.message || '请求失败');
  if (result.code === 401) {
    removeToken();
    router.replace('/login');
  }
  return Promise.reject(new Error(result.message || '请求失败'));
}

export default service;
