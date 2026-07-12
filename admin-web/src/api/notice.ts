import { request } from '@/utils/request';
import type { IdPayload, PageResult } from '@/types/common';
import type { NoticeCreateForm, NoticePageQuery, NoticeVO } from '@/types/notice';

export function getNoticePageApi(params: NoticePageQuery) {
  return request<PageResult<NoticeVO>>({ url: '/api/admin/notice/page', method: 'get', params });
}

export function getNoticeDetailApi(params: IdPayload) {
  return request<NoticeVO>({ url: '/api/admin/notice/detail', method: 'get', params });
}

export function createNoticeApi(data: NoticeCreateForm) {
  return request<number>({ url: '/api/admin/notice/create', method: 'post', data });
}

export function deleteNoticeApi(data: IdPayload) {
  return request<void>({ url: '/api/admin/notice/delete', method: 'post', data });
}
