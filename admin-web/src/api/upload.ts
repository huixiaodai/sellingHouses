import { request } from '@/utils/request';
import type { UploadImageVO } from '@/types/upload';

export function uploadImageApi(file: File) {
  const formData = new FormData();
  formData.append('file', file);
  return request<UploadImageVO>({
    url: '/api/admin/upload/image',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}
