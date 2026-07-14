import { getRoleHomePath } from './roleHome';
import { getCurrentUser } from '../api/auth';
import { getUserInfo, setUserInfo } from './storage';

export function redirectByRole() {
  const userInfo = getUserInfo() || {};
  uni.reLaunch({
    url: getRoleHomePath(userInfo)
  });
}

export function requireSalesRole() {
  const userInfo = getUserInfo() || {};
  if (userInfo.roleCode === 'SALES') {
    return true;
  }
  uni.reLaunch({
    url: getRoleHomePath(userInfo)
  });
  return false;
}

export function redirectSalesFromCustomerHome() {
  const userInfo = getUserInfo() || {};
  if (userInfo.roleCode === 'SALES') {
    uni.reLaunch({
      url: getRoleHomePath(userInfo)
    });
    return true;
  }
  return false;
}

export async function redirectSalesFromCustomerHomeByServer() {
  const currentUser = await getCurrentUser();
  const storedUser = getUserInfo() || {};
  const nextUser = {
    ...storedUser,
    ...currentUser
  };
  setUserInfo(nextUser);
  if (nextUser.roleCode === 'SALES') {
    uni.reLaunch({
      url: getRoleHomePath(nextUser)
    });
    return true;
  }
  return false;
}
