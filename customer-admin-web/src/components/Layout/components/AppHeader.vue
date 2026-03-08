<template>
  <div class="header-container">
    <div class="header-left">
      <breadcrumb class="breadcrumb" />
    </div>
    <div class="header-right">
      <div class="user-info">
        <el-dropdown trigger="click" @command="handleCommand">
          <span class="user-dropdown">
            <el-avatar :size="32" :src="userAvatar" />
            <span class="username">{{ userInfo?.userName || userInfo?.userCode }}</span>
            <el-icon class="arrow-down">
              <ArrowDown />
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon>
                个人中心
              </el-dropdown-item>
              <el-dropdown-item command="settings">
                <el-icon><Setting /></el-icon>
                系统设置
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserInfo } from '@/utils/auth'
import { logout } from '@/api/auth'
import Breadcrumb from './Breadcrumb.vue'

const router = useRouter()

const userInfo = computed(() => getUserInfo())
const userAvatar = computed(() => 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png')

const handleCommand = async (command: string) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      await logout()
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      ElMessage.success('退出成功')
      router.push('/login')
    } catch (error) {
      // 取消操作
    }
  } else if (command === 'profile') {
    ElMessage.info('功能开发中')
  } else if (command === 'settings') {
    ElMessage.info('功能开发中')
  }
}
</script>

<style scoped lang="scss">
.header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
  padding: 0 20px;
  background-color: #fff;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  cursor: pointer;

  .user-dropdown {
    display: flex;
    align-items: center;
    gap: 8px;

    .username {
      font-size: 14px;
      color: #303133;
    }

    .arrow-down {
      color: #909399;
    }
  }
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
