import type { PageQuery } from './common';

export interface AppointmentVO {
  id: number;
  estateId?: number;
  buildingId?: number;
  buildingName?: string;
  roomId?: number;
  roomNo?: string;
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
  buildingId?: number | '';
  salesUserId?: number | string;
  status?: number | '';
}

export interface AppointmentStatusUpdate {
  id: number;
  status: number;
  salesUserId?: number;
}
