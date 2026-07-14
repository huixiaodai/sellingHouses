<template>
  <view class="card" @tap="$emit('select')">
    <text class="floor">{{ floor.floorNo }}F</text>
    <view class="stats">
      <text class="available">待售 {{ floor.availableCount || 0 }}</text>
      <text class="sold">已售 {{ floor.soldCount || 0 }}</text>
      <text class="locked">锁定 {{ floor.lockedCount || 0 }}</text>
    </view>
  </view>
</template>

<script setup>
defineProps({
  floor: {
    type: Object,
    required: true
  }
});

defineEmits(['select']);
</script>

<style scoped>
.card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18rpx;
  padding: 24rpx 26rpx;
  border-radius: 8rpx;
  background: #fff;
  border: 1rpx solid rgba(31, 53, 44, 0.08);
}

.floor {
  width: 112rpx;
  color: #1d2a24;
  font-size: 36rpx;
  font-weight: 900;
}

.stats {
  flex: 1;
  display: flex;
  justify-content: flex-end;
  flex-wrap: wrap;
  gap: 10rpx;
}

.stats text {
  padding: 8rpx 10rpx;
  border-radius: 8rpx;
  font-size: 22rpx;
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
