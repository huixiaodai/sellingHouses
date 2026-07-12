import { ElMessageBox } from 'element-plus';

export async function confirmDelete(message = '确认删除这条数据吗？') {
  await ElMessageBox.confirm(message, '删除确认', {
    type: 'warning',
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    confirmButtonClass: 'el-button--danger'
  });
}
