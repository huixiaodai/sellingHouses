export const enabledStatusOptions = [
  { label: '启用', value: 1 },
  { label: '禁用', value: 0 }
];

export const roomStatusOptions = [
  { label: '待售', value: 1, type: 'success' },
  { label: '已预订', value: 2, type: 'warning' },
  { label: '已售', value: 3, type: 'danger' },
  { label: '不可售', value: 4, type: 'info' }
] as const;

export const appointmentStatusOptions = [
  { label: '待处理', value: 1, type: 'warning' },
  { label: '已分配', value: 2, type: 'primary' },
  { label: '已完成', value: 3, type: 'success' },
  { label: '已取消', value: 4, type: 'info' }
] as const;

export const noticeTargetOptions = [
  { label: '全部', value: 'ALL' },
  { label: '管理员', value: 'ADMIN' },
  { label: '销售', value: 'SALES' },
  { label: '购房用户', value: 'CUSTOMER' }
];

export function getOptionLabel<T extends { label: string; value: string | number }>(
  options: readonly T[],
  value?: string | number
) {
  return options.find((item) => item.value === value)?.label || '-';
}

export function getOptionType<T extends { type?: string; value: string | number }>(
  options: readonly T[],
  value?: string | number
) {
  return options.find((item) => item.value === value)?.type || 'info';
}
