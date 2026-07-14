<template>
  <view class="page">
    <view class="sticky">
      <view class="header">
        <text class="title">{{ estateName || '楼栋' }}</text>
        <text class="subtitle">选择楼栋查看楼层销控</text>
      </view>
      <view class="toolbar">
        <input v-model.trim="keyword" class="input" placeholder="搜索楼栋" placeholder-class="placeholder" />
        <picker :range="sortLabels" :value="sortIndex" @change="handleSort">
          <view class="sort">{{ sortLabels[sortIndex] }}</view>
        </picker>
      </view>
      <view class="overview">
        <text>楼栋 {{ filteredBuildings.length }}</text>
        <text>待售 {{ totalAvailable }}</text>
      </view>
    </view>

    <view v-if="loading" class="state">加载中...</view>
    <view v-else-if="filteredBuildings.length === 0" class="state">暂无楼栋</view>
    <view v-else class="list">
      <BuildingCard v-for="item in filteredBuildings" :key="item.id" :building="item" @select="goFloors(item)" />
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue';
import { onLoad, onPullDownRefresh, onShow } from '@dcloudio/uni-app';
import BuildingCard from '../../components/BuildingCard.vue';
import { getControlBuildingList } from '../../api/building';
import { requireLogin } from '../../utils/authGuard';
import { requireSalesRole } from '../../utils/roleGuard';

const estateId = ref('');
const estateName = ref('');
const keyword = ref('');
const buildings = ref([]);
const loading = ref(false);
const sortIndex = ref(0);
const sortLabels = ['默认排序', '待售优先', '总套数优先'];

onLoad((options) => {
  estateId.value = options.estateId || '';
  estateName.value = decodeURIComponent(options.estateName || '');
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

const filteredBuildings = computed(() => {
  const text = keyword.value.toLowerCase();
  const list = buildings.value.filter((item) => !text || String(item.name || '').toLowerCase().includes(text));
  return [...list].sort((a, b) => {
    if (sortIndex.value === 1) {
      return (b.availableCount || 0) - (a.availableCount || 0);
    }
    if (sortIndex.value === 2) {
      return (b.totalCount || 0) - (a.totalCount || 0);
    }
    return (a.sortNo || 0) - (b.sortNo || 0);
  });
});

const totalAvailable = computed(() => filteredBuildings.value.reduce((sum, item) => sum + (item.availableCount || 0), 0));

async function loadData() {
  if (!estateId.value || loading.value) {
    return;
  }
  loading.value = true;
  try {
    buildings.value = await getControlBuildingList(estateId.value);
  } finally {
    loading.value = false;
  }
}

function handleSort(event) {
  sortIndex.value = Number(event.detail.value);
}

function goFloors(item) {
  uni.navigateTo({
    url: `/pages/floor/index?estateId=${estateId.value}&unitId=${item.id}&buildingName=${encodeURIComponent(item.name || '')}`
  });
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding: 0 28rpx 42rpx;
  background: #f7f8f5;
}

.sticky {
  position: sticky;
  top: 0;
  z-index: 10;
  padding: 42rpx 0 20rpx;
  background: #f7f8f5;
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

.toolbar {
  display: grid;
  grid-template-columns: 1fr 176rpx;
  gap: 14rpx;
  margin-top: 24rpx;
}

.input,
.sort {
  height: 74rpx;
  border-radius: 8rpx;
  background: #fff;
  border: 1rpx solid rgba(31, 53, 44, 0.1);
  font-size: 26rpx;
}

.input {
  padding: 0 22rpx;
}

.sort {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #1f352c;
  font-weight: 800;
}

.placeholder {
  color: #9aa39d;
}

.overview {
  display: flex;
  gap: 14rpx;
  margin-top: 16rpx;
  color: #526059;
  font-size: 24rpx;
  font-weight: 800;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 18rpx;
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
