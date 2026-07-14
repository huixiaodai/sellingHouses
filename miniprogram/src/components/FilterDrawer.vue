<template>
  <view v-if="visible" class="mask" @tap="$emit('close')">
    <view class="drawer" @tap.stop>
      <view class="header">
        <text class="title">高级筛选</text>
        <text class="close" @tap="$emit('close')">关闭</text>
      </view>

      <view class="section">
        <text class="label">总价</text>
        <view class="range">
          <input v-model="local.minPrice" class="input" type="digit" placeholder="最低" />
          <text class="dash">-</text>
          <input v-model="local.maxPrice" class="input" type="digit" placeholder="最高" />
        </view>
      </view>

      <view class="section">
        <text class="label">面积</text>
        <view class="range">
          <input v-model="local.minArea" class="input" type="digit" placeholder="最小" />
          <text class="dash">-</text>
          <input v-model="local.maxArea" class="input" type="digit" placeholder="最大" />
        </view>
      </view>

      <view class="section">
        <text class="label">户型</text>
        <view class="chips">
          <text v-for="item in layouts" :key="item" class="chip" :class="{ active: local.layout === item }" @tap="toggle('layout', item)">
            {{ item }}
          </text>
        </view>
      </view>

      <view class="section">
        <text class="label">朝向</text>
        <view class="chips">
          <text v-for="item in orientations" :key="item" class="chip" :class="{ active: local.orientation === item }" @tap="toggle('orientation', item)">
            {{ item }}
          </text>
        </view>
      </view>

      <view class="section">
        <text class="label">装修</text>
        <view class="chips">
          <text v-for="item in decorations" :key="item" class="chip" :class="{ active: local.decoration === item }" @tap="toggle('decoration', item)">
            {{ item }}
          </text>
        </view>
      </view>

      <view class="section">
        <text class="label">状态</text>
        <view class="chips">
          <text v-for="item in statuses" :key="String(item.value)" class="chip" :class="{ active: local.status === item.value }" @tap="toggle('status', item.value)">
            {{ item.label }}
          </text>
        </view>
      </view>

      <view class="actions">
        <button class="ghost" @tap="reset">重置</button>
        <button class="primary" @tap="submit">搜索</button>
      </view>
    </view>
  </view>
</template>

<script setup>
import { reactive, watch } from 'vue';
import { ROOM_STATUS_OPTIONS } from '../utils/format';

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  modelValue: {
    type: Object,
    default: () => ({})
  }
});

const emit = defineEmits(['close', 'apply', 'reset']);

const local = reactive({
  minPrice: '',
  maxPrice: '',
  minArea: '',
  maxArea: '',
  layout: '',
  orientation: '',
  decoration: '',
  status: ''
});

const layouts = ['两室', '三室', '四室'];
const orientations = ['南', '东', '西', '北', '南北通透'];
const decorations = ['毛坯', '精装'];
const statuses = ROOM_STATUS_OPTIONS.filter((item) => item.value !== '');

watch(
  () => props.modelValue,
  (value) => {
    Object.keys(local).forEach((key) => {
      local[key] = value?.[key] ?? '';
    });
  },
  { immediate: true, deep: true }
);

function toggle(key, value) {
  local[key] = local[key] === value ? '' : value;
}

function reset() {
  Object.keys(local).forEach((key) => {
    local[key] = '';
  });
  emit('reset');
}

function submit() {
  emit('apply', { ...local });
}
</script>

<style scoped>
.mask {
  position: fixed;
  inset: 0;
  z-index: 80;
  display: flex;
  justify-content: flex-end;
  background: rgba(24, 32, 28, 0.42);
}

.drawer {
  width: 86vw;
  max-width: 680rpx;
  min-height: 100vh;
  padding: 36rpx 28rpx calc(36rpx + env(safe-area-inset-bottom));
  background: #f7f8f5;
  overflow-y: auto;
}

.header,
.actions,
.range {
  display: flex;
  align-items: center;
}

.header {
  justify-content: space-between;
  margin-bottom: 28rpx;
}

.title {
  color: #1d2a24;
  font-size: 36rpx;
  font-weight: 900;
}

.close {
  color: #526059;
  font-size: 26rpx;
  font-weight: 800;
}

.section {
  margin-top: 28rpx;
}

.label {
  display: block;
  margin-bottom: 14rpx;
  color: #1d2a24;
  font-size: 26rpx;
  font-weight: 800;
}

.range {
  gap: 12rpx;
}

.input {
  flex: 1;
  height: 78rpx;
  min-width: 0;
  padding: 0 20rpx;
  border-radius: 8rpx;
  background: #fff;
  border: 1rpx solid rgba(31, 53, 44, 0.1);
  font-size: 26rpx;
}

.dash {
  color: #7b857f;
}

.chips {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
}

.chip {
  min-width: 104rpx;
  padding: 14rpx 18rpx;
  border-radius: 8rpx;
  background: #fff;
  border: 1rpx solid rgba(31, 53, 44, 0.1);
  color: #526059;
  text-align: center;
  font-size: 24rpx;
  font-weight: 800;
}

.chip.active {
  background: #1f352c;
  color: #fff;
}

.actions {
  gap: 16rpx;
  margin-top: 40rpx;
}

.ghost,
.primary {
  flex: 1;
  height: 84rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
  font-weight: 900;
  line-height: 84rpx;
}

.ghost {
  background: #fff;
  color: #1f352c;
  border: 1rpx solid rgba(31, 53, 44, 0.14);
}

.primary {
  background: #1f352c;
  color: #fff;
}
</style>
