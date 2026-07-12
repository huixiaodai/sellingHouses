<template>
  <div class="table-toolbar">
    <div>
      <h2>{{ title }}</h2>
      <p v-if="description">{{ description }}</p>
    </div>
    <div class="toolbar-actions">
      <el-button :icon="Refresh" @click="$emit('refresh')">刷新</el-button>
      <el-button v-if="showAdd" type="primary" :icon="Plus" @click="$emit('add')">新增</el-button>
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
import { Plus, Refresh } from '@element-plus/icons-vue';

withDefaults(
  defineProps<{
    title: string;
    description?: string;
    showAdd?: boolean;
  }>(),
  {
    description: '',
    showAdd: true
  }
);

defineEmits<{
  refresh: [];
  add: [];
}>();
</script>

<style scoped lang="scss">
.table-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 14px;
  margin-bottom: 14px;

  h2 {
    margin: 0;
    font-size: 18px;
    font-weight: 700;
  }

  p {
    margin: 4px 0 0;
    color: var(--admin-muted);
    font-size: 13px;
  }
}

.toolbar-actions {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-end;
  gap: 10px;
}

@media (max-width: 640px) {
  .table-toolbar {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>
