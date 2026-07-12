export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginVO {
  token: string;
  userId: number;
  username: string;
  realName: string;
  roleCode: string;
  homePath: string;
}

export interface UserInfo {
  userId: number;
  username: string;
  realName: string;
  phone?: string;
  roleCode: string;
  status?: number;
}
