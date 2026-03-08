import { ref, reactive, computed, onMounted } from 'vue'

interface Pagination {
  page: number
  limit: number
  total: number
}

export function useTable<T = any>(
  fetchApi: (params: any) => Promise<any>,
  defaultParams?: Record<string, any>
) {
  const loading = ref(false)
  const dataList = ref<T[]>([])
  const pagination = reactive<Pagination>({
    page: 1,
    limit: 10,
    total: 0
  })

  const queryParams = reactive<Record<string, any>>(defaultParams || {})

  const loadData = async () => {
    loading.value = true
    try {
      const params = {
        ...queryParams,
        page: pagination.page,
        limit: pagination.limit
      }
      const res = await fetchApi(params)
      if (res.code === 200) {
        dataList.value = res.data?.list || res.data || []
        pagination.total = res.data?.total || 0
      }
    } catch (error) {
      console.error('Load data error:', error)
    } finally {
      loading.value = false
    }
  }

  const search = (params?: Record<string, any>) => {
    if (params) {
      Object.assign(queryParams, params)
    }
    pagination.page = 1
    loadData()
  }

  const reset = () => {
    pagination.page = 1
    Object.keys(queryParams).forEach((key) => {
      delete queryParams[key]
    })
    loadData()
  }

  const handlePageChange = (page: number) => {
    pagination.page = page
    loadData()
  }

  const handleSizeChange = (size: number) => {
    pagination.limit = size
    pagination.page = 1
    loadData()
  }

  const reload = () => {
    loadData()
  }

  onMounted(() => {
    loadData()
  })

  return {
    loading,
    dataList,
    pagination,
    queryParams,
    loadData,
    search,
    reset,
    handlePageChange,
    handleSizeChange,
    reload
  }
}
