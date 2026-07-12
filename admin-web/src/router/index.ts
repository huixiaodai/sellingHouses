import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import {
  Bell,
  House,
  OfficeBuilding,
  Tickets,
  Van,
  Warning
} from '@element-plus/icons-vue';
import AdminLayout from '@/layout/AdminLayout.vue';
import { useUserStore } from '@/store/user';
import { getToken } from '@/utils/token';

export const menuRoutes: RouteRecordRaw[] = [
  {
    path: 'building',
    name: 'Building',
    component: () => import('@/views/building/BuildingList.vue'),
    meta: { title: '楼盘管理', icon: House }
  },
  {
    path: 'building-unit',
    name: 'BuildingUnit',
    component: () => import('@/views/building-unit/BuildingUnitList.vue'),
    meta: { title: '楼栋管理', icon: OfficeBuilding }
  },
  {
    path: 'room',
    name: 'Room',
    component: () => import('@/views/room/RoomList.vue'),
    meta: { title: '房源管理', icon: Van }
  },
  {
    path: 'notice',
    name: 'Notice',
    component: () => import('@/views/notice/NoticeList.vue'),
    meta: { title: '公告管理', icon: Bell }
  },
  {
    path: 'appointment',
    name: 'Appointment',
    component: () => import('@/views/appointment/AppointmentList.vue'),
    meta: { title: '预约管理', icon: Tickets }
  }
];

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/LoginPage.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: AdminLayout,
    redirect: '/building',
    children: menuRoutes
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/error/NotFound.vue'),
    meta: { title: '404', icon: Warning }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach(async (to) => {
  const userStore = useUserStore();
  const token = getToken();

  if (to.path === '/login') {
    return token ? '/building' : true;
  }

  if (!token) {
    return { path: '/login', query: { redirect: to.fullPath } };
  }

  if (!userStore.userInfo) {
    try {
      await userStore.getCurrentUser();
    } catch {
      userStore.clearLoginState();
      return '/login';
    }
  }

  return true;
});

export default router;
