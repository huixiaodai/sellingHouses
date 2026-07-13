<template>
  <view class="page">
    <view class="topbar">
      <text class="back" @tap="goBack">‹</text>
      <text class="title">创建账号</text>
    </view>

    <view class="panel">
      <view class="field">
        <text class="label">账号</text>
        <input v-model.trim="form.username" class="input" placeholder="请输入登录账号" placeholder-class="placeholder" />
      </view>

      <view class="field">
        <text class="label">手机号</text>
        <input
          v-model.trim="form.phone"
          class="input"
          type="number"
          maxlength="11"
          placeholder="请输入手机号"
          placeholder-class="placeholder"
        />
      </view>

      <view class="field">
        <text class="label">密码</text>
        <input v-model="form.password" class="input" password placeholder="请输入密码" placeholder-class="placeholder" />
      </view>

      <view class="field">
        <text class="label">确认密码</text>
        <input
          v-model="form.confirmPassword"
          class="input"
          password
          placeholder="请再次输入密码"
          placeholder-class="placeholder"
          @confirm="handleRegister"
        />
      </view>

      <button class="primary-button" :loading="loading" :disabled="loading" @tap="handleRegister">
        注册
      </button>
    </view>
  </view>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { register } from '../../api/auth';

const loading = ref(false);
const form = reactive({
  username: '',
  phone: '',
  password: '',
  confirmPassword: ''
});

function validateForm() {
  if (!form.username) {
    uni.showToast({ title: '请输入账号', icon: 'none' });
    return false;
  }
  if (!/^1[3-9]\d{9}$/.test(form.phone)) {
    uni.showToast({ title: '手机号格式不正确', icon: 'none' });
    return false;
  }
  if (form.password.length < 6 || form.password.length > 32) {
    uni.showToast({ title: '密码长度为6到32位', icon: 'none' });
    return false;
  }
  if (form.password !== form.confirmPassword) {
    uni.showToast({ title: '两次密码不一致', icon: 'none' });
    return false;
  }
  return true;
}

async function handleRegister() {
  if (!validateForm() || loading.value) {
    return;
  }
  loading.value = true;
  try {
    await register({
      username: form.username,
      phone: form.phone,
      password: form.password,
      confirmPassword: form.confirmPassword
    });
    uni.showToast({ title: '注册成功', icon: 'success' });
    setTimeout(() => {
      uni.redirectTo({
        url: '/pages/login/login'
      });
    }, 600);
  } finally {
    loading.value = false;
  }
}

function goBack() {
  uni.navigateBack({
    delta: 1,
    fail: () => {
      uni.redirectTo({ url: '/pages/login/login' });
    }
  });
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding: 72rpx 40rpx 48rpx;
  background:
    linear-gradient(160deg, rgba(31, 53, 44, 0.12), rgba(246, 243, 236, 0) 42%),
    #f6f3ec;
}

.topbar {
  height: 80rpx;
  display: flex;
  align-items: center;
  margin-bottom: 48rpx;
}

.back {
  width: 72rpx;
  color: #1f352c;
  font-size: 58rpx;
  line-height: 72rpx;
}

.title {
  color: #1d2a24;
  font-size: 44rpx;
  font-weight: 800;
}

.panel {
  padding: 38rpx;
  border-radius: 8rpx;
  background: rgba(255, 252, 245, 0.9);
  border: 1rpx solid rgba(62, 55, 46, 0.1);
  box-shadow: 0 24rpx 72rpx rgba(49, 42, 32, 0.1);
}

.field + .field {
  margin-top: 28rpx;
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
</style>
