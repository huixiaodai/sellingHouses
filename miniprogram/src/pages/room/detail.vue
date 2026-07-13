<template>
  <view class="page">
    <view v-if="loading" class="state">加载中...</view>
    <view v-else-if="!room" class="state">房源不存在</view>

    <view v-else>
      <swiper
        v-if="carouselImages.length > 0"
        class="hero-swiper"
        indicator-dots
        circular
        autoplay
        :interval="4200"
        :duration="450"
        indicator-color="rgba(255, 250, 240, 0.62)"
        indicator-active-color="#fffaf0"
      >
        <swiper-item v-for="image in carouselImages" :key="image">
          <image class="hero-image" :src="image" mode="aspectFill" />
        </swiper-item>
      </swiper>
      <view v-else class="hero-swiper placeholder-cover">
        <text>{{ room.roomNo || '房源详情' }}</text>
      </view>

      <view class="content">
        <view class="title-row">
          <text class="title">{{ room.unitName || '楼栋' }} {{ room.roomNo }}</text>
          <text class="status-pill" :class="getRoomStatusClass(room.status)">
            {{ getRoomStatus(room.status) }}
          </text>
        </view>
        <text class="building">{{ room.buildingName || '-' }}</text>

        <view class="price-block">
          <text class="price">{{ formatPrice(room.price) }}</text>
          <text class="price-note">参考总价</text>
        </view>

        <view class="info-grid">
          <view class="info-item">
            <text class="info-label">面积</text>
            <text class="info-value">{{ room.area || '-' }}㎡</text>
          </view>
          <view class="info-item">
            <text class="info-label">户型</text>
            <text class="info-value">{{ room.layout || '-' }}</text>
          </view>
          <view class="info-item">
            <text class="info-label">楼层</text>
            <text class="info-value">{{ room.floorNo || '-' }}</text>
          </view>
          <view class="info-item">
            <text class="info-label">朝向</text>
            <text class="info-value">{{ room.orientation || '-' }}</text>
          </view>
          <view class="info-item">
            <text class="info-label">装修</text>
            <text class="info-value">{{ room.decoration || '-' }}</text>
          </view>
        </view>

        <view v-if="gallery.length > 0" class="section">
          <text class="section-title">房源图片</text>
          <image v-for="image in gallery" :key="image" class="detail-image" :src="image" mode="aspectFill" />
        </view>

        <view class="section">
          <text class="section-title">备注</text>
          <text class="remark">{{ room.remark || '暂无备注' }}</text>
        </view>
      </view>
    </view>

    <view v-if="room" class="action-bar">
      <button class="secondary-button" @tap="goAppointments">我的预约</button>
      <button class="primary-button" :disabled="!canBook" @tap="openAppointment">
        {{ canBook ? '预约看房' : '已售不可预约' }}
      </button>
    </view>

    <view v-if="appointmentVisible" class="modal-mask" @tap="closeAppointment">
      <view class="modal" @tap.stop>
        <view class="modal-header">
          <text class="modal-title">预约看房</text>
          <text class="modal-subtitle">{{ room?.unitName || '楼栋' }} {{ room?.roomNo || '' }}</text>
        </view>

        <view class="field">
          <text class="label">预约日期</text>
          <picker mode="date" :start="today" :value="form.date" @change="handleDateChange">
            <view class="picker-value">{{ form.date || '请选择日期' }}</view>
          </picker>
        </view>

        <view class="field">
          <text class="label">预约时间</text>
          <picker mode="time" :value="form.time" @change="handleTimeChange">
            <view class="picker-value">{{ form.time || '请选择时间' }}</view>
          </picker>
        </view>

        <view class="field">
          <text class="label">联系人</text>
          <input v-model.trim="form.contactName" class="input" placeholder="请输入联系人" placeholder-class="placeholder" />
        </view>

        <view class="field">
          <text class="label">联系电话</text>
          <input
            v-model.trim="form.contactPhone"
            class="input"
            type="number"
            maxlength="11"
            placeholder="请输入手机号"
            placeholder-class="placeholder"
          />
        </view>

        <view class="field">
          <text class="label">备注</text>
          <textarea v-model.trim="form.remark" class="textarea" maxlength="120" placeholder="可填写期望看房说明" />
        </view>

        <view class="modal-actions">
          <button class="ghost-button" @tap="closeAppointment">取消</button>
          <button class="primary-button modal-submit" :loading="submitting" :disabled="submitting" @tap="submitAppointment">
            提交预约
          </button>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed, reactive, ref } from 'vue';
