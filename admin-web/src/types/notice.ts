import type { PageQuery } from './common';

export interface NoticeVO {
  id: number;
  title: string;
  content: string;
  targetRole: string;
  publisherName?: string;
  publishTime?: string;
}

export interface NoticePageQuery extends PageQuery {
  title?: string;
}

export interface NoticeCreateForm {
  title: string;
  content: string;
  targetRoleCode: string;
}
