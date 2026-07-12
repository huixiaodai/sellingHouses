<template>
  <div v-loading="loading" class="loading-box">
    <slot />
  </div>
</template>

<script setup lang="ts">
defineProps<{
  loading: boolean;
}>();
</script>

<style scoped>
.loading-box {
  min-height: 160px;
}
</style>
