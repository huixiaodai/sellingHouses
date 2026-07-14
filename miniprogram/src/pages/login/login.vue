<template>
  <view class="page">
    <view class="brand">
      <view class="brand-mark">SH</view>
      <text class="brand-title">售楼处</text>
      <text class="brand-subtitle">房源与预约服务</text>
    </view>

    <view class="panel">
      <view class="field">
        <text class="label">账号</text>
        <input
          v-model.trim="form.username"
          class="input"
          placeholder="请输入账号"
          placeholder-class="placeholder"
          confirm-type="next"
        />
      </view>

      <view class="field">
        <text class="label">密码</text>
        <input
          v-model="form.password"
          class="input"
          password
          placeholder="请输入密码"
          placeholder-class="placeholder"
          confirm-type="done"
          @confirm="handleLogin"
        />
      </view>

      <button class="primary-button" :loading="loading" :disabled="loading" @tap="handleLogin">
        登录
      </button>

      <view class="actions">
        <text class="link" @tap="goRegister">立即注册</text>
        <text class="muted">忘记密码</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { login } from '../../api/auth';
import { getRoleHomePath } from '../../utils/roleHome';
import { setToken, setUserInfo } from '../../utils/storage';

const loading = ref(false);
const form = reactive({
  username: '',
  password: ''
});

function validateForm() {
  if (!form.username) {
    uni.showToast({ title: '请输入账号', icon: 'none' });
    return false;
  }
  if (!form.password) {
    uni.showToast({ title: '请输入密码', icon: 'none' });
    return false;
  }
  return true;
}

async function handleLogin() {
  if (!validateForm() || loading.value) {
    return;
  }
  loading.value = true;
  try {
    const data = await login({
      username: form.username,
      password: form.password
    });
    setToken(data.token);
    const userInfo = {
      userId: data.userId,
      username: data.username,
      realName: data.realName,
      roleCode: data.roleCode,
      homePath: data.homePath
    };
    setUserInfo(userInfo);
    uni.showToast({ title: '登录成功', icon: 'success' });
    uni.reLaunch({
      url: getRoleHomePath(userInfo)
    });
  } finally {
    loading.value = false;
  }
}

function goRegister() {
  uni.navigateTo({
    url: '/pages/register/register'
  });
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding: 96rpx 40rpx 48rpx;
  background:
    linear-gradient(145deg, rgba(217, 190, 144, 0.34), rgba(246, 243, 236, 0) 38%),
    #f6f3ec;
}

.brand {
  margin-bottom: 64rpx;
}

.brand-mark {
  width: 112rpx;
  height: 112rpx;
  border-radius: 28rpx;
  background: #19231f;
  color: #f5d18a;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36rpx;
  font-weight: 700;
  margin-bottom: 28rpx;
  box-shadow: 0 20rpx 48rpx rgba(25, 35, 31, 0.18);
}

.brand-title {
  display: block;
  color: #1d2a24;
  font-size: 56rpx;
  line-height: 1.1;
  font-weight: 800;
}

.brand-subtitle {
  display: block;
  margin-top: 12rpx;
  color: #7d7263;
  font-size: 28rpx;
}

.panel {
  padding: 40rpx;
  border: 1rpx solid rgba(62, 55, 46, 0.1);
  border-radius: 8rpx;
  background: rgba(255, 252, 245, 0.86);
  box-shadow: 0 24rpx 72rpx rgba(49, 42, 32, 0.1);
}

.field + .field {
  margin-top: 30rpx;
}

.label {
  display: block;
  margin-bottom: 14rpx;
  color: #4d463b;
  font-size: 26rpx;
  font-weight: 600;
}

.input {
  height: 96rpx;
  padding: 0 28rpx;
  border-radius: 8rpx;
  background: #fffaf0;
  border: 1rpx solid rgba(44, 38, 31, 0.1);
  color: #1f2933;
  font-size: 30rpx;
}

.placeholder {
  color: #b5ab9b;
}

.primary-button {
  width: 100%;
  height: 96rpx;
  margin-top: 42rpx;
  border-radius: 8rpx;
  background: #1f352c;
  color: #fff8e8;
  font-size: 30rpx;
  font-weight: 700;
  line-height: 96rpx;
}

.primary-button[disabled] {
  background: #8a968f;
  color: #fff8e8;
}

.actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 32rpx;
  font-size: 26rpx;
}

.link {
  color: #8b5f20;
  font-weight: 700;
}

.muted {
  color: #8c8173;
}
</style>
