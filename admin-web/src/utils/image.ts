export function parseImageList(value?: string) {
  if (!value) {
    return [];
  }
  try {
    const parsed = JSON.parse(value) as unknown;
    if (Array.isArray(parsed)) {
      return parsed.filter((item): item is string => typeof item === 'string' && item.length > 0);
    }
  } catch {
    return [value];
  }
  return [];
}

export function stringifyImageList(list: string[]) {
  return JSON.stringify(list.filter(Boolean));
}
