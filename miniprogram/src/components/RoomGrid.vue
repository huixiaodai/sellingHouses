<template>
  <view class="grid">
    <RoomCard
      v-for="room in rooms"
      :key="room.id"
      :room="room"
      :highlighted="highlightId === room.id"
      @select="$emit('select', room)"
    />
  </view>
</template>

<script setup>
import RoomCard from './RoomCard.vue';

defineProps({
  rooms: {
    type: Array,
    default: () => []
  },
  highlightId: {
    type: [Number, String],
    default: ''
  }
});

defineEmits(['select']);
</script>

<style scoped>
.grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 18rpx;
}
</style>
