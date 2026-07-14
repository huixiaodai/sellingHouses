<template>
  <view class="search-wrap">
    <input
      class="search-input"
      :value="modelValue"
      :placeholder="placeholder"
      placeholder-class="placeholder"
      confirm-type="search"
      @input="handleInput"
      @confirm="handleSearch"
    />
    <button class="search-button" @tap="handleSearch">搜索</button>
  </view>
</template>

<script setup>
const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  placeholder: {
    type: String,
    default: '输入房号快速定位'
  }
});

const emit = defineEmits(['update:modelValue', 'search']);

function handleInput(event) {
  emit('update:modelValue', event.detail.value);
}

function handleSearch() {
  emit('search', props.modelValue);
}
</script>

<style scoped>
.search-wrap {
  display: grid;
  grid-template-columns: 1fr 116rpx;
  gap: 14rpx;
  align-items: center;
}

.search-input {
  height: 72rpx;
  min-width: 0;
  padding: 0 24rpx;
  border-radius: 8rpx;
  border: 1rpx solid rgba(31, 53, 44, 0.12);
  background: #fff;
  color: #1d2a24;
  font-size: 28rpx;
}

.placeholder {
  color: #9aa39d;
}

.search-button {
  height: 72rpx;
  margin: 0;
  border-radius: 8rpx;
  background: #1f352c;
  color: #fff;
  font-size: 26rpx;
  font-weight: 800;
  line-height: 72rpx;
}
</style>
