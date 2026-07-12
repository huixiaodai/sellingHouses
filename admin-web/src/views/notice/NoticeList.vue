<template>
  <div>
    <SearchBar :model="query" @search="search" @reset="reset">
      <el-form-item label="标题">
        <el-input v-model="query.title" placeholder="请输入公告标题" clearable />
      </el-form-item>
    </SearchBar>

    <section class="table-card">
      <TableToolbar title="公告管理" description="发布并维护面向不同角色的业务公告" @add="openCreate" @refresh="fetchList" />
      <el-table v-loading="loading" :data="records" row-key="id" stripe>
        <el-table-column prop="title" label="公告标题" min-width="220" show-overflow-tooltip />
        <el-table-column label="可见角色" width="130">
          <template #default="{ row }">{{ getOptionLabel(noticeTargetOptions, row.targetRole) }}</template>
        </el-table-column>
        <el-table-column prop="publisherName" label="发布人" width="130" />
        <el-table-column label="发布时间" width="190">
          <template #default="{ row }">{{ formatDateTimeZh(row.publishTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetail(row.id)">详情</el-button>
            <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
        <template #empty>
          <Empty />
        </template>
      </el-table>
      <Pagination
        :page-no="query.pageNo"
        :page-size="query.pageSize"
        :total="total"
        @page-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </section>

    <el-dialog v-model="dialogVisible" title="新增公告" width="680px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="96px">
        <el-form-item label="公告标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="可见角色" prop="targetRoleCode">
          <el-select v-model="form.targetRoleCode" placeholder="请选择可见角色">
            <el-option v-for="item in noticeTargetOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="公告内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="8" placeholder="请输入公告内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">发布</el-button>
        </div>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="公告详情" size="520px">
      <article v-if="detail" class="notice-detail">
        <h2>{{ detail.title }}</h2>
        <p>
          {{ getOptionLabel(noticeTargetOptions, detail.targetRole) }}
          <span v-if="detail.publishTime"> · {{ formatDateTimeZh(detail.publishTime) }}</span>
        </p>
        <div>{{ detail.content }}</div>
      </article>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { ElMessage, type FormInstance, type FormRules } from 'element-plus';
import SearchBar from '@/components/SearchBar.vue';
import TableToolbar from '@/components/TableToolbar.vue';
import Pagination from '@/components/Pagination.vue';
import Empty from '@/components/Empty.vue';
import { createNoticeApi, deleteNoticeApi, getNoticeDetailApi, getNoticePageApi } from '@/api/notice';
import { useTable } from '@/hooks/useTable';
import { confirmDelete } from '@/utils/confirm';
import { formatDateTimeZh } from '@/utils/date';
import { getOptionLabel, noticeTargetOptions } from '@/utils/options';
import type { NoticeCreateForm, NoticeVO } from '@/types/notice';

const defaultForm = (): NoticeCreateForm => ({
  title: '',
  content: '',
  targetRoleCode: 'ALL'
});

const { loading, records, total, query, fetchList, search, reset, handlePageChange, handleSizeChange } = useTable(
  getNoticePageApi,
  { title: '' }
);
const formRef = ref<FormInstance>();
const dialogVisible = ref(false);
const detailVisible = ref(false);
const submitLoading = ref(false);
const detail = ref<NoticeVO | null>(null);
const form = reactive<NoticeCreateForm>(defaultForm());

const rules: FormRules<NoticeCreateForm> = {
  title: [{ required: true, message: '请输入公告标题', trigger: 'blur' }],
  targetRoleCode: [{ required: true, message: '请选择可见角色', trigger: 'change' }],
  content: [{ required: true, message: '请输入公告内容', trigger: 'blur' }]
};

const openCreate = () => {
  Object.assign(form, defaultForm());
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  await formRef.value?.validate();
  submitLoading.value = true;
  try {
    await createNoticeApi(form);
    ElMessage.success('公告已发布');
    dialogVisible.value = false;
    fetchList();
  } finally {
    submitLoading.value = false;
  }
};

const handleDelete = async (id: number) => {
  await confirmDelete('确认删除该公告吗？');
  await deleteNoticeApi({ id });
  ElMessage.success('删除成功');
  fetchList();
};

const openDetail = async (id: number) => {
  detail.value = await getNoticeDetailApi({ id });
  detailVisible.value = true;
};
</script>

<style scoped lang="scss">
.notice-detail {
  h2 {
    margin: 0 0 8px;
    font-size: 22px;
  }

  p {
    margin: 0 0 20px;
    color: var(--admin-muted);
  }

  div {
    white-space: pre-wrap;
    line-height: 1.8;
  }
}
</style>
