<template>
  <div class="data-table">
    <el-table
      ref="tableRef"
      v-loading="loading"
      :data="data"
      :stripe="stripe"
      :border="border"
      :size="size"
      :height="height"
      :max-height="maxHeight"
      @selection-change="handleSelectionChange"
      @sort-change="handleSortChange"
    >
      <el-table-column v-if="showSelection" type="selection" width="50" align="center" />
      <el-table-column v-if="showIndex" type="index" label="序号" width="60" align="center" />
      <el-table-column
        v-for="column in columns"
        :key="column.prop"
        :prop="column.prop"
        :label="column.label"
        :width="column.width"
        :min-width="column.minWidth"
        :align="column.align || 'left'"
        :fixed="column.fixed"
        :sortable="column.sortable"
        :formatter="column.formatter"
      >
        <template v-if="column.slot" #default="scope">
          <slot :name="column.slot" :row="scope.row" :$index="scope.$index" />
        </template>
      </el-table-column>
      <el-table-column v-if="showAction" label="操作" :width="actionWidth" fixed="right" align="center">
        <template #default="scope">
          <slot name="action" :row="scope.row" :$index="scope.$index" />
        </template>
      </el-table-column>
    </el-table>

    <div v-if="showPagination" class="pagination-container">
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
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import type { ElTable, TableColumnCtx } from 'element-plus'
import { PAGE_SIZE_OPTIONS } from '@/utils/constants'

interface Column {
  prop: string
  label: string
  width?: string | number
  minWidth?: string | number
  align?: 'left' | 'center' | 'right'
  fixed?: boolean | 'left' | 'right'
  sortable?: boolean | 'custom'
  formatter?: (row: any, column: TableColumnCtx<any>, cellValue: any, index: number) => string
  slot?: string
}

interface Props {
  data: any[]
  columns: Column[]
  loading?: boolean
  stripe?: boolean
  border?: boolean
  size?: 'large' | 'default' | 'small'
  height?: string | number
  maxHeight?: string | number
  showSelection?: boolean
  showIndex?: boolean
  showAction?: boolean
  actionWidth?: string | number
  showPagination?: boolean
  total?: number
  page?: number
  limit?: number
  pageSizes?: number[]
  layout?: string
  background?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  stripe: true,
  border: true,
  size: 'default',
  showSelection: false,
  showIndex: true,
  showAction: true,
  actionWidth: 200,
  showPagination: true,
  total: 0,
  page: 1,
  limit: 10,
  pageSizes: () => PAGE_SIZE_OPTIONS,
  layout: 'total, sizes, prev, pager, next, jumper',
  background: true
})

const emit = defineEmits<{
  (e: 'selection-change', selection: any[]): void
  (e: 'sort-change', sort: { prop: string; order: string }): void
  (e: 'update:page', page: number): void
  (e: 'update:limit', limit: number): void
  (e: 'pagination', payload: { page: number; limit: number }): void
}>()

const tableRef = ref<InstanceType<typeof ElTable>>()

const currentPage = computed({
  get: () => props.page,
  set: (val) => emit('update:page', val)
})

const pageSize = computed({
  get: () => props.limit,
  set: (val) => emit('update:limit', val)
})

const handleSelectionChange = (selection: any[]) => {
  emit('selection-change', selection)
}

const handleSortChange = ({ prop, order }: any) => {
  emit('sort-change', { prop, order })
}

const handleSizeChange = (val: number) => {
  emit('update:limit', val)
  emit('pagination', { page: currentPage.value, limit: val })
}

const handleCurrentChange = (val: number) => {
  emit('update:page', val)
  emit('pagination', { page: val, limit: pageSize.value })
}

const toggleRowSelection = (row: any, selected?: boolean) => {
  tableRef.value?.toggleRowSelection(row, selected)
}

const clearSelection = () => {
  tableRef.value?.clearSelection()
}

const toggleAllSelection = () => {
  tableRef.value?.toggleAllSelection()
}

defineExpose({
  tableRef,
  toggleRowSelection,
  clearSelection,
  toggleAllSelection
})
</script>

<style scoped lang="scss">
.data-table {
  .pagination-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 16px;
  }
}
</style>
