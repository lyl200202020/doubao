<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="welcome-card">
          <h2>欢迎回来，{{ userInfo?.userName || userInfo?.userCode }}</h2>
          <p>这是客户运营管理后台系统，您可以在这里进行客户管理、授权管理等工作。</p>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="stat-row">
      <el-col :xs="24" :sm="12" :md="6" v-for="item in statCards" :key="item.title">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: item.color }">
              <el-icon :size="24">
                <component :is="item.icon" />
              </el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-label">{{ item.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :xs="24" :md="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>快捷操作</span>
            </div>
          </template>
          <div class="quick-actions">
            <el-button v-for="action in quickActions" :key="action.label" type="primary" text>
              <el-icon><component :is="action.icon" /></el-icon>
              {{ action.label }}
            </el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>系统公告</span>
            </div>
          </template>
          <div class="notice-list">
            <div v-for="notice in notices" :key="notice.id" class="notice-item">
              <span class="notice-title">{{ notice.title }}</span>
              <span class="notice-time">{{ notice.time }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { getUserInfo } from '@/utils/auth'

const userInfo = computed(() => getUserInfo())

const statCards = [
  { title: '待办任务', value: 5, icon: 'List', color: '#409eff' },
  { title: '客户总数', value: 1280, icon: 'User', color: '#67c23a' },
  { title: '本月新增', value: 36, icon: 'Plus', color: '#e6a23c' },
  { title: '授权金额', value: '50万', icon: 'Money', color: '#f56c6c' }
]

const quickActions = [
  { label: '新增客户', icon: 'Plus' },
  { label: '授权审批', icon: 'Check' },
  { label: '导出数据', icon: 'Download' },
  { label: '系统设置', icon: 'Setting' }
]

const notices = [
  { id: 1, title: '系统将于今晚22:00进行维护', time: '2026-03-05' },
  { id: 2, title: '客户数据已更新至最新', time: '2026-03-04' },
  { id: 3, title: '新增客户批量导入功能', time: '2026-03-03' }
]
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 0;
}

.welcome-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 30px;
  border-radius: 8px;
  margin-bottom: 20px;

  h2 {
    margin: 0 0 10px;
    font-size: 24px;
  }

  p {
    margin: 0;
    opacity: 0.9;
    font-size: 14px;
  }
}

.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  margin-bottom: 20px;

  .stat-content {
    display: flex;
    align-items: center;
    gap: 16px;
  }

  .stat-icon {
    width: 56px;
    height: 56px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
  }

  .stat-info {
    flex: 1;

    .stat-value {
      font-size: 28px;
      font-weight: 600;
      color: #303133;
      line-height: 1.2;
    }

    .stat-label {
      font-size: 14px;
      color: #909399;
      margin-top: 4px;
    }
  }
}

.chart-card {
  margin-bottom: 20px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    span {
      font-weight: 500;
    }
  }
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;

  .el-button {
    display: flex;
    align-items: center;
    gap: 4px;
  }
}

.notice-list {
  .notice-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .notice-title {
      color: #303133;
      font-size: 14px;
    }

    .notice-time {
      color: #909399;
      font-size: 12px;
    }
  }
}
</style>
