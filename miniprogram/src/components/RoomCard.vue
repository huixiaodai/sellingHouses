<template>
  <view
    :id="`room-${room.id}`"
    class="room-card"
    :class="[{ highlighted }, statusClass]"
    :style="{ backgroundColor: statusColor }"
    @tap="$emit('select', room)"
  >
    <view class="top-row">
      <text class="room-no">{{ room.roomNo }}</text>
      <text class="status">{{ statusLabel }}</text>
    </view>
    <text class="meta">{{ formatArea(room.area) }} · {{ room.layout || '户型待定' }}</text>
    <text class="price">{{ formatPrice(room.price) }}</text>
    <text class="unit-price">{{ formatUnitPrice(room.price, room.area) }}</text>
  </view>
</template>

<script setup>
import { computed } from 'vue';
import { formatArea, formatPrice, formatUnitPrice, getRoomStatus, getRoomStatusClass, getRoomStatusColor } from '../utils/format';

const props = defineProps({
  room: {
    type: Object,
    required: true
  },
  highlighted: {
    type: Boolean,
    default: false
  }
});

defineEmits(['select']);

const statusLabel = computed(() => getRoomStatus(props.room.status));
const statusClass = computed(() => getRoomStatusClass(props.room.status));
const statusColor = computed(() => getRoomStatusColor(props.room.status));
</script>

<style scoped>
.room-card {
  min-height: 190rpx;
  padding: 20rpx;
  border-radius: 8rpx;
  color: #fff;
  box-shadow: 0 14rpx 30rpx rgba(31, 53, 44, 0.12);
  transform: scale(1);
  transition: transform 180ms ease, box-shadow 180ms ease, opacity 180ms ease;
}

.room-card.highlighted {
  transform: scale(1.04);
  box-shadow: 0 0 0 6rpx rgba(31, 53, 44, 0.18), 0 18rpx 40rpx rgba(31, 53, 44, 0.2);
}

.status-locked {
  color: #1f2933;
}

.top-row {
  display: flex;
  justify-content: space-between;
  gap: 10rpx;
  align-items: center;
}

.room-no {
  min-width: 0;
  font-size: 34rpx;
  line-height: 42rpx;
  font-weight: 900;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.status {
  flex-shrink: 0;
  font-size: 22rpx;
  font-weight: 900;
}

.meta,
.price,
.unit-price {
  display: block;
  margin-top: 10rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.meta {
  font-size: 23rpx;
  font-weight: 700;
}

.price {
  font-size: 27rpx;
  font-weight: 900;
}

.unit-price {
  font-size: 21rpx;
  opacity: 0.9;
}
</style>
