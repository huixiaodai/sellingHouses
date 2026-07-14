const ROLE_HOME_MAP = {
  SALES: '/pages/estate/index',
  CUSTOMER: '/pages/home/home'
};

export function getRoleHomePath(userInfo = {}) {
  if (ROLE_HOME_MAP[userInfo.roleCode]) {
    return ROLE_HOME_MAP[userInfo.roleCode];
  }
  if (userInfo.homePath && userInfo.homePath.startsWith('/pages/')) {
    return normalizeHomePath(userInfo.homePath);
  }
  return '/pages/home/home';
}

function normalizeHomePath(path) {
  const legacyMap = {
    '/pages/home/index': '/pages/home/home',
    '/pages/sale-control/index': '/pages/estate/index'
  };
  return legacyMap[path] || path;
}
