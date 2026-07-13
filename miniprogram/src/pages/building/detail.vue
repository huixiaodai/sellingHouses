<template>
  <view class="page">
    <view v-if="loading" class="state">加载中...</view>
    <view v-else-if="!building" class="state">楼盘不存在</view>

    <view v-else>
      <image v-if="heroImage" class="hero-image" :src="heroImage" mode="aspectFill" />
      <view v-else class="hero-image placeholder-cover">
        <text>楼盘详情</text>
      </view>

      <view class="content">
        <text class="name">{{ building.name }}</text>
        <text class="address">{{ building.address || '地址待完善' }}</text>

        <view class="info-grid">
          <view class="info-item">
            <text class="info-label">开发商</text>
            <text class="info-value">{{ building.developer || '-' }}</text>
          </view>
          <view class="info-item">
            <text class="info-label">开盘时间</text>
            <text class="info-value">{{ formatDateTime(building.openingTime) }}</text>
          </view>
          <view class="info-item">
            <text class="info-label">交房时间</text>
            <text class="info-value">{{ formatDateTime(building.deliveryTime) }}</text>
          </view>
        </view>

        <view class="section">
          <text class="section-title">项目介绍</text>
          <text class="description">{{ building.description || '暂无项目介绍' }}</text>
        </view>

        <view v-if="gallery.length > 0" class="section">
          <text class="section-title">项目相册</text>
          <scroll-view scroll-x class="gallery">
            <image v-for="image in gallery" :key="image" class="gallery-image" :src="image" mode="aspectFill" />
          </scroll-view>
        </view>
      </view>

      <view class="bottom-bar">
        <button class="primary-button" @tap="goRooms">查看房源</button>
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue';
import { onLoad, onShow } from '@dcloudio/uni-app';
import { getBuildingDetail } from '../../api/building';
import { requireLogin } from '../../utils/authGuard';
import { formatDateTime, parseImageList } from '../../utils/format';

const buildingId = ref(null);
const building = ref(null);
const loading = ref(false);
const gallery = computed(() => parseImageList(building.value?.bannerImages));
const heroImage = computed(() => building.value?.cover || gallery.value[0] || '');

onLoad((options) => {
  buildingId.value = options.id;
});

onShow(() => {
  if (!requireLogin()) {
    return;
  }
  loadDetail();
});

async function loadDetail() {
  if (!buildingId.value || loading.value) {
    return;
  }
  loading.value = true;
  try {
    building.value = await getBuildingDetail(buildingId.value);
  } finally {
    loading.value = false;
  }
}

function goRooms() {
  uni.navigateTo({
    url: `/pages/room/list?buildingId=${buildingId.value}&buildingName=${encodeURIComponent(building.value?.name || '')}`
  });
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding-bottom: 144rpx;
  background: #f6f3ec;
}

.hero-image {
  width: 100%;
  height: 430rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.placeholder-cover {
  background: linear-gradient(135deg, #263d33, #9b6b26);
  color: #fff8e8;
  font-size: 32rpx;
  font-weight: 800;
}

.content {
  padding: 34rpx 32rpx 0;
}

.name {
  display: block;
  color: #1d2a24;
  font-size: 44rpx;
  font-weight: 800;
}

.address {
  display: block;
  margin-top: 14rpx;
  color: #756b5f;
  font-size: 28rpx;
  line-height: 42rpx;
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 18rpx;
  margin-top: 32rpx;
}

.info-item {
  padding: 24rpx;
  border-radius: 8rpx;
  background: #fffaf0;
  border: 1rpx solid rgba(62, 55, 46, 0.1);
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
  font-weight: 700;
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

.description {
  display: block;
  color: #4d463b;
  font-size: 28rpx;
  line-height: 46rpx;
}

.gallery {
  width: 100%;
  white-space: nowrap;
}

.gallery-image {
  width: 260rpx;
  height: 180rpx;
  margin-right: 18rpx;
  border-radius: 8rpx;
}

.bottom-bar {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 22rpx 32rpx 40rpx;
  background: rgba(246, 243, 236, 0.96);
  border-top: 1rpx solid rgba(62, 55, 46, 0.1);
}

.primary-button {
  width: 100%;
  height: 92rpx;
  border-radius: 8rpx;
  background: #1f352c;
  color: #fff8e8;
  font-size: 30rpx;
  font-weight: 800;
  line-height: 92rpx;
}

.state {
  min-height: 70vh;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #756b5f;
  font-size: 28rpx;
}
</style>
