<template>
  <div>
    <SearchBar :model="query" @search="search" @reset="reset">
      <el-form-item label="楼盘名称">
        <el-input v-model="query.name" placeholder="请输入楼盘名称" clearable />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" placeholder="全部状态" clearable>
          <el-option v-for="item in enabledStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
    </SearchBar>

    <section class="table-card">
      <TableToolbar title="楼盘管理" description="维护项目基础资料、地址、开盘交付信息" @add="openCreate" @refresh="fetchList" />
      <el-table v-loading="loading" :data="records" row-key="id" stripe>
        <el-table-column prop="name" label="楼盘名称" min-width="160" />
        <el-table-column prop="developer" label="开发商" min-width="140" show-overflow-tooltip />
        <el-table-column prop="address" label="地址" min-width="220" show-overflow-tooltip />
        <el-table-column label="开盘时间" min-width="190">
          <template #default="{ row }">{{ formatDateTimeZh(row.openingTime) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="110">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="(value: string | number | boolean) => handleStatusChange(row, Number(value))"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetail(row.id)">详情</el-button>
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="720px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="96px">
        <div class="form-grid">
          <el-form-item label="楼盘名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入楼盘名称" />
          </el-form-item>
          <el-form-item label="开发商">
            <el-input v-model="form.developer" placeholder="请输入开发商" />
          </el-form-item>
          <el-form-item label="楼盘地址" prop="address" class="full-row">
            <el-input v-model="form.address" placeholder="请输入楼盘地址" />
          </el-form-item>
          <el-form-item label="封面图" class="full-row">
            <ImageUpload :model-value="form.cover || ''" @update:model-value="(value) => (form.cover = String(value))" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="form.status">
              <el-option v-for="item in enabledStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="开盘时间">
            <el-date-picker
              v-model="form.openingTime"
              type="datetime"
              format="YYYY年MM月DD日 HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              placeholder="请选择开盘时间"
            />
          </el-form-item>
          <el-form-item label="交房时间">
            <el-date-picker
              v-model="form.deliveryTime"
              type="datetime"
              format="YYYY年MM月DD日 HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              placeholder="请选择交房时间"
            />
          </el-form-item>
          <el-form-item label="轮播图" class="full-row">
            <ImageUpload v-model="bannerImageList" multiple />
          </el-form-item>
          <el-form-item label="楼盘介绍" class="full-row">
            <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入楼盘介绍" />
          </el-form-item>
        </div>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="楼盘详情" size="520px">
      <el-descriptions v-if="detail" :column="1" border>
        <el-descriptions-item label="楼盘名称">{{ detail.name }}</el-descriptions-item>
        <el-descriptions-item label="开发商">{{ detail.developer || '-' }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ detail.address }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ getOptionLabel(enabledStatusOptions, detail.status) }}</el-descriptions-item>
        <el-descriptions-item label="开盘时间">{{ formatDateTimeZh(detail.openingTime) }}</el-descriptions-item>
        <el-descriptions-item label="交房时间">{{ formatDateTimeZh(detail.deliveryTime) }}</el-descriptions-item>
        <el-descriptions-item label="介绍">{{ detail.description || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue';
import { ElMessage, type FormInstance, type FormRules } from 'element-plus';
import SearchBar from '@/components/SearchBar.vue';
import TableToolbar from '@/components/TableToolbar.vue';
import Pagination from '@/components/Pagination.vue';
import Empty from '@/components/Empty.vue';
import ImageUpload from '@/components/ImageUpload.vue';
import { useTable } from '@/hooks/useTable';
import {
  createBuildingApi,
  deleteBuildingApi,
  getBuildingDetailApi,
  getBuildingPageApi,
  updateBuildingApi,
  updateBuildingStatusApi
} from '@/api/building';
import { confirmDelete } from '@/utils/confirm';
import { formatDateTimeZh } from '@/utils/date';
import { parseImageList, stringifyImageList } from '@/utils/image';
import { enabledStatusOptions, getOptionLabel } from '@/utils/options';
import type { BuildingForm, BuildingPageQuery, BuildingVO } from '@/types/building';

const defaultForm = (): BuildingForm => ({
  name: '',
  address: '',
  status: 1
});

const { loading, records, total, query, fetchList, search, reset, handlePageChange, handleSizeChange } = useTable(
  getBuildingPageApi,
  { name: '', status: '' } satisfies Omit<BuildingPageQuery, 'pageNo' | 'pageSize'>
);

const formRef = ref<FormInstance>();
const dialogVisible = ref(false);
const detailVisible = ref(false);
const submitLoading = ref(false);
const form = reactive<BuildingForm>(defaultForm());
const bannerImageList = ref<string[]>([]);
const detail = ref<BuildingVO | null>(null);
const isEdit = computed(() => Boolean(form.id));
const dialogTitle = computed(() => (isEdit.value ? '编辑楼盘' : '新增楼盘'));

const rules: FormRules<BuildingForm> = {
  name: [{ required: true, message: '请输入楼盘名称', trigger: 'blur' }],
  address: [{ required: true, message: '请输入楼盘地址', trigger: 'blur' }]
};

const openCreate = () => {
  Object.assign(form, defaultForm());
  bannerImageList.value = [];
  dialogVisible.value = true;
};

const openEdit = (row: BuildingVO) => {
  Object.assign(form, row);
  bannerImageList.value = parseImageList(row.bannerImages);
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  await formRef.value?.validate();
  submitLoading.value = true;
  try {
    form.bannerImages = stringifyImageList(bannerImageList.value);
    if (isEdit.value) {
      await updateBuildingApi(form);
      ElMessage.success('楼盘已更新');
    } else {
      await createBuildingApi(form);
      ElMessage.success('楼盘已新增');
    }
    dialogVisible.value = false;
    fetchList();
  } finally {
    submitLoading.value = false;
  }
};

const handleDelete = async (id: number) => {
  await confirmDelete('确认删除该楼盘吗？');
  await deleteBuildingApi({ id });
  ElMessage.success('删除成功');
  fetchList();
};

const handleStatusChange = async (row: BuildingVO, status: number) => {
  const previous = status === 1 ? 0 : 1;
  try {
    await updateBuildingStatusApi({ id: row.id, status });
    ElMessage.success('状态已更新');
  } catch {
    row.status = previous;
  }
};

const openDetail = async (id: number) => {
  detail.value = await getBuildingDetailApi({ id });
  detailVisible.value = true;
};
</script>
