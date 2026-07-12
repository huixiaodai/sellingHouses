import type { PageQuery } from './common';

export interface BuildingVO {
  id: number;
  name: string;
  cover?: string;
  bannerImages?: string;
  developer?: string;
  address: string;
  description?: string;
  openingTime?: string;
  deliveryTime?: string;
  status: number;
  createTime?: string;
}

export interface BuildingPageQuery extends PageQuery {
  name?: string;
  status?: number | '';
}

export interface BuildingForm {
  id?: number;
  name: string;
  cover?: string;
  bannerImages?: string;
  developer?: string;
  address: string;
  description?: string;
  openingTime?: string;
  deliveryTime?: string;
  status: number;
}

export interface BuildingStatusUpdate {
  id: number;
  status: number;
}