import { onLoad, onShow } from '@dcloudio/uni-app';
import { createAppointment } from '../../api/appointment';
import { getRoomDetail } from '../../api/room';
import { requireLogin } from '../../utils/authGuard';
import { getUserInfo } from '../../utils/storage';
import { formatPrice, getRoomStatus, getRoomStatusClass, parseImageList } from '../../utils/format';

const roomId = ref(null);
const room = ref(null);
const loading = ref(false);
const appointmentVisible = ref(false);
const submitting = ref(false);
const form = reactive({
  date: '',
  time: '',
  contactName: '',
  contactPhone: '',
  remark: ''
});

const gallery = computed(() => parseImageList(room.value?.images));
const carouselImages = computed(() => {
  if (gallery.value.length > 0) {
    return gallery.value;
  }
  return room.value?.cover ? [room.value.cover] : [];
});
const canBook = computed(() => room.value?.status === 0);
const today = computed(() => formatDate(new Date()));

onLoad((options) => {
  roomId.value = options.id;
});

onShow(() => {
  if (!requireLogin()) {
    return;
  }
  loadDetail();
});

async function loadDetail() {
  if (!roomId.value || loading.value) {
    return;
  }
  loading.value = true;
  try {
    room.value = await getRoomDetail(roomId.value);
  } finally {
    loading.value = false;
  }
}

function openAppointment() {
  if (!canBook.value) {
    uni.showToast({ title: '已售房源不能预约', icon: 'none' });
    return;
  }
  const userInfo = getUserInfo() || {};
  const next = getDefaultAppointmentDate();
  form.date = formatDate(next);
  form.time = '10:00';
  form.contactName = userInfo.realName || userInfo.username || '';
  form.contactPhone = '';
  form.remark = '';
  appointmentVisible.value = true;
}

function closeAppointment() {
  if (!submitting.value) {
    appointmentVisible.value = false;
  }
}

function handleDateChange(event) {
  form.date = event.detail.value;
}

function handleTimeChange(event) {
  form.time = event.detail.value;
}

function validateForm() {
  if (!form.date || !form.time) {
    uni.showToast({ title: '请选择预约时间', icon: 'none' });
    return false;
  }
  if (!form.contactName) {
    uni.showToast({ title: '请输入联系人', icon: 'none' });
    return false;
  }
  if (!/^1[3-9]\d{9}$/.test(form.contactPhone)) {
    uni.showToast({ title: '手机号格式不正确', icon: 'none' });
    return false;
  }
  if (new Date(`${form.date}T${form.time}:00`).getTime() <= Date.now()) {
    uni.showToast({ title: '预约时间必须晚于当前时间', icon: 'none' });
    return false;
  }
  return true;
}

async function submitAppointment() {
  if (!validateForm() || submitting.value) {
    return;
  }
  submitting.value = true;
  try {
    await createAppointment({
      roomId: Number(roomId.value),
      appointmentTime: `${form.date} ${form.time}:00`,
      contactName: form.contactName,
      contactPhone: form.contactPhone,
      remark: form.remark
    });
    appointmentVisible.value = false;
    uni.showModal({
      title: '预约成功',
      content: '已为你提交看房预约，可在我的预约中查看。',
      confirmText: '查看预约',
      cancelText: '留在当前',
      success: (res) => {
        if (res.confirm) {
          goAppointments();
        }
      }
    });
  } finally {
    submitting.value = false;
  }
}

function goAppointments() {
  uni.navigateTo({
    url: '/pages/appointment/list'
  });
}

function getDefaultAppointmentDate() {
  const date = new Date();
  date.setDate(date.getDate() + 1);
  return date;
}

function formatDate(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding-bottom: 140rpx;
  background: #f6f3ec;
}

.hero-swiper {
  width: 100%;
  height: 430rpx;
}

.hero-image {
  width: 100%;
  height: 430rpx;
}

.placeholder-cover {
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #263d33, #9b6b26);
  color: #fff8e8;
  font-size: 32rpx;
  font-weight: 800;
}

.content {
  padding: 34rpx 32rpx 56rpx;
}

.title-row {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 18rpx;
}

.title {
  flex: 1;
  min-width: 0;
  color: #1d2a24;
  font-size: 42rpx;
  font-weight: 900;
  line-height: 52rpx;
}

