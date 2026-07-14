import { request } from '../utils/request';

export function getEstateList() {
  return request({
    url: '/api/estate/list'
  });
}
