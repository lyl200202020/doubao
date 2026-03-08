<template>
  <svg class="svg-icon" :style="{ width: iconSize, height: iconSize }" aria-hidden="true">
    <use :xlink:href="iconName" />
  </svg>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  icon: string
  size?: number | string
}

const props = withDefaults(defineProps<Props>(), {
  size: 16
})

const iconName = computed(() => `#icon-${props.icon}`)
const iconSize = computed(() => {
  if (typeof props.size === 'number') {
    return `${props.size}px`
  }
  return props.size
})
</script>

<style scoped lang="scss">
.svg-icon {
  vertical-align: middle;
  fill: currentColor;
}
</style>
