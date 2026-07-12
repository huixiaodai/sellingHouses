import type { PageQuery } from './common';

export interface RoomVO {
  id: number;
  buildingId: number;
  buildingName?: string;
  unitId: number;
  unitName?: string;
  roomNo: string;
  floorNo: number;
  area: number;
  price: number;
  cover?: string;
  images?: string;
  layout?: string;
  orientation?: string;
  decoration?: string;
  status: number;
  remark?: string;
  createTime?: string;
}

export interface RoomPageQuery extends PageQuery {
  buildingId?: number | '';
  unitId?: number | '';
  floorNo?: number | '';
  roomNo?: string;
  status?: number | '';
}

export interface RoomForm {
  id?: number;
  buildingId?: number;
  unitId?: number;
  roomNo: string;
  floorNo?: number;
  area?: number;
  price?: number;
  cover?: string;
  images?: string;
  layout?: string;
  orientation?: string;
  decoration?: string;
  status: number;
  remark?: string;
}

export interface RoomPriceUpdate {
  id: number;
  price: number;
}

export interface RoomStatusUpdate {
  id: number;
  status: number;
}
