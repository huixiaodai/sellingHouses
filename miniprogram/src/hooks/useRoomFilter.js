import { computed, reactive, ref } from 'vue';

export function useRoomFilter() {
  const quickStatus = ref('');
  const filters = reactive({
    minPrice: '',
    maxPrice: '',
    minArea: '',
    maxArea: '',
    layout: '',
    orientation: '',
    decoration: '',
    status: ''
  });

  const query = computed(() => {
    const data = {};
    Object.keys(filters).forEach((key) => {
      if (filters[key] !== '' && filters[key] !== null && filters[key] !== undefined) {
        data[key] = filters[key];
      }
    });
    if (quickStatus.value !== '') {
      data.status = quickStatus.value;
    }
    return data;
  });

  function setQuickStatus(status) {
    quickStatus.value = status;
  }

  function resetFilters() {
    Object.keys(filters).forEach((key) => {
      filters[key] = '';
    });
  }

  return {
    quickStatus,
    filters,
    query,
    setQuickStatus,
    resetFilters
  };
}
