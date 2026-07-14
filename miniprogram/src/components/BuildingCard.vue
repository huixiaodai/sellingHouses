<template>
  <view class="card" @tap="$emit('select')">
    <view class="main">
      <text class="name">{{ building.name }}</text>
      <text class="count">{{ total }}套</text>
    </view>
    <view class="stats">
      <text class="available">待售 {{ building.availableCount || 0 }}</text>
      <text class="sold">已售 {{ building.soldCount || 0 }}</text>
      <text class="locked">锁定 {{ building.lockedCount || 0 }}</text>
    </view>
  </view>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  building: {
    type: Object,
    required: true
  }
});

defineEmits(['select']);

const total = computed(() => props.building.totalCount || 0);
</script>

<style scoped>
.card {
  padding: 26rpx;
  border-radius: 8rpx;
  background: #fff;
  border: 1rpx solid rgba(31, 53, 44, 0.08);
  box-shadow: 0 14rpx 36rpx rgba(31, 53, 44, 0.08);
}

.main {
  display: flex;
  justify-content: space-between;
  gap: 18rpx;
}

.name {
  flex: 1;
  min-width: 0;
  color: #1d2a24;
  font-size: 34rpx;
  font-weight: 900;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.count {
  color: #526059;
  font-size: 26rpx;
  font-weight: 800;
}

.stats {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
  margin-top: 22rpx;
}

.stats text {
  padding: 8rpx 12rpx;
  border-radius: 8rpx;
  font-size: 23rpx;
  font-weight: 800;
}

.available {
  color: #1f7a3d;
  background: #e8f7ec;
}

.sold {
  color: #b4232c;
  background: #ffe9eb;
}

.locked {
  color: #5d6460;
  background: #eeeeee;
}
</style>
