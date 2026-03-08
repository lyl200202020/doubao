<template>
  <el-pagination
    v-model:current-page="currentPage"
    v-model:page-size="pageSize"
    :page-sizes="pageSizes"
    :total="total"
    :layout="layout"
    :background="background"
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
  />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { PAGE_SIZE_OPTIONS } from '@/utils/constants'

interface Props {
  total: number
  page?: number
  limit?: number
  pageSizes?: number[]
  layout?: string
  background?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  page: 1,
  limit: 10,
  pageSizes: () => PAGE_SIZE_OPTIONS,
  layout: 'total, sizes, prev, pager, next, jumper',
  background: true
})

const emit = defineEmits<{
  (e: 'update:page', page: number): void
  (e: 'update:limit', limit: number): void
  (e: 'pagination', payload: { page: number; limit: number }): void
}>()

const currentPage = computed({
  get: () => props.page,
  set: (val) => emit('update:page', val)
})

const pageSize = computed({
  get: () => props.limit,
  set: (val) => emit('update:limit', val)
})

const handleSizeChange = (val: number) => {
  emit('update:limit', val)
  emit('pagination', { page: currentPage.value, limit: val })
}

const handleCurrentChange = (val: number) => {
  emit('update:page', val)
  emit('pagination', { page: val, limit: pageSize.value })
}
</script>

<style scoped lang="scss">
.el-pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
