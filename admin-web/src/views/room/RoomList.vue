<template>
  <div class="room-page">
    <aside class="room-tree-card">
      <div class="tree-header">
        <div>
          <h2>房源定位</h2>
          <p>先选择楼盘或楼栋，再维护右侧房源</p>
        </div>
        <el-button :icon="Refresh" circle @click="loadRoomTree" />
      </div>

      <el-input v-model="treeKeyword" placeholder="搜索楼盘/楼栋" clearable :prefix-icon="Search" />

      <el-tree
        ref="treeRef"
        v-loading="treeLoading"
        class="room-tree"
        node-key="key"
        :data="treeData"
        :props="{ label: 'label', children: 'children' }"
        :filter-node-method="filterTreeNode"
        default-expand-all
        highlight-current
        @node-click="handleTreeClick"
      >
        <template #default="{ data }">
          <div class="tree-node">
            <el-icon>
              <OfficeBuilding v-if="data.type === 'building'" />
              <House v-else />
            </el-icon>
            <span>{{ data.label }}</span>
            <el-tag v-if="data.type === 'building'" size="small" :type="data.status === 1 ? 'success' : 'info'">
              {{ data.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </div>
        </template>
        <template #empty>
          <Empty />
        </template>
      </el-tree>
    </aside>

    <main class="room-main">
      <section class="context-card">
        <div>
          <span class="context-label">当前楼盘</span>
          <strong>{{ currentBuildingName }}</strong>
        </div>
        <div>
          <span class="context-label">当前楼栋</span>
          <strong>{{ currentUnitName }}</strong>
        </div>
        <div>
          <span class="context-label">当前视图</span>
          <el-radio-group v-model="viewMode" size="small">
            <el-radio-button label="grid">房态网格</el-radio-button>
            <el-radio-button label="table">表格维护</el-radio-button>
          </el-radio-group>
        </div>
      </section>

      <SearchBar :model="query" @search="search" @reset="handleReset">
        <el-form-item label="房号">
          <el-input v-model="query.roomNo" placeholder="请输入房号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部状态" clearable>
            <el-option v-for="item in roomStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
      </SearchBar>

      <section class="table-card">
        <TableToolbar
          title="房源管理"
          :description="toolbarDescription"
          @add="openCreate"
          @refresh="fetchList"
        />

        <div v-if="viewMode === 'grid'" v-loading="loading" class="room-grid-view">
          <template v-if="floorGroups.length">
            <section v-for="group in floorGroups" :key="group.floorNo" class="floor-section">
              <div class="floor-title">
                <strong>{{ group.floorNo }} 层</strong>
                <span>{{ group.rooms.length }} 套房源</span>
              </div>
              <div class="room-grid">
                <button
                  v-for="room in group.rooms"
                  :key="room.id"
                  class="room-card"
                  :class="`status-${room.status}`"
                  type="button"
                  @click="openDetail(room.id)"
                >
                  <span class="room-no">{{ room.roomNo }}</span>
                  <span class="room-meta">{{ room.area || '-' }}㎡</span>
                  <span class="room-price">￥{{ formatPrice(room.price) }}</span>
                  <el-tag size="small" :type="getOptionType(roomStatusOptions, room.status)">
                    {{ getOptionLabel(roomStatusOptions, room.status) }}
                  </el-tag>
                </button>
              </div>
            </section>
          </template>
          <Empty v-else />
        </div>

        <template v-else>
          <el-table v-loading="loading" :data="records" row-key="id" stripe>
            <el-table-column label="图片" width="104">
              <template #default="{ row }">
                <el-image
                  v-if="row.cover"
                  class="room-cover"
                  :src="row.cover"
                  fit="cover"
                  :preview-src-list="[row.cover]"
                  preview-teleported
                />
                <div v-else class="room-cover-placeholder">无图</div>
              </template>
            </el-table-column>
            <el-table-column prop="buildingName" label="楼盘" min-width="150" show-overflow-tooltip />
            <el-table-column prop="unitName" label="楼栋" min-width="110" show-overflow-tooltip />
            <el-table-column prop="roomNo" label="房号" width="110" />
            <el-table-column prop="floorNo" label="楼层" width="90" />
            <el-table-column prop="area" label="面积" width="110">
              <template #default="{ row }">{{ row.area }}㎡</template>
            </el-table-column>
            <el-table-column prop="price" label="价格" width="140">
              <template #default="{ row }">￥{{ formatPrice(row.price) }}</template>
            </el-table-column>
            <el-table-column label="状态" width="138">
              <template #default="{ row }">
                <el-select
                  v-model="row.status"
                  size="small"
                  @change="(value: string | number | boolean) => handleStatusChange(row, Number(value))"
                >
                  <el-option v-for="item in roomStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="300" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" @click="openDetail(row.id)">详情</el-button>
                <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
                <el-button link type="primary" @click="openPrice(row)">改价</el-button>
                <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
              </template>
            </el-table-column>
            <template #empty>
              <Empty />
            </template>
          </el-table>
        </template>

        <Pagination
          :page-no="query.pageNo"
          :page-size="query.pageSize"
          :total="total"
          @page-change="handlePageChange"
          @size-change="handleSizeChange"
        />
      </section>
    </main>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="820px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="96px">
        <div class="form-grid">
          <el-form-item label="所属楼盘" prop="buildingId">
            <el-select v-model="form.buildingId" placeholder="请选择楼盘" filterable @change="handleFormBuildingChange">
              <el-option v-for="item in buildingOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="所属楼栋" prop="unitId">
            <el-select v-model="form.unitId" placeholder="请选择楼栋" filterable>
              <el-option v-for="item in formUnitOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="房号" prop="roomNo">
            <el-input v-model="form.roomNo" placeholder="请输入房号" />
          </el-form-item>
          <el-form-item label="楼层" prop="floorNo">
            <el-input-number v-model="form.floorNo" :min="1" :max="200" />
          </el-form-item>
          <el-form-item label="面积" prop="area">
            <el-input-number v-model="form.area" :precision="2" :min="0" />
          </el-form-item>
          <el-form-item label="价格" prop="price">
            <el-input-number v-model="form.price" :precision="2" :min="0" />
          </el-form-item>
          <el-form-item label="封面图" class="full-row">
            <ImageUpload :model-value="form.cover || ''" @update:model-value="(value) => (form.cover = String(value))" />
          </el-form-item>
          <el-form-item label="房源图库" class="full-row">
            <ImageUpload v-model="roomImageList" multiple />
          </el-form-item>
          <el-form-item label="户型">
            <el-input v-model="form.layout" placeholder="例如：三室一厅" />
          </el-form-item>
          <el-form-item label="朝向">
            <el-input v-model="form.orientation" placeholder="例如：南北通透" />
          </el-form-item>
          <el-form-item label="装修">
            <el-input v-model="form.decoration" placeholder="例如：精装" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="form.status">
              <el-option v-for="item in roomStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="备注" class="full-row">
            <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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

    <el-dialog v-model="priceVisible" title="修改房源价格" width="420px">
      <el-form :model="priceForm" label-width="88px">
        <el-form-item label="新价格">
          <el-input-number v-model="priceForm.price" :precision="2" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="priceVisible = false">取消</el-button>
          <el-button type="primary" @click="handlePriceSubmit">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="房源详情" size="560px">
      <div v-if="detail" class="detail-content">
        <div class="detail-images">
          <el-image
            v-if="detail.cover"
            class="detail-cover"
            :src="detail.cover"
            fit="cover"
            :preview-src-list="[detail.cover, ...detailImageList]"
            preview-teleported
          />
          <div v-else class="detail-cover-placeholder">暂无封面</div>
          <div v-if="detailImageList.length" class="detail-gallery">
            <el-image
              v-for="(url, index) in detailImageList"
              :key="url"
              class="gallery-thumb"
              :src="url"
              fit="cover"
              :preview-src-list="detailImageList"
              :initial-index="index"
              preview-teleported
            />
          </div>
        </div>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="楼盘">{{ detail.buildingName || detail.buildingId }}</el-descriptions-item>
          <el-descriptions-item label="楼栋">{{ detail.unitName || detail.unitId }}</el-descriptions-item>
          <el-descriptions-item label="房号">{{ detail.roomNo }}</el-descriptions-item>
          <el-descriptions-item label="楼层">{{ detail.floorNo }}</el-descriptions-item>
          <el-descriptions-item label="面积">{{ detail.area }}㎡</el-descriptions-item>
          <el-descriptions-item label="价格">￥{{ formatPrice(detail.price) }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getOptionType(roomStatusOptions, detail.status)">
              {{ getOptionLabel(roomStatusOptions, detail.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="户型">{{ detail.layout || '-' }}</el-descriptions-item>
          <el-descriptions-item label="朝向">{{ detail.orientation || '-' }}</el-descriptions-item>
          <el-descriptions-item label="装修">{{ detail.decoration || '-' }}</el-descriptions-item>
          <el-descriptions-item label="备注">{{ detail.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onMounted, reactive, ref, watch } from 'vue';
import { ElMessage, ElMessageBox, type FormInstance, type FormRules, type TreeInstance } from 'element-plus';
import { House, OfficeBuilding, Refresh, Search } from '@element-plus/icons-vue';
import SearchBar from '@/components/SearchBar.vue';
import TableToolbar from '@/components/TableToolbar.vue';
import Pagination from '@/components/Pagination.vue';
import Empty from '@/components/Empty.vue';
import ImageUpload from '@/components/ImageUpload.vue';
import { getBuildingPageApi } from '@/api/building';
import { listBuildingUnitsByBuildingApi } from '@/api/buildingUnit';
import {
  createRoomApi,
  deleteRoomApi,
  getRoomDetailApi,
  getRoomPageApi,
  updateRoomApi,
  updateRoomPriceApi,
  updateRoomStatusApi
} from '@/api/room';
import { useTable } from '@/hooks/useTable';
import { confirmDelete } from '@/utils/confirm';
import { parseImageList, stringifyImageList } from '@/utils/image';
import { getOptionLabel, getOptionType, roomStatusOptions } from '@/utils/options';
import type { BuildingVO } from '@/types/building';
import type { BuildingUnitVO } from '@/types/buildingUnit';
import type { RoomForm, RoomPageQuery, RoomVO } from '@/types/room';

type ViewMode = 'grid' | 'table';
type TreeNodeType = 'building' | 'unit';

interface RoomTreeNode {
  key: string;
  id: number;
  label: string;
  type: TreeNodeType;
  buildingId: number;
  buildingName: string;
  unitId?: number;
  unitName?: string;
  status?: number;
  children?: RoomTreeNode[];
}

interface FloorGroup {
  floorNo: number;
  rooms: RoomVO[];
}

const defaultForm = (): RoomForm => ({
  id: undefined,
  buildingId: undefined,
  unitId: undefined,
  roomNo: '',
  floorNo: undefined,
  area: undefined,
  price: undefined,
  cover: '',
  images: '',
  layout: '',
  orientation: '',
  decoration: '',
  status: 1,
  remark: ''
});

const buildingOptions = ref<BuildingVO[]>([]);
const formUnitOptions = ref<BuildingUnitVO[]>([]);
const treeData = ref<RoomTreeNode[]>([]);
const treeRef = ref<TreeInstance>();
const treeKeyword = ref('');
const treeLoading = ref(false);
const selectedNode = ref<RoomTreeNode | null>(null);
const viewMode = ref<ViewMode>('grid');
const detail = ref<RoomVO | null>(null);
const roomImageList = ref<string[]>([]);
const originalScope = ref<{ buildingId?: number; unitId?: number }>({});

const { loading, records, total, query, fetchList, search, reset, handlePageChange, handleSizeChange } = useTable<
  RoomVO,
  Omit<RoomPageQuery, 'pageNo' | 'pageSize'>
>(getRoomPageApi, { buildingId: '', unitId: '', floorNo: '', roomNo: '', status: '' });

const formRef = ref<FormInstance>();
const dialogVisible = ref(false);
const detailVisible = ref(false);
const priceVisible = ref(false);
const submitLoading = ref(false);
const form = reactive<RoomForm>(defaultForm());
const priceForm = reactive({ id: 0, price: 0 });
const isEdit = computed(() => Boolean(form.id));
const dialogTitle = computed(() => (isEdit.value ? '编辑房源' : '新增房源'));
const detailImageList = computed(() => parseImageList(detail.value?.images));
const currentBuildingName = computed(() => selectedNode.value?.buildingName || '全部楼盘');
const currentUnitName = computed(() => selectedNode.value?.unitName || (selectedNode.value ? '全部楼栋' : '未选择'));
const toolbarDescription = computed(() => {
  const buildingText = selectedNode.value?.buildingName ? `当前楼盘：${selectedNode.value.buildingName}` : '当前查看全部楼盘';
  const unitText = selectedNode.value?.unitName ? `，楼栋：${selectedNode.value.unitName}` : '';
  return `${buildingText}${unitText}，共 ${total.value} 套房源`;
});

const floorGroups = computed<FloorGroup[]>(() => {
  const groupMap = new Map<number, RoomVO[]>();
  records.value.forEach((room) => {
    const floorNo = room.floorNo || 0;
    if (!groupMap.has(floorNo)) {
      groupMap.set(floorNo, []);
    }
    groupMap.get(floorNo)?.push(room);
  });

  return Array.from(groupMap.entries())
    .sort(([a], [b]) => b - a)
    .map(([floorNo, rooms]) => ({
      floorNo,
      rooms: rooms.sort((a, b) => String(a.roomNo).localeCompare(String(b.roomNo), 'zh-CN', { numeric: true }))
    }));
});

const rules: FormRules<RoomForm> = {
  buildingId: [{ required: true, message: '请选择楼盘', trigger: 'change' }],
  unitId: [{ required: true, message: '请选择楼栋', trigger: 'change' }],
  roomNo: [{ required: true, message: '请输入房号', trigger: 'blur' }],
  floorNo: [{ required: true, message: '请输入楼层', trigger: 'change' }],
  area: [{ required: true, message: '请输入面积', trigger: 'change' }],
  price: [{ required: true, message: '请输入价格', trigger: 'change' }]
};

watch(treeKeyword, (keyword) => {
  treeRef.value?.filter(keyword);
});

const loadRoomTree = async () => {
  treeLoading.value = true;
  try {
    const result = await getBuildingPageApi({ pageNo: 1, pageSize: 200, name: '', status: '' });
    buildingOptions.value = result.records || [];

    const nodes = await Promise.all(
      buildingOptions.value.map(async (building) => {
        const units = await listBuildingUnitsByBuildingApi(building.id);
        return {
          key: `building-${building.id}`,
          id: building.id,
          label: building.name,
          type: 'building' as const,
          buildingId: building.id,
          buildingName: building.name,
          status: building.status,
          children: units.map((unit) => ({
            key: `unit-${unit.id}`,
            id: unit.id,
            label: unit.name,
            type: 'unit' as const,
            buildingId: building.id,
            buildingName: building.name,
            unitId: unit.id,
            unitName: unit.name,
            status: unit.status
          }))
        };
      })
    );

    treeData.value = nodes;
    if (!selectedNode.value && nodes.length) {
      await selectTreeNode(nodes[0]);
    }
  } finally {
    treeLoading.value = false;
  }
};

const selectTreeNode = async (node: RoomTreeNode) => {
  selectedNode.value = node;
  query.buildingId = node.buildingId;
  query.unitId = node.type === 'unit' ? node.unitId || '' : '';
  query.pageNo = 1;
  await nextTick();
  treeRef.value?.setCurrentKey(node.key);
  await fetchList();
};

const handleTreeClick = (node: RoomTreeNode) => {
  selectTreeNode(node);
};

const filterTreeNode = (keyword: string, data: RoomTreeNode) => {
  if (!keyword) {
    return true;
  }
  return data.label.toLowerCase().includes(keyword.toLowerCase());
};

const handleFormBuildingChange = async (buildingId?: number) => {
  form.unitId = undefined;
  formUnitOptions.value = buildingId ? await listBuildingUnitsByBuildingApi(buildingId) : [];
};

const handleReset = async () => {
  await reset();
  if (selectedNode.value) {
    query.buildingId = selectedNode.value.buildingId;
    query.unitId = selectedNode.value.type === 'unit' ? selectedNode.value.unitId || '' : '';
    await fetchList();
  }
};

const openCreate = async () => {
  Object.assign(form, defaultForm());
  roomImageList.value = [];
  originalScope.value = {};

  if (selectedNode.value) {
    form.buildingId = selectedNode.value.buildingId;
    form.unitId = selectedNode.value.type === 'unit' ? selectedNode.value.unitId : undefined;
    formUnitOptions.value = await listBuildingUnitsByBuildingApi(selectedNode.value.buildingId);
  } else {
    formUnitOptions.value = [];
  }

  dialogVisible.value = true;
  await nextTick();
  formRef.value?.clearValidate();
};

const openEdit = async (row: RoomVO) => {
  Object.assign(form, row);
  originalScope.value = { buildingId: row.buildingId, unitId: row.unitId };
  roomImageList.value = parseImageList(row.images);
  formUnitOptions.value = row.buildingId ? await listBuildingUnitsByBuildingApi(row.buildingId) : [];
  dialogVisible.value = true;
};

const confirmHierarchyChange = async () => {
  if (!isEdit.value) {
    return;
  }
  const buildingChanged = originalScope.value.buildingId !== form.buildingId;
  const unitChanged = originalScope.value.unitId !== form.unitId;
  if (!buildingChanged && !unitChanged) {
    return;
  }
  await ElMessageBox.confirm('你正在变更该房源所属楼盘或楼栋，确认保存吗？', '确认变更归属', {
    type: 'warning',
    confirmButtonText: '确认保存',
    cancelButtonText: '再检查一下'
  });
};

const handleSubmit = async () => {
  await formRef.value?.validate();
  await confirmHierarchyChange();
  submitLoading.value = true;
  try {
    form.images = stringifyImageList(roomImageList.value);
    if (isEdit.value) {
      await updateRoomApi(form);
      ElMessage.success('房源已更新');
    } else {
      await createRoomApi(form);
      ElMessage.success('房源已新增');
    }
    dialogVisible.value = false;
    fetchList();
  } finally {
    submitLoading.value = false;
  }
};

const handleDelete = async (id: number) => {
  await confirmDelete('确认删除该房源吗？');
  await deleteRoomApi({ id });
  ElMessage.success('删除成功');
  fetchList();
};

const handleStatusChange = async (row: RoomVO, status: number) => {
  try {
    await updateRoomStatusApi({ id: row.id, status });
    ElMessage.success('状态已更新');
  } catch {
    fetchList();
  }
};

const openPrice = (row: RoomVO) => {
  priceForm.id = row.id;
  priceForm.price = Number(row.price);
  priceVisible.value = true;
};

const handlePriceSubmit = async () => {
  await updateRoomPriceApi(priceForm);
  ElMessage.success('价格已更新');
  priceVisible.value = false;
  fetchList();
};

const openDetail = async (id: number) => {
  detail.value = await getRoomDetailApi({ id });
  detailVisible.value = true;
};

const formatPrice = (price?: number) => {
  if (price === undefined || price === null) {
    return '-';
  }
  return Number(price).toLocaleString('zh-CN', { minimumFractionDigits: 0, maximumFractionDigits: 2 });
};

onMounted(loadRoomTree);
</script>

<style scoped lang="scss">
.room-page {
  display: grid;
  grid-template-columns: 300px minmax(0, 1fr);
  gap: 18px;
}

.room-tree-card,
.context-card,
.table-card {
  border: 1px solid var(--admin-border);
  border-radius: var(--admin-radius);
  background: #fff;
  box-shadow: var(--admin-shadow);
}

.room-tree-card {
  align-self: start;
  position: sticky;
  top: 18px;
  display: grid;
  gap: 14px;
  padding: 18px;
}

.tree-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;

  h2 {
    margin: 0;
    font-size: 18px;
    font-weight: 700;
  }

  p {
    margin: 5px 0 0;
    color: var(--admin-muted);
    font-size: 13px;
  }
}

.room-tree {
  min-height: 320px;

  :deep(.el-tree-node__content) {
    height: 38px;
    border-radius: 8px;
  }

  :deep(.el-tree-node.is-current > .el-tree-node__content) {
    color: var(--admin-primary);
    background: #ecf5ff;
  }
}

.tree-node {
  display: flex;
  align-items: center;
  min-width: 0;
  gap: 8px;

  span {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.room-main {
  display: grid;
  min-width: 0;
  gap: 16px;
}

.context-card {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 14px;
  padding: 16px 18px;

  > div {
    display: grid;
    gap: 6px;
  }

  strong {
    overflow: hidden;
    color: var(--admin-text);
    font-size: 16px;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.context-label {
  color: var(--admin-muted);
  font-size: 12px;
}

.room-grid-view {
  min-height: 320px;
}

.floor-section + .floor-section {
  margin-top: 18px;
}

.floor-title {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;

  strong {
    font-size: 16px;
  }

  span {
    color: var(--admin-muted);
    font-size: 13px;
  }
}

.room-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 12px;
}

.room-card {
  display: grid;
  min-height: 126px;
  padding: 14px;
  border: 1px solid #d9e2ef;
  border-left-width: 4px;
  border-radius: 8px;
  background: #fff;
  cursor: pointer;
  gap: 8px;
  text-align: left;
  transition:
    border-color 0.16s ease,
    box-shadow 0.16s ease,
    transform 0.16s ease;

  &:hover {
    border-color: var(--admin-primary);
    box-shadow: 0 10px 22px rgb(15 23 42 / 10%);
    transform: translateY(-2px);
  }

  &.status-1 {
    border-left-color: #22c55e;
  }

  &.status-2 {
    border-left-color: #f59e0b;
  }

  &.status-3 {
    border-left-color: #ef4444;
  }

  &.status-4 {
    border-left-color: #94a3b8;
    background: #f8fafc;
  }
}

.room-no {
  color: var(--admin-text);
  font-size: 18px;
  font-weight: 700;
}

.room-meta,
.room-price {
  color: #475569;
  font-size: 13px;
}

.room-cover,
.room-cover-placeholder {
  width: 72px;
  height: 52px;
  border-radius: 8px;
}

.room-cover {
  display: block;
}

.room-cover-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px dashed var(--admin-border);
  color: var(--admin-muted);
  font-size: 12px;
  background: #f8fafc;
}

.detail-content {
  display: grid;
  gap: 18px;
}

.detail-images {
  display: grid;
  gap: 12px;
}

.detail-cover,
.detail-cover-placeholder {
  width: 100%;
  height: 220px;
  border-radius: 8px;
}

.detail-cover-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px dashed var(--admin-border);
  color: var(--admin-muted);
  background: #f8fafc;
}

.detail-gallery {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(92px, 1fr));
  gap: 10px;
}

.gallery-thumb {
  width: 100%;
  height: 68px;
  border-radius: 8px;
}

@media (max-width: 980px) {
  .room-page {
    grid-template-columns: 1fr;
  }

  .room-tree-card {
    position: static;
  }

  .context-card {
    grid-template-columns: 1fr;
  }
}
</style>
