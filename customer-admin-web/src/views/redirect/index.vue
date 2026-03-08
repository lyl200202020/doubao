<template>
  <div></div>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { onMounted } from 'vue'

const route = useRoute()
const router = useRouter()

onMounted(() => {
  const path = route.query.path as string
  if (path) {
    router.replace(path)
  } else {
    router.replace('/')
  }
})
</script>
