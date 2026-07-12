<template>
  <section class="search-bar">
    <el-form :model="model" inline label-width="80px" @submit.prevent>
      <slot />
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="$emit('search')">搜索</el-button>
        <el-button :icon="Refresh" @click="$emit('reset')">重置</el-button>
      </el-form-item>
    </el-form>
  </section>
</template>

<script setup lang="ts">
import { Refresh, Search } from '@element-plus/icons-vue';

defineProps<{
  model: Record<string, unknown>;
}>();

defineEmits<{
  search: [];
  reset: [];
}>();
</script>

<style scoped lang="scss">
.search-bar {
  padding: 18px 18px 0;
  background: #fff;
  border: 1px solid var(--admin-border);
  border-radius: var(--admin-radius);
  box-shadow: var(--admin-shadow);
}
</style>
