<template>
  <view class="page">
    <view class="top">
      <SearchBar v-model="keyword" @search="handleSearch" />
      <StatusLegend />
      <view class="tabs">
        <text
          v-for="item in quickOptions"
          :key="String(item.value)"
          class="tab"
          :class="{ active: quickStatus === item.value }"
          @tap="changeQuickStatus(item.value)"
        >
          {{ item.label }}
        </text>
        <text class="filter" @tap="drawerVisible = true">筛选</text>
      </view>
    </view>

    <scroll-view class="scroll" scroll-y :scroll-into-view="scrollTarget" scroll-with-animation>
      <view class="summary">
        <text class="title">{{ buildingName || '销控图' }} · {{ floorNo }}F</text>
        <text class="subtitle">当前显示 {{ rooms.length }} 套</text>
      </view>

      <view v-if="loading" class="state">加载中...</view>
      <view v-else-if="rooms.length === 0" class="state">暂无符合条件的房源</view>
      <RoomGrid v-else :rooms="rooms" :highlight-id="highlightId" @select="goDetail" />
    </scroll-view>

    <FilterDrawer
      :visible="drawerVisible"
      :model-value="filters"
      @close="drawerVisible = false"
      @reset="handleFilterReset"
      @apply="handleFilterApply"
    />
  </view>
</template>

<script setup>
import { nextTick, reactive, ref } from 'vue';
import { onLoad, onPullDownRefresh, onShow } from '@dcloudio/uni-app';
import FilterDrawer from '../../components/FilterDrawer.vue';
import RoomGrid from '../../components/RoomGrid.vue';
import SearchBar from '../../components/SearchBar.vue';
import StatusLegend from '../../components/StatusLegend.vue';
import { filterControlRooms, getControlRoomList, searchControlRooms } from '../../api/room';
import { SALE_CONTROL_QUICK_STATUS_OPTIONS } from '../../utils/format';
import { requireLogin } from '../../utils/authGuard';
import { requireSalesRole } from '../../utils/roleGuard';
import { useSocket } from '../../hooks/useSocket';

const buildingId = ref('');
const unitId = ref('');
const floorNo = ref('');
const buildingName = ref('');
const initialRoomId = ref('');
const keyword = ref('');
const rooms = ref([]);
const loading = ref(false);
const drawerVisible = ref(false);
const quickStatus = ref('');
const quickOptions = SALE_CONTROL_QUICK_STATUS_OPTIONS;
const highlightId = ref('');
const scrollTarget = ref('');
const filters = reactive({
  minPrice: '',
  maxPrice: '',
  minArea: '',
  maxArea: '',
  layout: '',
  orientation: '',
  decoration: '',
  status: ''
});

useSocket({
  onRoomStatusChange: ({ roomId, status }) => {
    const room = rooms.value.find((item) => String(item.id) === String(roomId));
    if (room) {
      room.status = status;
    }
  }
});

onLoad((options) => {
  buildingId.value = options.buildingId || '';
  unitId.value = options.unitId || '';
  floorNo.value = options.floorNo || '';
  buildingName.value = decodeURIComponent(options.buildingName || '');
  initialRoomId.value = options.roomId || '';
});

onShow(() => {
  if (!requireLogin()) {
    return;
  }
  if (!requireSalesRole()) {
    return;
  }
  loadRooms();
});

onPullDownRefresh(async () => {
  await loadRooms();
  uni.stopPullDownRefresh();
});

function baseParams(extra = {}) {
  return {
    buildingId: buildingId.value,
    unitId: unitId.value,
    floorNo: floorNo.value,
    ...extra
  };
}

async function loadRooms(extra = {}) {
  if (!unitId.value || !floorNo.value || loading.value) {
    return;
  }
  loading.value = true;
  try {
    const params = baseParams({ ...extra });
    if (quickStatus.value !== '') {
      params.status = quickStatus.value;
    }
    rooms.value = await getControlRoomList(params);
    if (initialRoomId.value) {
      await locateRoom(initialRoomId.value);
      initialRoomId.value = '';
    }
  } finally {
    loading.value = false;
  }
}

