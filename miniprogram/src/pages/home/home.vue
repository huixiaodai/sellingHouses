<template>
  <view class="page">
    <view class="hero">
      <text class="eyebrow">SELLING HOUSES</text>
      <text class="title">精选房源</text>
      <text class="subtitle">直接浏览可售房源，查看价格、户型与销售状态</text>
    </view>

    <view v-if="loading && rooms.length === 0" class="state">加载中...</view>

    <view v-else-if="rooms.length === 0" class="state">
      <text class="state-title">暂无房源</text>
      <text class="state-desc">稍后再来看看新的可售房源</text>
    </view>

    <view v-else class="room-list">
      <view v-for="room in rooms" :key="room.id" class="room-card" @tap="goDetail(room.id)">
        <image v-if="room.cover" class="room-cover" :src="room.cover" mode="aspectFill" />
        <view v-else class="room-cover placeholder-cover">
          <text>{{ room.roomNo || '房源' }}</text>
        </view>

        <view class="room-body">
          <view class="room-title-row">
            <text class="building-name">{{ room.buildingName || '楼盘待完善' }}</text>
            <text class="status-dot" :class="getRoomStatusClass(room.status)">
              {{ getRoomStatus(room.status) }}
            </text>
          </view>

          <text class="room-title">{{ room.unitName || '楼栋' }} {{ room.roomNo || '-' }}</text>
          <text class="room-meta">
            {{ room.layout || '户型待定' }} · {{ room.area || '-' }}㎡ · {{ room.orientation || '朝向待定' }}
          </text>

          <text class="price">{{ formatPrice(room.price) }}</text>
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
import { onPullDownRefresh, onReachBottom, onShow } from '@dcloudio/uni-app';
import { getRoomPage } from '../../api/room';
import { requireLogin } from '../../utils/authGuard';
import { formatPrice, getRoomStatus, getRoomStatusClass } from '../../utils/format';

const pageNo = ref(1);
const pageSize = 20;
const total = ref(0);
const loading = ref(false);
const rooms = ref([]);
const hasMore = ref(true);

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

async function loadRooms(reset) {
  if (loading.value) {
    return;
  }
  if (reset) {
    pageNo.value = 1;
    hasMore.value = true;
  }
  loading.value = true;
  try {
    const data = await getRoomPage({
      pageNo: pageNo.value,
      pageSize
    });
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
  padding: 56rpx 28rpx 48rpx;
  background:
    linear-gradient(145deg, rgba(217, 190, 144, 0.28), rgba(246, 243, 236, 0) 34%),
    #f6f3ec;
}

.hero {
  margin-bottom: 28rpx;
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
  font-size: 54rpx;
  font-weight: 800;
}

.subtitle {
  display: block;
  margin-top: 10rpx;
  color: #7d7263;
  font-size: 26rpx;
  line-height: 38rpx;
}

.room-list {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 20rpx;
}

.room-card {
  min-width: 0;
  overflow: hidden;
  border-radius: 8rpx;
  background: #fffaf0;
  border: 1rpx solid rgba(62, 55, 46, 0.1);
  box-shadow: 0 14rpx 36rpx rgba(49, 42, 32, 0.07);
}

.room-cover {
  width: 100%;
  height: 210rpx;
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
  padding: 18rpx;
}

.room-title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10rpx;
}

.building-name {
  flex: 1;
  min-width: 0;
  color: #8c6a34;
  font-size: 22rpx;
  font-weight: 800;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.status-dot {
  flex-shrink: 0;
  max-width: 86rpx;
  padding: 5rpx 10rpx;
  border-radius: 8rpx;
  font-size: 20rpx;
  font-weight: 800;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.room-title {
  display: block;
  margin-top: 10rpx;
  color: #1d2a24;
  font-size: 28rpx;
  font-weight: 900;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.room-meta {
  display: block;
  margin-top: 10rpx;
  color: #756b5f;
  font-size: 22rpx;
  line-height: 32rpx;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.price {
  display: block;
  margin-top: 14rpx;
  color: #9b3f22;
  font-size: 28rpx;
  font-weight: 900;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.status-available {
  color: #1f6b45;
  background: #e5f4ea;
}

.status-reserved {
  color: #8b5f20;
  background: #f8ebcf;
}

.status-sold {
  color: #9a342f;
  background: #f7dedb;
}

.status-unavailable,
.status-unknown {
  color: #6b7280;
  background: #ece8df;
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
