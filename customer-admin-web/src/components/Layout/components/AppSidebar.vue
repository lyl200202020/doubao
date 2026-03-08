<template>
  <div class="sidebar-container">
    <div class="logo-container">
      <h1 class="logo-title">客户运营</h1>
    </div>
    <el-menu
      :default-active="activeMenu"
      :collapse="false"
      :background-color="variables.menuBg"
      :text-color="variables.menuText"
      :active-text-color="variables.menuActiveText"
      router
      class="sidebar-menu"
    >
      <sidebar-item
        v-for="menu in sidebarMenus"
        :key="menu.menuId"
        :menu="menu"
      />
    </el-menu>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import SidebarItem from './SidebarItem.vue'
import variables from '@/assets/styles/variables.module.scss'
import { usePermissionStore } from '@/store/modules/permission'

const route = useRoute()
const permissionStore = usePermissionStore()

const sidebarMenus = computed(() => permissionStore.getSidebarMenus())

const activeMenu = computed(() => {
  const { path } = route
  return path
})
</script>

<style scoped lang="scss">
.sidebar-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: #304156;
  overflow-x: hidden;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #2b3a4a;

  .logo-title {
    font-size: 18px;
    font-weight: 600;
    color: #fff;
    margin: 0;
    white-space: nowrap;
  }
}

.sidebar-menu {
  border: none;
  height: calc(100% - 60px);
  overflow-y: auto;
}
</style>
