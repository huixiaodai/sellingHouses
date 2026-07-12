<template>
  <div class="unit-page">
    <aside class="building-panel">
      <div class="panel-header">
        <h2>楼盘</h2>
        <el-input v-model="buildingKeyword" placeholder="搜索楼盘" clearable :prefix-icon="Search" />
      </div>
      <div v-loading="buildingLoading" class="building-list">
        <button
          v-for="item in filteredBuildings"
          :key="item.id"
          class="building-item"
          :class="{ active: item.id === selectedBuildingId }"
          type="button"
          @click="selectBuilding(item)"
        >
          <span class="building-name">{{ item.name }}</span>
          <el-tag size="small" :type="item.status === 1 ? 'success' : 'info'">
            {{ getOptionLabel(enabledStatusOptions, item.status) }}
          </el-tag>
        </button>
        <Empty v-if="!buildingLoading && filteredBuildings.length === 0" description="暂无楼盘" />
      </div>
    </aside>

    <main class="unit-main">
      <SearchBar :model="query" @search="search" @reset="handleReset">
        <el-form-item label="楼栋名称">
          <el-input v-model="query.name" placeholder="请输入楼栋名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部状态" clearable>
            <el-option v-for="item in enabledStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
      </SearchBar>

      <section class="table-card">
        <TableToolbar
          title="楼栋管理"
          :description="currentBuilding ? `当前楼盘：${currentBuilding.name}` : '请先选择左侧楼盘'"
          :show-add="Boolean(currentBuilding)"
          @add="openCreate"
          @refresh="fetchList"
        />
        <el-table v-loading="loading" :data="records" row-key="id" stripe>
          <el-table-column prop="name" label="楼栋名称" min-width="180" />
          <el-table-column prop="sortNo" label="排序" width="120" />
          <el-table-column label="状态" width="130">
            <template #default="{ row }">
              <el-switch
                v-model="row.status"
                :active-value="1"
                :inactive-value="0"
                @change="(value: string | number | boolean) => handleStatusChange(row, Number(value))"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="{ row }">
              <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
              <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
            </template>
          </el-table-column>
          <template #empty>
            <Empty :description="currentBuilding ? '该楼盘暂无楼栋' : '请先选择楼盘'" />
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
    </main>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="560px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="96px">
        <el-form-item label="所属楼盘" prop="buildingId">
          <el-select v-model="form.buildingId" placeholder="请选择楼盘" filterable @change="handleFormBuildingChange">
            <el-option v-for="item in buildingOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="楼栋名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入楼栋名称" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortNo" :min="0" :max="9999" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option v-for="item in enabledStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus';
import { Search } from '@element-plus/icons-vue';
import SearchBar from '@/components/SearchBar.vue';
import TableToolbar from '@/components/TableToolbar.vue';
import Pagination from '@/components/Pagination.vue';
import Empty from '@/components/Empty.vue';
import { getBuildingPageApi } from '@/api/building';
import {
  createBuildingUnitApi,
  deleteBuildingUnitApi,
  getBuildingUnitPageApi,
  updateBuildingUnitApi,
  updateBuildingUnitStatusApi
} from '@/api/buildingUnit';
import { useTable } from '@/hooks/useTable';
import { confirmDelete } from '@/utils/confirm';
import { enabledStatusOptions, getOptionLabel } from '@/utils/options';
import type { BuildingVO } from '@/types/building';
import type { BuildingUnitForm, BuildingUnitPageQuery, BuildingUnitVO } from '@/types/buildingUnit';

const defaultForm = (): BuildingUnitForm => ({
  buildingId: undefined,
  name: '',
  sortNo: 0,
  status: 1
});

const buildingOptions = ref<BuildingVO[]>([]);
const buildingKeyword = ref('');
const buildingLoading = ref(false);
const selectedBuildingId = ref<number>();
const originalBuildingId = ref<number>();
const formRef = ref<FormInstance>();
const dialogVisible = ref(false);
const submitLoading = ref(false);
const form = reactive<BuildingUnitForm>(defaultForm());

const { loading, records, total, query, fetchList, search, reset, handlePageChange, handleSizeChange } = useTable<
  BuildingUnitVO,
  Omit<BuildingUnitPageQuery, 'pageNo' | 'pageSize'>
>(
  getBuildingUnitPageApi,
  { buildingId: '', name: '', status: '' }
);

const filteredBuildings = computed(() => {
  const keyword = buildingKeyword.value.trim();
  if (!keyword) {
    return buildingOptions.value;
  }
  return buildingOptions.value.filter((item) => item.name.includes(keyword));
});

