<template>
  <div class="pagination-wrap">
    <el-pagination
      background
      layout="total, sizes, prev, pager, next, jumper"
      :current-page="pageNo"
      :page-size="pageSize"
      :page-sizes="[10, 20, 50, 100]"
      :total="total"
      @current-change="$emit('page-change', $event)"
      @size-change="$emit('size-change', $event)"
    />
  </div>
</template>

<script setup lang="ts">
defineProps<{
  pageNo: number;
  pageSize: number;
  total: number;
}>();

defineEmits<{
  'page-change': [pageNo: number];
  'size-change': [pageSize: number];
}>();
</script>

<style scoped lang="scss">
.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  padding-top: 16px;
}
</style>
