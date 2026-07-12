export interface ApiResult<T> {
  code: number;
  message: string;
  data: T;
  timestamp: number;
}

export interface PageResult<T> {
  records: T[];
  pageNo: number;
  pageSize: number;
  total: number;
}

export interface PageQuery {
  pageNo: number;
  pageSize: number;
}

export interface IdPayload {
  id: number;
}

export type StatusCode = 0 | 1;
