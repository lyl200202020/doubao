<template>
  <el-form ref="formRef" :model="modelValue" :rules="rules" :label-width="labelWidth" v-bind="$attrs">
    <el-row :gutter="20">
      <el-col v-for="item in searchFields" :key="item.prop" :span="item.span || 6">
        <el-form-item :label="item.label" :prop="item.prop">
          <el-input
            v-if="item.type === 'input'"
            v-model="modelValue[item.prop]"
            :placeholder="item.placeholder || `请输入${item.label}`"
            v-bind="item.props"
            @change="handleChange(item, 'change')"
            @input="handleChange(item, 'input')"
          />
          <el-select
            v-else-if="item.type === 'select'"
            v-model="modelValue[item.prop]"
            :placeholder="item.placeholder || `请选择${item.label}`"
            v-bind="item.props"
            @change="handleChange(item, 'change')"
          >
            <el-option
              v-for="option in item.options"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
          <el-date-picker
            v-else-if="item.type === 'date'"
            v-model="modelValue[item.prop]"
            type="date"
            :placeholder="item.placeholder || `请选择${item.label}`"
            v-bind="item.props"
            @change="handleChange(item, 'change')"
          />
          <el-date-picker
            v-else-if="item.type === 'daterange'"
            v-model="modelValue[item.prop]"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            v-bind="item.props"
            @change="handleChange(item, 'change')"
          />
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button v-if="showExpand" type="text" @click="isExpanded = !isExpanded">
            {{ isExpanded ? '收起' : '展开' }}
            <el-icon><ArrowUp v-if="isExpanded" /><ArrowDown v-else /></el-icon>
          </el-button>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'

interface SearchField {
  prop: string
  label: string
  type: 'input' | 'select' | 'date' | 'daterange'
  placeholder?: string
  span?: number
  options?: Array<{ label: string; value: any }>
  props?: Record<string, any>
}

interface Props {
  modelValue: Record<string, any>
  fields: SearchField[]
  labelWidth?: string | number
  rules?: FormRules
  showExpand?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  labelWidth: '100px',
  showExpand: false
})

const emit = defineEmits<{
  (e: 'update:modelValue', value: Record<string, any>): void
  (e: 'search', value: Record<string, any>): void
  (e: 'reset'): void
}>()

const formRef = ref<FormInstance>()
const isExpanded = ref(false)

const searchFields = computed(() => {
  if (props.showExpand && !isExpanded.value) {
    return props.fields.slice(0, 4)
  }
  return props.fields
})

const handleSearch = () => {
  emit('search', props.modelValue)
}

const handleReset = () => {
  formRef.value?.resetFields()
  emit('reset')
}

const handleChange = (item: SearchField, type: string) => {
  if (item.props?.immediate !== false && type === 'change') {
    handleSearch()
  }
}

defineExpose({
  formRef,
  isExpanded
})
</script>
