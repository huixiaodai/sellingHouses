<template>
  <view class="page">
    <view class="header">
      <text class="eyebrow">APPOINTMENTS</text>
      <text class="title">我的预约</text>
      <text class="subtitle">查看看房预约记录，已预约的记录可在看房前取消。</text>
    </view>

    <scroll-view scroll-x class="status-scroll">
      <view class="status-tabs">
        <text
          v-for="item in statusOptions"
          :key="String(item.value)"
          class="status-tab"
          :class="{ active: currentStatus === item.value }"
          @tap="changeStatus(item.value)"
        >
          {{ item.label }}
        </text>
      </view>
    </scroll-view>

    <view v-if="loading && appointments.length === 0" class="state">加载中...</view>

    <view v-else-if="appointments.length === 0" class="state">
      <text class="state-title">暂无预约</text>
      <text class="state-desc">去房源详情页预约一次看房吧</text>
    </view>

    <view v-else class="appointment-list">
      <view v-for="item in appointments" :key="item.id" class="appointment-card">
        <view class="card-top">
          <view class="room-block">
            <text class="building">{{ item.buildingName || '-' }}</text>
            <text class="room">{{ item.unitName || '楼栋' }} {{ item.roomNo || '-' }}</text>
          </view>
          <text class="status-pill" :class="getAppointmentStatusClass(item.status)">
            {{ getAppointmentStatus(item.status) }}
          </text>
        </view>

        <view class="info-row">
          <text class="info-label">预约时间</text>
          <text class="info-value">{{ formatDateTime(item.appointmentTime) }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">联系人</text>
          <text class="info-value">{{ item.contactName || '-' }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">联系电话</text>
          <text class="info-value">{{ item.contactPhone || '-' }}</text>
        </view>
        <view v-if="item.remark" class="info-row">
          <text class="info-label">备注</text>
          <text class="info-value">{{ item.remark }}</text>
        </view>
        <view v-if="item.cancelReason" class="info-row">
          <text class="info-label">取消原因</text>
          <text class="info-value">{{ item.cancelReason }}</text>
        </view>

        <view class="card-actions">
          <button class="ghost-button" @tap="goRoom(item.roomId)">查看房源</button>
          <button v-if="item.status === 1" class="cancel-button" @tap="cancelItem(item)">取消预约</button>
        </view>
      </view>
    </view>

    <view v-if="appointments.length > 0" class="footer-state">
      {{ hasMore ? (loading ? '加载中...' : '上拉加载更多') : '已经到底了' }}
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { onPullDownRefresh, onReachBottom, onShow } from '@dcloudio/uni-app';
import { cancelAppointment, getMyAppointmentPage } from '../../api/appointment';
import { requireLogin } from '../../utils/authGuard';
import {
  APPOINTMENT_STATUS_OPTIONS,
  formatDateTime,
  getAppointmentStatus,
  getAppointmentStatusClass
} from '../../utils/format';

const statusOptions = APPOINTMENT_STATUS_OPTIONS;
const currentStatus = ref('');
const pageNo = ref(1);
const pageSize = 10;
const total = ref(0);
const loading = ref(false);
const appointments = ref([]);
const hasMore = ref(true);

onShow(() => {
  if (!requireLogin()) {
    return;
  }
  loadAppointments(true);
});

onPullDownRefresh(async () => {
  await loadAppointments(true);
  uni.stopPullDownRefresh();
});

onReachBottom(() => {
  if (!loading.value && hasMore.value) {
    loadAppointments(false);
  }
});

function changeStatus(status) {
  if (currentStatus.value === status) {
    return;
  }
  currentStatus.value = status;
  loadAppointments(true);
}

async function loadAppointments(reset) {
  if (loading.value) {
    return;
  }
  if (reset) {
    pageNo.value = 1;
    hasMore.value = true;
  }
  loading.value = true;
  try {
    const params = {
      pageNo: pageNo.value,
      pageSize
    };
    if (currentStatus.value !== '') {
      params.status = currentStatus.value;
    }
    const data = await getMyAppointmentPage(params);
    const records = data?.records || [];
    total.value = data?.total || 0;
    appointments.value = reset ? records : appointments.value.concat(records);
    hasMore.value = appointments.value.length < total.value;
    if (hasMore.value) {
      pageNo.value += 1;
    }
  } finally {
    loading.value = false;
  }
}

function cancelItem(item) {
  uni.showModal({
    title: '取消预约',
    content: '确认取消这次看房预约吗？',
    confirmText: '确认取消',
    success: async (res) => {
      if (!res.confirm) {
        return;
      }
      await cancelAppointment({
        id: item.id,
        cancelReason: '用户主动取消'
      });
      uni.showToast({ title: '已取消', icon: 'success' });
      loadAppointments(true);
    }
  });
}

function goRoom(roomId) {
  if (!roomId) {
    return;
  }
  uni.navigateTo({
    url: `/pages/room/detail?id=${roomId}`
  });
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding: 48rpx 32rpx;
  background:
    linear-gradient(145deg, rgba(217, 190, 144, 0.24), rgba(246, 243, 236, 0) 34%),
    #f6f3ec;
}

.header {
  margin-bottom: 26rpx;
}

.eyebrow {
  display: block;
  color: #9b6b26;
  font-size: 22rpx;
  font-weight: 800;
}

.title {
  display: block;
  margin-top: 14rpx;
  color: #1d2a24;
  font-size: 48rpx;
  font-weight: 900;
}

.subtitle {
  display: block;
  margin-top: 10rpx;
  color: #7d7263;
  font-size: 26rpx;
  line-height: 38rpx;
}

.status-scroll {
  width: 100%;
  margin-bottom: 26rpx;
  white-space: nowrap;
}

.status-tabs {
  display: inline-flex;
  gap: 16rpx;
}

.status-tab {
  display: inline-flex;
  min-width: 112rpx;
  height: 64rpx;
  padding: 0 24rpx;
  border-radius: 8rpx;
  align-items: center;
  justify-content: center;
  background: #fffaf0;
  color: #756b5f;
  border: 1rpx solid rgba(62, 55, 46, 0.1);
  font-size: 26rpx;
}

.status-tab.active {
  background: #1f352c;
  color: #fff8e8;
}

.appointment-list {
  display: grid;
  gap: 22rpx;
}

.appointment-card {
  padding: 26rpx;
  border-radius: 8rpx;
  background: #fffaf0;
  border: 1rpx solid rgba(62, 55, 46, 0.1);
  box-shadow: 0 14rpx 36rpx rgba(49, 42, 32, 0.07);
}

.card-top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 18rpx;
  margin-bottom: 22rpx;
}

.room-block {
  flex: 1;
  min-width: 0;
}

.building {
  display: block;
  color: #8c6a34;
  font-size: 24rpx;
  font-weight: 800;
}

.room {
  display: block;
  margin-top: 8rpx;
  color: #1d2a24;
  font-size: 34rpx;
  font-weight: 900;
}

.status-pill {
  flex-shrink: 0;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  font-weight: 800;
}

.appointment-booked {
  color: #1f6b45;
  background: #e5f4ea;
}

.appointment-canceled {
  color: #6b7280;
  background: #ece8df;
}

.appointment-expired {
  color: #9a342f;
  background: #f7dedb;
}

.appointment-unknown {
  color: #6b7280;
  background: #ece8df;
}

.info-row {
  display: flex;
  justify-content: space-between;
  gap: 24rpx;
  padding: 12rpx 0;
  border-top: 1rpx solid rgba(62, 55, 46, 0.08);
}

.info-label {
  flex-shrink: 0;
  color: #8c8173;
  font-size: 25rpx;
}

.info-value {
  min-width: 0;
  color: #1f352c;
  font-size: 26rpx;
  text-align: right;
  word-break: break-word;
}

.card-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 18rpx;
  margin-top: 22rpx;
}

.ghost-button,
.cancel-button {
  height: 78rpx;
  border-radius: 8rpx;
  font-size: 26rpx;
  font-weight: 800;
  line-height: 78rpx;
}

.ghost-button {
  background: #fffaf0;
  color: #1f352c;
  border: 1rpx solid rgba(31, 53, 44, 0.18);
}

.cancel-button {
  background: #f7dedb;
  color: #9a342f;
}

.state {
  min-height: 360rpx;
  border-radius: 8rpx;
  background: rgba(255, 250, 240, 0.78);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #756b5f;
  font-size: 28rpx;
}

.state-title {
  color: #1d2a24;
  font-size: 32rpx;
  font-weight: 800;
}

.state-desc {
  margin-top: 12rpx;
  color: #7d7263;
  font-size: 26rpx;
}

.footer-state {
  padding: 32rpx 0 12rpx;
  text-align: center;
  color: #8c8173;
  font-size: 24rpx;
}
</style>
