import type { PageQuery } from './common';

export interface AppointmentVO {
  id: number;
  estateId?: number;
  buildingId?: number;
  buildingName?: string;
  unitId?: number;
  unitName?: string;
  roomId?: number;
  roomNo?: string;
  userId?: number;
  customerUserId?: number;
  customerName?: string;
  salesUserId?: number;
  salesName?: string;
  appointmentTime?: string;
  contactName?: string;
  contactPhone?: string;
  remark?: string;
  cancelReason?: string;
  status: number;
  createTime?: string;
}

export interface AppointmentPageQuery extends PageQuery {
  estateId?: number | string;
  buildingId?: number | string;
  buildingName?: string;
  salesUserId?: number | string;
  salesName?: string;
  contactName?: string;
  contactPhone?: string;
  status?: number | '';
}

export interface AppointmentStatusUpdate {
  id: number;
  status: number;
  salesUserId?: number;
}

export interface SalesUserVO {
  id: number;
  username: string;
  realName?: string;
  phone?: string;
}
