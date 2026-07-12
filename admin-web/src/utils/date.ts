export function formatDateTimeZh(value?: string | null) {
  if (!value) {
    return '-';
  }

  const normalized = value.replace(/-/g, '/');
  const date = new Date(normalized);
  if (Number.isNaN(date.getTime())) {
    return value;
  }

  const pad = (num: number) => String(num).padStart(2, '0');
  return `${date.getFullYear()}年${pad(date.getMonth() + 1)}月${pad(date.getDate())}日 ${pad(date.getHours())}:${pad(
    date.getMinutes()
  )}:${pad(date.getSeconds())}`;
}
