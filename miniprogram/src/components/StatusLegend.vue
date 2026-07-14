<template>
  <view class="legend">
    <view v-for="item in items" :key="item.value" class="legend-item">
      <view class="dot" :style="{ backgroundColor: item.color }"></view>
      <text>{{ item.label }}</text>
    </view>
  </view>
</template>

<script setup>
const items = [
  { label: '待售', value: 0, color: '#52C41A' },
  { label: '已售', value: 1, color: '#F5222D' },
  { label: '锁定', value: 2, color: '#D9D9D9' }
];
</script>

<style scoped>
.legend {
  display: flex;
  align-items: center;
  gap: 18rpx;
  min-height: 56rpx;
  overflow-x: auto;
  white-space: nowrap;
}

.legend-item {
  display: inline-flex;
  align-items: center;
  gap: 8rpx;
  color: #49564f;
  font-size: 24rpx;
  font-weight: 700;
}

.dot {
  width: 18rpx;
  height: 18rpx;
  border-radius: 50%;
  border: 1rpx solid rgba(0, 0, 0, 0.08);
}
</style>
