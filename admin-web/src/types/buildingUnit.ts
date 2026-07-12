import type { PageQuery } from './common';

export interface BuildingUnitVO {
  id: number;
  buildingId: number;
  buildingName?: string;
  name: string;
  sortNo?: number;
  status: number;
  createTime?: string;
}

export interface BuildingUnitPageQuery extends PageQuery {
  buildingId?: number | '';
  name?: string;
  status?: number | '';
}

export interface BuildingUnitForm {
  id?: number;
  buildingId?: number;
  name: string;
  sortNo?: number;
  status: number;
}

export interface BuildingUnitStatusUpdate {
  id: number;
  status: number;
}
