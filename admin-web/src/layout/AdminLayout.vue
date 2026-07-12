<template>
  <div class="admin-layout">
    <aside class="admin-sider" :class="{ collapsed }">
      <div class="brand">
        <div class="brand-mark">SH</div>
        <div v-show="!collapsed" class="brand-copy">
          <strong>Selling Houses</strong>
          <span>Admin Console</span>
        </div>
      </div>
      <el-menu
        class="layout-menu"
        :collapse="collapsed"
        :default-active="activePath"
        router
      >
        <el-menu-item v-for="route in menuRoutes" :key="route.path" :index="`/${route.path}`">
          <el-icon>
            <component :is="route.meta?.icon" />
          </el-icon>
          <span>{{ route.meta?.title }}</span>
        </el-menu-item>
      </el-menu>
    </aside>

    <section class="admin-shell">
      <header class="admin-header">
        <div class="header-left">
          <el-button text :icon="collapsed ? Expand : Fold" @click="collapsed = !collapsed" />
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>Selling Houses Admin</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-user">
          <el-avatar :size="32" class="user-avatar">{{ avatarText }}</el-avatar>
          <div class="user-meta">
            <strong>{{ userStore.userInfo?.realName || userStore.userInfo?.username || '管理员' }}</strong>
            <span>{{ roleName }}</span>
          </div>
          <el-button text type="danger" :icon="SwitchButton" @click="handleLogout">退出</el-button>
        </div>
      </header>

      <main class="admin-main">
        <RouterView />
      </main>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { Expand, Fold, SwitchButton } from '@element-plus/icons-vue';
import { menuRoutes } from '@/router';
import { useUserStore } from '@/store/user';

const collapsed = ref(false);
const route = useRoute();
const router = useRouter();
const userStore = useUserStore();

const activePath = computed(() => route.path);
const currentTitle = computed(() => String(route.meta.title || '工作台'));
const avatarText = computed(() => (userStore.userInfo?.realName || userStore.userInfo?.username || 'A').slice(0, 1));
const roleName = computed(() => {
  const role = userStore.userInfo?.roleCode;
  if (role === 'ADMIN') {
    return '超级管理员';
  }
  if (role === 'SALES') {
    return '销售';
  }
  if (role === 'CUSTOMER') {
    return '购房用户';
  }
  return role || '未识别角色';
});

const handleLogout = async () => {
  await userStore.logout();
  ElMessage.success('已退出登录');
  router.replace('/login');
};
</script>

<style scoped lang="scss">
.admin-layout {
  display: flex;
  min-height: 100vh;
  background: var(--admin-bg);
}

.admin-sider {
  position: sticky;
  top: 0;
  z-index: 10;
  flex: 0 0 232px;
  width: 232px;
  height: 100vh;
  background: #0f1f3a;
  color: #fff;
  transition: width 0.2s ease, flex-basis 0.2s ease;

  &.collapsed {
    flex-basis: 72px;
    width: 72px;
  }
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  height: 64px;
  padding: 0 18px;
  border-bottom: 1px solid rgb(255 255 255 / 10%);
}

.brand-mark {
  display: grid;
  flex: 0 0 36px;
  width: 36px;
  height: 36px;
  place-items: center;
  border-radius: 8px;
  background: linear-gradient(135deg, #2f7dff, #13c2c2);
  font-weight: 800;
}

.brand-copy {
  display: flex;
  min-width: 0;
  flex-direction: column;

  strong {
    line-height: 1.1;
  }

  span {
    margin-top: 3px;
    color: rgb(255 255 255 / 62%);
    font-size: 12px;
  }
}

.layout-menu {
  border-right: 0;
  background: transparent;

  :deep(.el-menu-item) {
    margin: 6px 10px;
    border-radius: 8px;
    color: rgb(255 255 255 / 72%);
  }

  :deep(.el-menu-item.is-active) {
    background: var(--admin-primary);
    color: #fff;
  }

  :deep(.el-menu-item:hover) {
    background: rgb(255 255 255 / 10%);
    color: #fff;
  }
}

.admin-shell {
  display: flex;
  min-width: 0;
  flex: 1;
  flex-direction: column;
}

.admin-header {
  position: sticky;
  top: 0;
  z-index: 9;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
  padding: 0 22px;
  background: rgb(255 255 255 / 92%);
  border-bottom: 1px solid var(--admin-border);
  backdrop-filter: blur(14px);
}

.header-left,
.header-user {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  background: var(--admin-primary);
  font-weight: 700;
}

.user-meta {
  display: flex;
  flex-direction: column;
  font-size: 13px;

  span {
    color: var(--admin-muted);
    font-size: 12px;
  }
}

.admin-main {
  padding: 22px;
}

@media (max-width: 768px) {
  .admin-sider {
    position: fixed;
    transform: translateX(-100%);

    &.collapsed {
      transform: translateX(0);
    }
  }

  .user-meta {
    display: none;
  }
}
</style>
