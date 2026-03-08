<template>
  <el-tag :type="tagType" :effect="effect" :size="size" :hit="hit" :closable="closable" @close="handleClose">
    {{ text }}
  </el-tag>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  type?: 'success' | 'warning' | 'danger' | 'info' | ''
  status?: number
  text?: string
  map?: Record<number, string>
  mapType?: Record<number, string>
  effect?: 'light' | 'dark' | 'plain'
  size?: 'large' | 'default' | 'small'
  hit?: boolean
  closable?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  type: '',
  status: 0,
  text: '',
  effect: 'light',
  size: 'default',
  hit: false,
  closable: false
})

const emit = defineEmits<{
  (e: 'close'): void
}>()

const tagType = computed(() => {
  if (props.type) return props.type
  if (props.mapType) {
    return props.mapType[props.status] || ''
  }
  const typeMap: Record<number, string> = {
    0: 'success',
    1: 'danger',
    2: 'warning',
    3: 'info'
  }
  return typeMap[props.status] || 'info'
})

const text = computed(() => {
  if (props.text) return props.text
  if (props.map) {
    return props.map[props.status] || ''
  }
  return ''
})

const handleClose = () => {
  emit('close')
}
</script>
