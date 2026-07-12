<template>
  <div class="not-found page-card">
    <el-result icon="warning" title="404" sub-title="页面不存在或已被移动">
      <template #extra>
        <el-button type="primary" @click="$router.replace('/building')">返回楼盘管理</el-button>
      </template>
    </el-result>
  </div>
</template>

<style scoped>
.not-found {
  min-height: calc(100vh - 108px);
}
</style>
