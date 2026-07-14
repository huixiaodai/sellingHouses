<template>
  <view class="page">
    <view class="header">
      <text class="eyebrow">SALE CONTROL</text>
      <text class="title">楼盘销控</text>
      <text class="subtitle">按楼盘、楼栋、楼层快速定位可售房源</text>
    </view>

    <view v-if="loading" class="state">加载中...</view>
    <view v-else-if="estates.length === 0" class="state">暂无楼盘</view>

    <view v-else class="list">
      <view v-for="estate in estates" :key="estate.id" class="estate-card">
        <image v-if="estate.cover" class="cover" :src="estate.cover" mode="aspectFill" lazy-load />
        <view v-else class="cover placeholder">
          <text>{{ estate.name }}</text>
        </view>
        <view class="body">
          <text class="name">{{ estate.name }}</text>
          <text class="address">{{ estate.address || '地址待完善' }}</text>
          <view class="summary">
            <text>总计 {{ estate.totalCount || 0 }} 套</text>
            <text class="available">待售 {{ estate.availableCount || 0 }}</text>
            <text class="sold">已售 {{ estate.soldCount || 0 }}</text>
            <text class="locked">锁定 {{ estate.lockedCount || 0 }}</text>
          </view>
          <button class="enter" @tap="goBuildings(estate)">进入楼栋</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { onPullDownRefresh, onShow } from '@dcloudio/uni-app';
import { getEstateList } from '../../api/estate';
import { requireLogin } from '../../utils/authGuard';
import { requireSalesRole } from '../../utils/roleGuard';

const estates = ref([]);
const loading = ref(false);

onShow(() => {
  if (!requireLogin()) {
    return;
  }
  if (!requireSalesRole()) {
    return;
  }
  loadData();
});

onPullDownRefresh(async () => {
  await loadData();
  uni.stopPullDownRefresh();
});

async function loadData() {
  if (loading.value) {
    return;
  }
  loading.value = true;
  try {
    estates.value = await getEstateList();
  } finally {
    loading.value = false;
  }
}

function goBuildings(estate) {
  uni.navigateTo({
    url: `/pages/building/index?estateId=${estate.id}&estateName=${encodeURIComponent(estate.name || '')}`
  });
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding: 48rpx 28rpx;
  background: #f7f8f5;
}

.header {
  margin-bottom: 28rpx;
}

.eyebrow {
  color: #947331;
  font-size: 22rpx;
  font-weight: 900;
}

.title {
  display: block;
  margin-top: 10rpx;
  color: #1d2a24;
  font-size: 50rpx;
  font-weight: 900;
}

.subtitle {
  display: block;
  margin-top: 10rpx;
  color: #6f7b74;
  font-size: 26rpx;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 22rpx;
}

.estate-card {
  overflow: hidden;
  border-radius: 8rpx;
  background: #fff;
  border: 1rpx solid rgba(31, 53, 44, 0.08);
  box-shadow: 0 16rpx 42rpx rgba(31, 53, 44, 0.08);
}

.cover {
  width: 100%;
  height: 260rpx;
}

.placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1f352c, #947331);
  color: #fff;
  font-size: 34rpx;
  font-weight: 900;
}

.body {
  padding: 24rpx;
}

.name {
  display: block;
  color: #1d2a24;
  font-size: 36rpx;
  font-weight: 900;
}

.address {
  display: block;
  margin-top: 10rpx;
  color: #6f7b74;
  font-size: 25rpx;
  line-height: 36rpx;
}

.summary {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
  margin-top: 20rpx;
}

.summary text {
  padding: 8rpx 12rpx;
  border-radius: 8rpx;
  background: #f1f3ef;
  color: #526059;
  font-size: 23rpx;
  font-weight: 800;
}

.summary .available {
  color: #1f7a3d;
  background: #e8f7ec;
}

.summary .sold {
  color: #b4232c;
  background: #ffe9eb;
}

.summary .locked {
  color: #5d6460;
  background: #eeeeee;
}

.enter {
  width: 100%;
  height: 82rpx;
  margin-top: 24rpx;
  border-radius: 8rpx;
  background: #1f352c;
  color: #fff;
  font-size: 28rpx;
  font-weight: 900;
  line-height: 82rpx;
}

.state {
  min-height: 360rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8rpx;
  background: #fff;
  color: #6f7b74;
  font-size: 28rpx;
}
</style>
