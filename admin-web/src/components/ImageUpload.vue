<template>
  <div class="image-upload">
    <div v-if="imageList.length" class="image-list">
      <div v-for="(url, index) in imageList" :key="url" class="image-item">
        <el-image class="thumb" :src="url" fit="cover" :preview-src-list="imageList" :initial-index="index" preview-teleported />
        <div class="image-actions">
          <el-tooltip content="预览" placement="top">
            <el-button circle size="small" :icon="View" @click.stop="preview(index)" />
          </el-tooltip>
          <template v-if="multiple">
            <el-tooltip content="前移" placement="top">
              <el-button circle size="small" :icon="ArrowLeft" :disabled="index === 0" @click="moveImage(index, -1)" />
            </el-tooltip>
            <el-tooltip content="后移" placement="top">
              <el-button
                circle
                size="small"
                :icon="ArrowRight"
                :disabled="index === imageList.length - 1"
                @click="moveImage(index, 1)"
              />
            </el-tooltip>
          </template>
          <el-tooltip content="删除" placement="top">
            <el-button circle size="small" type="danger" :icon="Delete" @click="removeImage(index)" />
          </el-tooltip>
        </div>
      </div>
    </div>

    <el-upload
      v-if="multiple || imageList.length === 0"
      class="upload-trigger"
      drag
      action="#"
      accept="image/jpeg,image/png,image/webp,image/gif"
      :show-file-list="false"
      :http-request="handleUpload"
      :before-upload="beforeUpload"
    >
      <el-icon class="upload-icon"><UploadFilled /></el-icon>
      <div class="upload-text">{{ multiple ? '点击或拖拽上传图片' : '上传封面图' }}</div>
      <template #tip>
        <div class="upload-tip">支持 JPG、PNG、WEBP、GIF，单张不超过 5MB</div>
      </template>
    </el-upload>

    <el-image-viewer
      v-if="previewVisible"
      :url-list="imageList"
      :initial-index="previewIndex"
      teleported
      @close="previewVisible = false"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { ElMessage, type UploadProps, type UploadRequestOptions } from 'element-plus';
import { ArrowLeft, ArrowRight, Delete, UploadFilled, View } from '@element-plus/icons-vue';
import { uploadImageApi } from '@/api/upload';

const props = withDefaults(
  defineProps<{
    modelValue: string | string[];
    multiple?: boolean;
  }>(),
  {
    multiple: false
  }
);

const emit = defineEmits<{
  'update:modelValue': [value: string | string[]];
}>();

const previewVisible = ref(false);
const previewIndex = ref(0);

const imageList = computed(() => {
  if (Array.isArray(props.modelValue)) {
    return props.modelValue;
  }
  return props.modelValue ? [props.modelValue] : [];
});

const emitList = (list: string[]) => {
  emit('update:modelValue', props.multiple ? list : list[0] || '');
};

const beforeUpload: UploadProps['beforeUpload'] = (rawFile) => {
  const allowedTypes = ['image/jpeg', 'image/png', 'image/webp', 'image/gif'];
  if (!allowedTypes.includes(rawFile.type)) {
    ElMessage.error('仅支持 JPG、PNG、WEBP、GIF 图片');
    return false;
  }
  if (rawFile.size > 5 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过 5MB');
    return false;
  }
  return true;
};

const handleUpload = async (options: UploadRequestOptions) => {
  const result = await uploadImageApi(options.file);
  const nextList = props.multiple ? [...imageList.value, result.url] : [result.url];
  emitList(nextList);
  ElMessage.success('图片上传成功');
};

const removeImage = (index: number) => {
  const nextList = [...imageList.value];
  nextList.splice(index, 1);
  emitList(nextList);
};

const moveImage = (index: number, offset: number) => {
  const nextIndex = index + offset;
  const nextList = [...imageList.value];
  const current = nextList[index];
  nextList[index] = nextList[nextIndex];
  nextList[nextIndex] = current;
  emitList(nextList);
};

const preview = (index: number) => {
  previewIndex.value = index;
  previewVisible.value = true;
};
</script>

<style scoped lang="scss">
.image-upload {
  width: 100%;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 12px;
}

.image-item {
  position: relative;
  width: 132px;
  height: 96px;
  overflow: hidden;
  border: 1px solid var(--admin-border);
  border-radius: 8px;
  background: #f8fafc;

  &:hover .image-actions {
    opacity: 1;
  }
}

.thumb {
  width: 100%;
  height: 100%;
}

.image-actions {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  background: rgb(15 23 42 / 48%);
  opacity: 0;
  transition: opacity 0.16s ease;
}

.upload-trigger {
  width: 240px;

  :deep(.el-upload-dragger) {
    padding: 18px 16px;
    border-radius: 8px;
  }
}

.upload-icon {
  color: var(--admin-primary);
  font-size: 28px;
}

.upload-text {
  margin-top: 8px;
  color: var(--admin-text);
}

.upload-tip {
  color: var(--admin-muted);
  font-size: 12px;
}
</style>
