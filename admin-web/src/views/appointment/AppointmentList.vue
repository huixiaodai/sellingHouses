<template>
  <div>
    <SearchBar :model="query" @search="search" @reset="reset">
      <el-form-item label="楼盘ID">
        <el-input v-model="query.buildingId" placeholder="请输入楼盘ID" clearable />
      </el-form-item>
      <el-form-item label="销售">
        <el-select v-model="query.salesUserId" placeholder="全部销售" clearable filterable>
          <el-option v-for="item in salesUsers" :key="item.id" :label="formatSalesLabel(item)" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="联系人">
        <el-input v-model="query.contactName" placeholder="请输入联系人" clearable />
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="query.contactPhone" placeholder="请输入手机号" clearable />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" placeholder="全部状态" clearable>
          <el-option v-for="item in appointmentStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
    </SearchBar>

    <section class="table-card">
      <TableToolbar
        title="预约管理"
        description="查看购房预约、分配销售并推进看房状态"
        :show-add="false"
        @refresh="fetchList"
      />
      <el-table v-loading="loading" :data="records" row-key="id" stripe>
        <el-table-column prop="contactName" label="联系人" min-width="120" />
        <el-table-column prop="contactPhone" label="联系电话" min-width="140" />
        <el-table-column prop="buildingName" label="楼盘" min-width="150" show-overflow-tooltip />
        <el-table-column prop="unitName" label="楼栋" min-width="110" show-overflow-tooltip />
        <el-table-column prop="roomNo" label="意向房号" width="120" />
        <el-table-column label="预约时间" width="190">
          <template #default="{ row }">{{ formatDateTimeZh(row.appointmentTime) }}</template>
        </el-table-column>
        <el-table-column label="销售" min-width="130" show-overflow-tooltip>
          <template #default="{ row }">{{ row.salesName || '' }}</template>
        </el-table-column>
        <el-table-column label="状态" width="110">
          <template #default="{ row }">
            <el-tag :type="getOptionType(appointmentStatusOptions, row.status)">
              {{ getOptionLabel(appointmentStatusOptions, row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetail(row)">查看</el-button>
            <el-button link type="primary" @click="openStatus(row)">修改状态</el-button>
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

    <el-dialog v-model="statusVisible" title="修改预约状态" width="520px">
      <el-alert
        class="flow-tip"
        type="info"
        :closable="false"
        show-icon
        title="状态流转：待处理 -> 已分配 -> 已完成 / 已取消"
      />
      <el-form :model="statusForm" label-width="96px">
        <el-form-item label="预约状态">
          <el-select v-model="statusForm.status" placeholder="请选择状态">
            <el-option
              v-for="item in statusUpdateOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
              :disabled="item.disabled"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分配销售">
          <el-select v-model="statusForm.salesUserId" placeholder="请选择销售" clearable filterable>
            <el-option v-for="item in salesUsers" :key="item.id" :label="formatSalesLabel(item)" :value="item.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="statusVisible = false">取消</el-button>
          <el-button type="primary" @click="handleStatusSubmit">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="预约详情" size="620px">
      <div v-if="detail" class="detail-content">
        <el-descriptions title="客户信息" :column="1" border>
          <el-descriptions-item label="客户">{{ detail.customerName || detail.userId || '' }}</el-descriptions-item>
          <el-descriptions-item label="联系人">{{ detail.contactName || '' }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ detail.contactPhone || '' }}</el-descriptions-item>
        </el-descriptions>

        <el-descriptions title="房源意向" :column="1" border>
          <el-descriptions-item label="楼盘">{{ detail.buildingName || '' }}</el-descriptions-item>
          <el-descriptions-item label="楼栋">{{ detail.unitName || '' }}</el-descriptions-item>
          <el-descriptions-item label="房源">{{ detail.roomNo || '' }}</el-descriptions-item>
          <el-descriptions-item label="预约时间">{{ formatDateTimeZh(detail.appointmentTime) }}</el-descriptions-item>
        </el-descriptions>

        <el-descriptions title="跟进状态" :column="1" border>
          <el-descriptions-item label="销售">{{ detail.salesName || '' }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getOptionType(appointmentStatusOptions, detail.status)">
              {{ getOptionLabel(appointmentStatusOptions, detail.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="备注">{{ detail.remark || '' }}</el-descriptions-item>
          <el-descriptions-item label="取消原因">{{ detail.cancelReason || '' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import SearchBar from '@/components/SearchBar.vue';
import TableToolbar from '@/components/TableToolbar.vue';
import Pagination from '@/components/Pagination.vue';
import Empty from '@/components/Empty.vue';
import { getAdminAppointmentPageApi, getSalesUserListApi, updateAppointmentStatusApi } from '@/api/appointment';
import { useTable } from '@/hooks/useTable';
import { formatDateTimeZh } from '@/utils/date';
import { appointmentStatusOptions, getOptionLabel, getOptionType } from '@/utils/options';
import type { AppointmentPageQuery, AppointmentStatusUpdate, AppointmentVO, SalesUserVO } from '@/types/appointment';

type StatusOption = {
  label: string;
  value: number;
  disabled?: boolean;
};

const { loading, records, total, query, fetchList, search, reset, handlePageChange, handleSizeChange } = useTable<
  AppointmentVO,
  Omit<AppointmentPageQuery, 'pageNo' | 'pageSize'>
>(getAdminAppointmentPageApi, {
  buildingId: '',
  salesUserId: '',
  contactName: '',
  contactPhone: '',
  status: ''
});

const statusVisible = ref(false);
const detailVisible = ref(false);
const detail = ref<AppointmentVO | null>(null);
const currentAppointment = ref<AppointmentVO | null>(null);
const salesUsers = ref<SalesUserVO[]>([]);
const statusForm = reactive<AppointmentStatusUpdate>({
  id: 0,
  status: 1,
  salesUserId: undefined
});

const statusUpdateOptions = computed<StatusOption[]>(() => {
  const currentStatus = currentAppointment.value?.status;
  const currentLabel = getOptionLabel(appointmentStatusOptions, currentStatus);
  if (currentStatus === 1) {
    return [
      { label: `${currentLabel}（当前）`, value: 1, disabled: true },
      { label: '已分配', value: 2 },
      { label: '已取消', value: 4 }
    ];
  }
  if (currentStatus === 2) {
    return [
      { label: `${currentLabel}（当前）`, value: 2, disabled: true },
      { label: '已完成', value: 3 },
      { label: '已取消', value: 4 }
    ];
  }
  return [{ label: `${currentLabel}（当前）`, value: Number(currentStatus), disabled: true }];
});

const loadSalesUsers = async () => {
  salesUsers.value = await getSalesUserListApi();
};

const formatSalesLabel = (item: SalesUserVO) => {
  const name = item.realName || item.username;
  return item.phone ? `${name}（${item.phone}）` : name;
};

const openDetail = (row: AppointmentVO) => {
  detail.value = row;
  detailVisible.value = true;
};

const openStatus = async (row: AppointmentVO) => {
  if (!salesUsers.value.length) {
    await loadSalesUsers();
  }
  currentAppointment.value = row;
  statusForm.id = row.id;
  statusForm.status = row.status;
  statusForm.salesUserId = row.salesUserId;
  statusVisible.value = true;
};

const handleStatusSubmit = async () => {
  if ((statusForm.status === 2 || statusForm.status === 3) && !statusForm.salesUserId) {
    ElMessage.warning('分配或完成预约前必须选择销售');
    return;
  }
  await updateAppointmentStatusApi(statusForm);
  ElMessage.success('预约状态已更新');
  statusVisible.value = false;
  fetchList();
};

onMounted(loadSalesUsers);
</script>

<style scoped lang="scss">
.flow-tip {
  margin-bottom: 16px;
}

.detail-content {
  display: grid;
  gap: 18px;
}
</style>