.building {
  display: block;
  margin-top: 12rpx;
  color: #756b5f;
  font-size: 26rpx;
}

.status-pill {
  flex-shrink: 0;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  font-weight: 800;
}

.status-available {
  color: #1f6b45;
  background: #e5f4ea;
}

.status-sold {
  color: #9a342f;
  background: #f7dedb;
}

.status-unknown {
  color: #6b7280;
  background: #ece8df;
}

.price-block,
.info-item {
  border-radius: 8rpx;
  background: #fffaf0;
  border: 1rpx solid rgba(62, 55, 46, 0.1);
}

.price-block {
  margin-top: 30rpx;
  padding: 28rpx;
}

.price {
  display: block;
  color: #9b3f22;
  font-size: 44rpx;
  font-weight: 900;
}

.price-note {
  display: block;
  margin-top: 8rpx;
  color: #8c8173;
  font-size: 24rpx;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 18rpx;
  margin-top: 24rpx;
}

.info-item {
  padding: 24rpx;
}

.info-label {
  display: block;
  color: #8c8173;
  font-size: 24rpx;
}

.info-value {
  display: block;
  margin-top: 8rpx;
  color: #1f352c;
  font-size: 28rpx;
  font-weight: 800;
}

.section {
  margin-top: 36rpx;
}

.section-title {
  display: block;
  color: #1d2a24;
  font-size: 32rpx;
  font-weight: 800;
  margin-bottom: 18rpx;
}

.detail-image {
  width: 100%;
  height: 360rpx;
  margin-bottom: 18rpx;
  border-radius: 8rpx;
}

.remark {
  display: block;
  color: #4d463b;
  font-size: 28rpx;
  line-height: 46rpx;
}

.state {
  min-height: 70vh;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #756b5f;
  font-size: 28rpx;
}

.action-bar {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 20;
  display: grid;
  grid-template-columns: 220rpx 1fr;
  gap: 18rpx;
  padding: 18rpx 28rpx calc(18rpx + env(safe-area-inset-bottom));
  background: rgba(246, 243, 236, 0.96);
  border-top: 1rpx solid rgba(62, 55, 46, 0.1);
}

.primary-button,
.secondary-button,
.ghost-button {
  height: 88rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
  font-weight: 800;
  line-height: 88rpx;
}

.primary-button {
  background: #1f352c;
  color: #fff8e8;
}

.primary-button[disabled] {
  background: #9aa39d;
  color: #fff8e8;
}

.secondary-button,
.ghost-button {
  background: #fffaf0;
  color: #1f352c;
  border: 1rpx solid rgba(31, 53, 44, 0.18);
}

.modal-mask {
  position: fixed;
  inset: 0;
  z-index: 40;
  display: flex;
  align-items: flex-end;
  background: rgba(24, 32, 28, 0.42);
}

.modal {
  width: 100%;
  max-height: 88vh;
  overflow-y: auto;
  padding: 34rpx 32rpx calc(34rpx + env(safe-area-inset-bottom));
  border-radius: 8rpx 8rpx 0 0;
  background: #f6f3ec;
}

.modal-header {
  margin-bottom: 26rpx;
}

.modal-title {
  display: block;
  color: #1d2a24;
  font-size: 38rpx;
  font-weight: 900;
}

.modal-subtitle {
  display: block;
  margin-top: 8rpx;
  color: #756b5f;
  font-size: 26rpx;
}

.field + .field {
  margin-top: 22rpx;
}

.label {
  display: block;
  margin-bottom: 12rpx;
  color: #4d463b;
  font-size: 26rpx;
  font-weight: 700;
}

.input,
.picker-value,
.textarea {
  box-sizing: border-box;
  width: 100%;
  border-radius: 8rpx;
  background: #fffaf0;
  border: 1rpx solid rgba(44, 38, 31, 0.1);
  color: #1f2933;
  font-size: 28rpx;
}

.input,
.picker-value {
  height: 88rpx;
  padding: 0 24rpx;
  line-height: 88rpx;
}

.textarea {
  min-height: 132rpx;
  padding: 22rpx 24rpx;
}

.placeholder {
  color: #b5ab9b;
}

.modal-actions {
  display: grid;
  grid-template-columns: 180rpx 1fr;
  gap: 18rpx;
  margin-top: 30rpx;
}

.modal-submit {
  margin: 0;
}
</style>
