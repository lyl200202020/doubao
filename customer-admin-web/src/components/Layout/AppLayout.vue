<template>
  <div class="app-layout">
    <AppSidebar class="sidebar-container" />
    <div class="main-container">
      <AppHeader class="header-container" />
      <div class="app-main">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import AppSidebar from './components/AppSidebar.vue'
import AppHeader from './components/AppHeader.vue'
</script>

<style scoped lang="scss">
.app-layout {
  display: flex;
  width: 100%;
  height: 100%;
}

.sidebar-container {
  width: 220px;
  height: 100vh;
  background-color: #304156;
  overflow-x: hidden;
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.header-container {
  height: 60px;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.app-main {
  flex: 1;
  overflow-y: auto;
  padding: 0;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-20px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(20px);
}
</style>
