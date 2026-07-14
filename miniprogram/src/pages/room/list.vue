<template>
  <view class="page">
    <view class="header">
      <text class="eyebrow">ROOMS</text>
      <text class="title">{{ buildingName || '房源列表' }}</text>
      <text class="subtitle">查看面积、价格与销售状态</text>
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

    <view v-if="loading && rooms.length === 0" class="state">加载中...</view>

    <view v-else-if="rooms.length === 0" class="state">
      <text class="state-title">暂无房源</text>
      <text class="state-desc">换个状态筛选试试</text>
    </view>

    <view v-else class="room-list">
      <view v-for="room in rooms" :key="room.id" class="room-card" @tap="goDetail(room.id)">
        <image v-if="room.cover" class="room-cover" :src="room.cover" mode="aspectFill" />
        <view v-else class="room-cover placeholder-cover">
          <text>{{ room.roomNo || '房源' }}</text>
        </view>
        <view class="room-body">
          <view class="room-title-row">
            <text class="room-title">{{ room.unitName || '楼栋' }} {{ room.roomNo }}</text>
            <text class="status-pill" :class="getRoomStatusClass(room.status)">
              {{ getRoomStatus(room.status) }}
            </text>
          </view>
          <text class="room-meta">{{ room.layout || '户型待定' }} · {{ room.area || '-' }}㎡ · {{ room.orientation || '朝向待定' }}</text>
          <view class="room-bottom">
            <text class="price">{{ formatPrice(room.price) }}</text>
            <text class="arrow">详情 ›</text>
          </view>
        </view>
      </view>
    </view>

    <view v-if="rooms.length > 0" class="footer-state">
      {{ hasMore ? (loading ? '加载中...' : '上拉加载更多') : '已经到底了' }}
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { onLoad, onPullDownRefresh, onReachBottom, onShow } from '@dcloudio/uni-app';
import { getRoomPage } from '../../api/room';
import { requireLogin } from '../../utils/authGuard';
import { ROOM_STATUS_OPTIONS, formatPrice, getRoomStatus, getRoomStatusClass } from '../../utils/format';

const buildingId = ref(null);
const buildingName = ref('');
const currentStatus = ref('');
const statusOptions = ROOM_STATUS_OPTIONS;
const pageNo = ref(1);
const pageSize = 10;
const total = ref(0);
const loading = ref(false);
const rooms = ref([]);
const hasMore = ref(true);

onLoad((options) => {
  buildingId.value = options.buildingId;
  buildingName.value = decodeURIComponent(options.buildingName || '');
});

onShow(() => {
  if (!requireLogin()) {
    return;
  }
  if (rooms.value.length === 0) {
    loadRooms(true);
  }
});

onPullDownRefresh(async () => {
  await loadRooms(true);
  uni.stopPullDownRefresh();
});

onReachBottom(() => {
  if (!loading.value && hasMore.value) {
    loadRooms(false);
  }
});

function changeStatus(status) {
  if (currentStatus.value === status) {
    return;
  }
  currentStatus.value = status;
  loadRooms(true);
}

async function loadRooms(reset) {
  if (!buildingId.value || loading.value) {
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
      pageSize,
      buildingId: buildingId.value
    };
    if (currentStatus.value !== '') {
      params.status = currentStatus.value;
    }
    const data = await getRoomPage(params);
    const records = data?.records || [];
    total.value = data?.total || 0;
    rooms.value = reset ? records : rooms.value.concat(records);
    hasMore.value = rooms.value.length < total.value;
    if (hasMore.value) {
      pageNo.value += 1;
    }
  } finally {
    loading.value = false;
  }
}

function goDetail(id) {
  uni.navigateTo({
    url: `/pages/room/detail?id=${id}`
  });
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding: 48rpx 32rpx;
  background: #f6f3ec;
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
  font-size: 44rpx;
  font-weight: 800;
}

.subtitle {
  display: block;
  margin-top: 10rpx;
  color: #7d7263;
  font-size: 26rpx;
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

.room-list {
  display: flex;
  flex-direction: column;
  gap: 22rpx;
}

.room-card {
  display: flex;
  overflow: hidden;
  border-radius: 8rpx;
  background: #fffaf0;
  border: 1rpx solid rgba(62, 55, 46, 0.1);
}

.room-cover {
  width: 190rpx;
  min-height: 190rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.placeholder-cover {
  background: linear-gradient(135deg, #263d33, #9b6b26);
  color: #fff8e8;
  font-size: 28rpx;
  font-weight: 800;
}

.room-body {
  flex: 1;
  min-width: 0;
  padding: 22rpx 24rpx;
}

.room-title-row {
  display: flex;
  justify-content: space-between;
  gap: 16rpx;
}

.room-title {
  flex: 1;
  color: #1d2a24;
  font-size: 30rpx;
  font-weight: 800;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.status-pill {
  flex-shrink: 0;
  padding: 6rpx 12rpx;
  border-radius: 8rpx;
  font-size: 22rpx;
  font-weight: 700;
}

.status-available {
  color: #1f6b45;
  background: #e5f4ea;
}

.status-sold {
  color: #9a342f;
  background: #f7dedb;
}

.status-locked,
.status-unavailable,
.status-unknown {
  color: #6b7280;
  background: #ece8df;
}

.room-meta {
  display: block;
  margin-top: 18rpx;
  color: #756b5f;
  font-size: 24rpx;
  line-height: 36rpx;
}

.room-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20rpx;
}

.price {
  color: #9b3f22;
  font-size: 30rpx;
  font-weight: 900;
}

.arrow {
  color: #1f352c;
  font-size: 24rpx;
  font-weight: 800;
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
