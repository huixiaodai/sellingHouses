<template>
  <div>
    <SearchBar :model="query" @search="search" @reset="reset">
      <el-form-item label="楼盘ID">
        <el-input v-model="query.estateId" placeholder="请输入楼盘ID" clearable inputmode="numeric" />
      </el-form-item>
      <el-form-item label="销售ID">
        <el-input v-model="query.salesUserId" placeholder="请输入销售ID" clearable inputmode="numeric" />
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
        description="查看全部购房预约，调整状态并分配销售"
        :show-add="false"
        @refresh="fetchList"
      />
      <el-table v-loading="loading" :data="records" row-key="id" stripe>
        <el-table-column prop="contactName" label="联系人" min-width="120" />
        <el-table-column prop="contactPhone" label="联系电话" min-width="140" />
        <el-table-column prop="buildingName" label="楼盘" min-width="150" show-overflow-tooltip />
        <el-table-column label="意向房号" width="120">
          <template #default="{ row }">{{ row.roomNo || '' }}</template>
        </el-table-column>
        <el-table-column label="预约时间" width="190">
          <template #default="{ row }">{{ formatDateTimeZh(row.appointmentTime) }}</template>
        </el-table-column>
        <el-table-column prop="salesName" label="销售" width="120" />
        <el-table-column label="状态" width="110">
          <template #default="{ row }">
            <el-tag :type="getOptionType(appointmentStatusOptions, row.status)">
              {{ getOptionLabel(appointmentStatusOptions, row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="210" fixed="right">
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

    <el-dialog v-model="statusVisible" title="修改预约状态" width="460px">
      <el-form :model="statusForm" label-width="96px">
        <el-form-item label="预约状态">
          <el-select v-model="statusForm.status">
            <el-option v-for="item in appointmentStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="销售ID">
          <el-input-number v-model="statusForm.salesUserId" :min="1" />
          <div class="form-tip">TODO：后续接入销售列表接口后替换为下拉选择。</div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="statusVisible = false">取消</el-button>
          <el-button type="primary" @click="handleStatusSubmit">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="预约详情" size="540px">
      <el-descriptions v-if="detail" :column="1" border>
        <el-descriptions-item label="联系人">{{ detail.contactName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detail.contactPhone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="楼盘">{{ detail.buildingName || detail.estateId || detail.buildingId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="意向房号">{{ detail.roomNo || detail.roomId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="预约时间">{{ formatDateTimeZh(detail.appointmentTime) }}</el-descriptions-item>
        <el-descriptions-item label="销售">{{ detail.salesName || detail.salesUserId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getOptionType(appointmentStatusOptions, detail.status)">
            {{ getOptionLabel(appointmentStatusOptions, detail.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注">{{ detail.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="取消原因">{{ detail.cancelReason || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import SearchBar from '@/components/SearchBar.vue';
import TableToolbar from '@/components/TableToolbar.vue';
import Pagination from '@/components/Pagination.vue';
import Empty from '@/components/Empty.vue';
import { getAdminAppointmentPageApi, updateAppointmentStatusApi } from '@/api/appointment';
import { useTable } from '@/hooks/useTable';
import { formatDateTimeZh } from '@/utils/date';
import { appointmentStatusOptions, getOptionLabel, getOptionType } from '@/utils/options';
import type { AppointmentPageQuery, AppointmentStatusUpdate, AppointmentVO } from '@/types/appointment';

const { loading, records, total, query, fetchList, search, reset, handlePageChange, handleSizeChange } = useTable<
  AppointmentVO,
  Omit<AppointmentPageQuery, 'pageNo' | 'pageSize'>
>(getAdminAppointmentPageApi, { estateId: '', salesUserId: '', status: '' });

const statusVisible = ref(false);
const detailVisible = ref(false);
const detail = ref<AppointmentVO | null>(null);
const statusForm = reactive<AppointmentStatusUpdate>({
  id: 0,
  status: 1,
  salesUserId: undefined
});

const openDetail = (row: AppointmentVO) => {
  detail.value = row;
  detailVisible.value = true;
};

const openStatus = (row: AppointmentVO) => {
  statusForm.id = row.id;
  statusForm.status = row.status;
  statusForm.salesUserId = row.salesUserId;
  statusVisible.value = true;
};

const handleStatusSubmit = async () => {
  await updateAppointmentStatusApi(statusForm);
  ElMessage.success('预约状态已更新');
  statusVisible.value = false;
  fetchList();
};
</script>

<style scoped lang="scss">
.form-tip {
  width: 100%;
  margin-top: 6px;
  color: var(--admin-muted);
  font-size: 12px;
}
</style>