async function changeQuickStatus(status) {
  if (quickStatus.value === status) {
    return;
  }
  quickStatus.value = status;
  await loadRooms(cleanFilters());
}

function cleanFilters() {
  const data = {};
  Object.keys(filters).forEach((key) => {
    if (filters[key] !== '') {
      data[key] = filters[key];
    }
  });
  return data;
}

async function handleFilterApply(data) {
  Object.keys(filters).forEach((key) => {
    filters[key] = data[key] ?? '';
  });
  drawerVisible.value = false;
  loading.value = true;
  try {
    const params = baseParams(cleanFilters());
    if (quickStatus.value !== '') {
      params.status = quickStatus.value;
    }
    rooms.value = await filterControlRooms(params);
  } finally {
    loading.value = false;
  }
}

async function handleFilterReset() {
  Object.keys(filters).forEach((key) => {
    filters[key] = '';
  });
  drawerVisible.value = false;
  await loadRooms();
}

async function handleSearch(value) {
  const text = String(value || '').trim();
  if (!text) {
    uni.showToast({ title: '请输入房号', icon: 'none' });
    return;
  }
  const matches = await searchControlRooms({ keyword: text, buildingId: buildingId.value });
  if (!matches || matches.length === 0) {
    uni.showToast({ title: '未找到房源', icon: 'none' });
    return;
  }
  if (matches.length === 1) {
    openMatchedRoom(matches[0]);
    return;
  }
  uni.showActionSheet({
    itemList: matches.slice(0, 6).map((item) => `${item.unitName || ''} ${item.floorNo}F ${item.roomNo}`),
    success: (res) => {
      openMatchedRoom(matches[res.tapIndex]);
    }
  });
}

async function openMatchedRoom(room) {
  if (String(room.unitId) === String(unitId.value) && String(room.floorNo) === String(floorNo.value)) {
    await locateRoom(room.id);
    return;
  }
  uni.navigateTo({
    url: `/pages/control/index?buildingId=${room.buildingId}&unitId=${room.unitId}&floorNo=${room.floorNo}&buildingName=${encodeURIComponent(room.unitName || '')}&roomId=${room.id}`
  });
}

async function locateRoom(id) {
  await nextTick();
  highlightId.value = Number(id);
  scrollTarget.value = `room-${id}`;
  setTimeout(() => {
    highlightId.value = '';
  }, 2200);
}

function goDetail(room) {
  uni.navigateTo({
    url: `/pages/room/detail?id=${room.id}`
  });
}
</script>

<style scoped>
.page {
  height: 100vh;
  background: #f7f8f5;
  display: flex;
  flex-direction: column;
}

.top {
  position: relative;
  z-index: 20;
  padding: 22rpx 24rpx 16rpx;
  background: rgba(247, 248, 245, 0.98);
  border-bottom: 1rpx solid rgba(31, 53, 44, 0.08);
}

.tabs {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-top: 14rpx;
}

.tab,
.filter {
  min-width: 112rpx;
  height: 62rpx;
  padding: 0 20rpx;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  border: 1rpx solid rgba(31, 53, 44, 0.1);
  color: #526059;
  font-size: 25rpx;
  font-weight: 900;
}

.tab.active,
.filter {
  background: #1f352c;
  color: #fff;
}

.filter {
  margin-left: auto;
}

.scroll {
  flex: 1;
  min-height: 0;
  padding: 24rpx;
}

.summary {
  margin-bottom: 20rpx;
}

.title {
  display: block;
  color: #1d2a24;
  font-size: 36rpx;
  font-weight: 900;
}

.subtitle {
  display: block;
  margin-top: 8rpx;
  color: #6f7b74;
  font-size: 24rpx;
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
