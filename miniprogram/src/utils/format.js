export const ROOM_STATUS_OPTIONS = [
  { label: '全部', value: '' },
  { label: '待售', value: 0 },
  { label: '已预订', value: 1 },
  { label: '已售', value: 2 },
  { label: '不可售', value: 3 }
];

export function formatDateTime(value) {
  if (!value) {
    return '-';
  }
  return String(value).replace('T', ' ').slice(0, 16);
}

export function formatPrice(value) {
  if (value === null || value === undefined || value === '') {
    return '价格待定';
  }
  const numeric = Number(value);
  if (Number.isNaN(numeric)) {
    return `${value} 元`;
  }
  return `${numeric.toLocaleString()} 元`;
}

export function getRoomStatus(status) {
  const item = ROOM_STATUS_OPTIONS.find((option) => option.value === status);
  return item ? item.label : '未知';
}

export function getRoomStatusClass(status) {
  const map = {
    0: 'status-available',
    1: 'status-reserved',
    2: 'status-sold',
    3: 'status-unavailable'
  };
  return map[status] || 'status-unknown';
}

export function parseImageList(value) {
  if (!value) {
    return [];
  }
  if (Array.isArray(value)) {
    return value.filter(Boolean);
  }
  if (typeof value !== 'string') {
    return [];
  }
  try {
    const parsed = JSON.parse(value);
    return Array.isArray(parsed) ? parsed.filter(Boolean) : [];
  } catch {
    return [];
  }
}