const currentBuilding = computed(() => buildingOptions.value.find((item) => item.id === selectedBuildingId.value));
const isEdit = computed(() => Boolean(form.id));
const dialogTitle = computed(() => (isEdit.value ? '编辑楼栋' : '新增楼栋'));

const rules: FormRules<BuildingUnitForm> = {
  buildingId: [{ required: true, message: '请选择楼盘', trigger: 'change' }],
  name: [{ required: true, message: '请输入楼栋名称', trigger: 'blur' }]
};

const loadBuildings = async () => {
  buildingLoading.value = true;
  try {
    const result = await getBuildingPageApi({ pageNo: 1, pageSize: 200, name: '', status: '' });
    buildingOptions.value = result.records;
    if (!selectedBuildingId.value && result.records.length > 0) {
      await selectBuilding(result.records[0]);
    }
  } finally {
    buildingLoading.value = false;
  }
};

const selectBuilding = async (building: BuildingVO) => {
  selectedBuildingId.value = building.id;
  query.buildingId = building.id;
  query.pageNo = 1;
  await fetchList();
};

const handleReset = async () => {
  await reset();
  if (selectedBuildingId.value) {
    query.buildingId = selectedBuildingId.value;
    await fetchList();
  }
};

const openCreate = () => {
  Object.assign(form, defaultForm(), { buildingId: selectedBuildingId.value });
  originalBuildingId.value = selectedBuildingId.value;
  dialogVisible.value = true;
};

const openEdit = (row: BuildingUnitVO) => {
  Object.assign(form, row);
  originalBuildingId.value = row.buildingId;
  dialogVisible.value = true;
};

const handleFormBuildingChange = async (buildingId?: number) => {
  if (!isEdit.value || !originalBuildingId.value || buildingId === originalBuildingId.value) {
    return;
  }
  await ElMessageBox.confirm('确认将该楼栋移动到其他楼盘下吗？', '变更所属楼盘', {
    type: 'warning',
    confirmButtonText: '确认变更',
    cancelButtonText: '取消'
  }).catch(() => {
    form.buildingId = originalBuildingId.value;
  });
};

const handleSubmit = async () => {
  await formRef.value?.validate();
  submitLoading.value = true;
  try {
    if (isEdit.value) {
      await updateBuildingUnitApi(form);
      ElMessage.success('楼栋已更新');
    } else {
      await createBuildingUnitApi(form);
      ElMessage.success('楼栋已新增');
    }
    dialogVisible.value = false;
    if (form.buildingId && form.buildingId !== selectedBuildingId.value) {
      const targetBuilding = buildingOptions.value.find((item) => item.id === form.buildingId);
      if (targetBuilding) {
        await selectBuilding(targetBuilding);
        return;
      }
    }
    fetchList();
  } finally {
    submitLoading.value = false;
  }
};

const handleDelete = async (id: number) => {
  await confirmDelete('确认删除该楼栋吗？');
  await deleteBuildingUnitApi({ id });
  ElMessage.success('删除成功');
  fetchList();
};

const handleStatusChange = async (row: BuildingUnitVO, status: number) => {
  const previous = status === 1 ? 0 : 1;
  try {
    await updateBuildingUnitStatusApi({ id: row.id, status });
    ElMessage.success('状态已更新');
  } catch {
    row.status = previous;
  }
};

onMounted(loadBuildings);
</script>

<style scoped lang="scss">
.unit-page {
  display: grid;
  grid-template-columns: 280px minmax(0, 1fr);
  gap: 18px;
}

.building-panel {
  align-self: start;
  padding: 16px;
  background: #fff;
  border: 1px solid var(--admin-border);
  border-radius: var(--admin-radius);
  box-shadow: var(--admin-shadow);
}

.panel-header {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 12px;

  h2 {
    margin: 0;
    font-size: 18px;
  }
}

.building-list {
  display: flex;
  max-height: calc(100vh - 220px);
  min-height: 280px;
  flex-direction: column;
  gap: 8px;
  overflow-y: auto;
}

.building-item {
  display: flex;
  width: 100%;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  padding: 12px;
  border: 1px solid transparent;
  border-radius: 8px;
  background: #f8fafc;
  color: var(--admin-text);
  cursor: pointer;
  text-align: left;

  &:hover {
    border-color: rgb(29 102 242 / 30%);
    background: #eef5ff;
  }

  &.active {
    border-color: var(--admin-primary);
    background: rgb(29 102 242 / 10%);
  }
}

.building-name {
  min-width: 0;
  overflow: hidden;
  font-weight: 600;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.unit-main {
  min-width: 0;
}

@media (max-width: 960px) {
  .unit-page {
    grid-template-columns: 1fr;
  }

  .building-list {
    max-height: 280px;
  }
}
</style>
