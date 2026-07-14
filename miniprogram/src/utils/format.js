export const ROOM_STATUS_OPTIONS = [
  { label: '\u5168\u90e8', value: '' },
  { label: '\u5f85\u552e', value: 0 },
  { label: '\u5df2\u552e', value: 1 },
  { label: '\u9501\u5b9a', value: 2 }
];

export const SALE_CONTROL_QUICK_STATUS_OPTIONS = [
  { label: '\u5168\u90e8', value: '' },
  { label: '\u5f85\u552e', value: 0 }
];

export const ROOM_STATUS_MAP = {
  0: { label: '\u5f85\u552e', color: '#52C41A', className: 'status-available' },
  1: { label: '\u5df2\u552e', color: '#F5222D', className: 'status-sold' },
  2: { label: '\u9501\u5b9a', color: '#D9D9D9', className: 'status-locked' }
};

export const APPOINTMENT_STATUS_OPTIONS = [
  { label: '\u5168\u90e8', value: '' },
  { label: '\u5df2\u9884\u7ea6', value: 1 },
  { label: '\u5df2\u53d6\u6d88', value: 2 },
  { label: '\u5df2\u8fc7\u671f', value: 3 }
];

export function formatDateTime(value) {
  if (!value) {
    return '-';
  }
  return String(value).replace('T', ' ').slice(0, 16);
}

export function formatPrice(value) {
  if (value === null || value === undefined || value === '') {
    return '\u4ef7\u683c\u5f85\u5b9a';
  }
  const numeric = Number(value);
  if (Number.isNaN(numeric)) {
    return `${value} \u5143`;
  }
  return `${numeric.toLocaleString()} \u5143`;
}

export function formatArea(value) {
  if (value === null || value === undefined || value === '') {
    return '-';
  }
  const numeric = Number(value);
  if (Number.isNaN(numeric)) {
    return `${value}\u33a1`;
  }
  return `${numeric.toFixed(2).replace(/\.00$/, '')}\u33a1`;
}

export function formatUnitPrice(price, area) {
  const priceNumber = Number(price);
  const areaNumber = Number(area);
  if (!priceNumber || !areaNumber) {
    return '-';
  }
  return `${Math.round(priceNumber / areaNumber).toLocaleString()} \u5143/\u33a1`;
}

export function getRoomStatus(status) {
  return ROOM_STATUS_MAP[status]?.label || '\u672a\u77e5';
}

export function getRoomStatusClass(status) {
  return ROOM_STATUS_MAP[status]?.className || 'status-unknown';
}

export function getRoomStatusColor(status) {
  return ROOM_STATUS_MAP[status]?.color || '#8c8c8c';
}

export function getAppointmentStatus(status) {
  const item = APPOINTMENT_STATUS_OPTIONS.find((option) => option.value === status);
  return item ? item.label : '\u672a\u77e5';
}

export function getAppointmentStatusClass(status) {
  const map = {
    1: 'appointment-booked',
    2: 'appointment-canceled',
    3: 'appointment-expired'
  };
  return map[status] || 'appointment-unknown';
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
