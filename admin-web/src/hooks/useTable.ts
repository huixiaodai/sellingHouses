import { onMounted, reactive, ref } from 'vue';
import type { PageResult } from '@/types/common';

type Fetcher<T, Q extends object> = (query: Q & { pageNo: number; pageSize: number }) => Promise<PageResult<T>>;

export function useTable<T, Q extends object>(fetcher: Fetcher<T, Q>, defaultQuery: Q) {
  const loading = ref(false);
  const records = ref<T[]>([]);
  const total = ref(0);
  const query = reactive({
    ...defaultQuery,
    pageNo: 1,
    pageSize: 10
  }) as Q & { pageNo: number; pageSize: number };

  const fetchList = async () => {
    loading.value = true;
    try {
      const result = await fetcher({ ...query });
      records.value = result.records || [];
      total.value = result.total || 0;
      query.pageNo = result.pageNo || query.pageNo;
      query.pageSize = result.pageSize || query.pageSize;
    } finally {
      loading.value = false;
    }
  };

  const search = () => {
    query.pageNo = 1;
    return fetchList();
  };

  const reset = () => {
    Object.assign(query, defaultQuery, { pageNo: 1, pageSize: 10 });
    return fetchList();
  };

  const handlePageChange = (pageNo: number) => {
    query.pageNo = pageNo;
    return fetchList();
  };

  const handleSizeChange = (pageSize: number) => {
    query.pageSize = pageSize;
    query.pageNo = 1;
    return fetchList();
  };

  onMounted(fetchList);

  return {
    loading,
    records,
    total,
    query,
    fetchList,
    search,
    reset,
    handlePageChange,
    handleSizeChange
  };
}
