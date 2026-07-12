<template>
  <main class="login-page">
    <section class="login-visual">
      <div class="visual-panel">
        <span class="eyebrow">REAL ESTATE OPERATIONS</span>
        <h1>Selling Houses Admin</h1>
        <p>面向售楼业务的企业级后台，集中管理楼盘、楼栋、房源、公告与预约流程。</p>
      </div>
    </section>

    <section class="login-card">
      <div class="login-heading">
        <h2>登录管理后台</h2>
        <p>请输入管理员账号继续</p>
      </div>
      <el-form ref="formRef" :model="form" :rules="rules" size="large" @keyup.enter="handleLogin">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="账号" :prefix-icon="User" clearable />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            placeholder="密码"
            :prefix-icon="Lock"
            type="password"
            show-password
          />
        </el-form-item>
        <el-button class="login-button" type="primary" size="large" :loading="loading" @click="handleLogin">
          登录
        </el-button>
      </el-form>
    </section>
  </main>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage, type FormInstance, type FormRules } from 'element-plus';
import { Lock, User } from '@element-plus/icons-vue';
import { useUserStore } from '@/store/user';
import type { LoginRequest } from '@/types/auth';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();
const formRef = ref<FormInstance>();
const loading = ref(false);
const form = reactive<LoginRequest>({
  username: '',
  password: ''
});

const rules: FormRules<LoginRequest> = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
};

const handleLogin = async () => {
  await formRef.value?.validate();
  loading.value = true;
  try {
    await userStore.login(form);
    ElMessage.success('登录成功');
    router.replace(String(route.query.redirect || '/building'));
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped lang="scss">
.login-page {
  display: grid;
  min-height: 100vh;
  grid-template-columns: minmax(0, 1.2fr) minmax(420px, 0.8fr);
  background:
    linear-gradient(120deg, rgb(29 102 242 / 10%), transparent 36%),
    #f5f7fb;
}

.login-visual {
  display: flex;
  align-items: center;
  padding: 56px;
  background:
    linear-gradient(135deg, rgb(15 31 58 / 92%), rgb(24 64 121 / 88%)),
    radial-gradient(circle at 20% 20%, rgb(47 125 255 / 45%), transparent 32%);
  color: #fff;
}

.visual-panel {
  max-width: 620px;

  .eyebrow {
    color: #8fd3ff;
    font-size: 13px;
    font-weight: 700;
  }

  h1 {
    margin: 20px 0 18px;
    font-size: clamp(38px, 6vw, 72px);
    line-height: 1;
  }

  p {
    max-width: 520px;
    color: rgb(255 255 255 / 76%);
    font-size: 18px;
    line-height: 1.8;
  }
}

.login-card {
  align-self: center;
  width: min(420px, calc(100% - 40px));
  margin: 0 auto;
  padding: 36px;
  background: #fff;
  border: 1px solid var(--admin-border);
  border-radius: 8px;
  box-shadow: var(--admin-shadow);
}

.login-heading {
  margin-bottom: 28px;

  h2 {
    margin: 0;
    font-size: 26px;
  }

  p {
    margin: 8px 0 0;
    color: var(--admin-muted);
  }
}

.login-button {
  width: 100%;
}

@media (max-width: 900px) {
  .login-page {
    grid-template-columns: 1fr;
  }

  .login-visual {
    min-height: 280px;
    padding: 36px;
  }
}
</style>
