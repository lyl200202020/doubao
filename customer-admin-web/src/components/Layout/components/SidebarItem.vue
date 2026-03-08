<template>
  <div v-if="!menu.hidden">
    <template v-if="hasOneShowingChild(menu.children || [], menu) && (!onlyOneChild.children || onlyOneChild.noShowingChildren)">
      <el-menu-item v-if="onlyOneChild.menuName" :index="resolvePath(onlyOneChild.menuUrl || '')" :route="resolvePath(onlyOneChild.menuUrl || '')">
        <el-icon v-if="onlyOneChild.menuIcon">
          <component :is="onlyOneChild.menuIcon" />
        </el-icon>
        <template #title>
          {{ onlyOneChild.menuName }}
        </template>
      </el-menu-item>
    </template>

    <el-sub-menu v-else :index="resolvePath(menu.menuUrl || '')" teleported>
      <template #title>
        <el-icon v-if="menu.menuIcon">
          <component :is="menu.menuIcon" />
        </el-icon>
        <span>{{ menu.menuName }}</span>
      </template>
      <sidebar-item
        v-for="child in (menu.children || [])"
        :key="child.menuId"
        :menu="child"
      />
    </el-sub-menu>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { Menu } from '@/types/menu'
import { isExternal } from '@/utils/common'

interface Props {
  menu: Menu
}

const props = defineProps<Props>()

const onlyOneChild = ref<Menu>()

const hasOneShowingChild = (children: Menu[] = [], parent: Menu) => {
  const showingChildren = children.filter((item) => {
    if (item.hidden || item.menuType === 3) {
      return false
    }
    onlyOneChild.value = item
    return true
  })

  if (showingChildren.length === 1) {
    return true
  }

  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, noShowingChildren: true }
    return true
  }

  return false
}

const resolvePath = (routePath: string) => {
  if (isExternal(routePath)) {
    return routePath
  }
  return routePath
}
</script>
