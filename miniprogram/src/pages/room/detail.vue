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
        <swiper-item v-for="(image, index) in carouselImages" :key="image">
          <image class="hero-image" :src="image" mode="aspectFill" @tap="previewImage(carouselImages, index)" />
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
          <image
            v-for="(image, index) in gallery"
            :key="image"
            class="detail-image"
            :src="image"
            mode="aspectFill"
            @tap="previewImage(gallery, index)"
          />
        </view>

        <view class="section">
          <text class="section-title">备注</text>
          <text class="remark">{{ room.remark || '暂无备注' }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue';
import { onLoad, onShow } from '@dcloudio/uni-app';
import { getRoomDetail } from '../../api/room';
import { requireLogin } from '../../utils/authGuard';
import { formatPrice, getRoomStatus, getRoomStatusClass, parseImageList } from '../../utils/format';

const roomId = ref(null);
const room = ref(null);
const loading = ref(false);
const gallery = computed(() => parseImageList(room.value?.images));
const carouselImages = computed(() => {
  if (gallery.value.length > 0) {
    return gallery.value;
  }
  return room.value?.cover ? [room.value.cover] : [];
});

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

function previewImage(images, index) {
  if (!images.length) {
    return;
  }
  uni.previewImage({
    urls: images,
    current: images[index]
  });
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f6f3ec;
}

.hero-swiper {
  width: 100%;
  height: 430rpx;
}

.hero-image {
  width: 100%;
  height: 430rpx;
  display: flex;
  align-items: center;
  justify-content: center;
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

.price-block {
  margin-top: 30rpx;
  padding: 28rpx;
  border-radius: 8rpx;
  background: #fffaf0;
  border: 1rpx solid rgba(62, 55, 46, 0.1);
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
</style>
