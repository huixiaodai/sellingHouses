<template>
  <view class="page">
    <view class="header">
      <text class="title">{{ buildingName || '楼层' }}</text>
      <text class="subtitle">楼层倒序展示，快速判断哪层还有房</text>
    </view>

    <view v-if="loading" class="state">加载中...</view>
    <view v-else-if="floors.length === 0" class="state">暂无楼层</view>
    <view v-else class="list">
      <FloorCard v-for="item in floors" :key="item.floorNo" :floor="item" @select="goControl(item)" />
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { onLoad, onPullDownRefresh, onShow } from '@dcloudio/uni-app';
import FloorCard from '../../components/FloorCard.vue';
import { getFloorList } from '../../api/building';
import { requireLogin } from '../../utils/authGuard';
import { requireSalesRole } from '../../utils/roleGuard';

const estateId = ref('');
const unitId = ref('');
const buildingName = ref('');
const floors = ref([]);
const loading = ref(false);

onLoad((options) => {
  estateId.value = options.estateId || '';
  unitId.value = options.unitId || '';
  buildingName.value = decodeURIComponent(options.buildingName || '');
});

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
  if (!unitId.value || loading.value) {
    return;
  }
  loading.value = true;
  try {
    floors.value = await getFloorList({ unitId: unitId.value });
  } finally {
    loading.value = false;
  }
}

function goControl(item) {
  uni.navigateTo({
    url: `/pages/control/index?buildingId=${estateId.value}&unitId=${unitId.value}&floorNo=${item.floorNo}&buildingName=${encodeURIComponent(buildingName.value)}`
  });
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding: 44rpx 28rpx;
  background: #f7f8f5;
}

.header {
  margin-bottom: 26rpx;
}

.title {
  display: block;
  color: #1d2a24;
  font-size: 44rpx;
  font-weight: 900;
}

.subtitle {
  display: block;
  margin-top: 8rpx;
  color: #6f7b74;
  font-size: 25rpx;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.state {
  min-height: 320rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8rpx;
  background: #fff;
  color: #6f7b74;
  font-size: 28rpx;
}
</style>
