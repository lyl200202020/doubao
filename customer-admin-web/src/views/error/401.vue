<template>
  <div class="error-container">
    <div class="error-content">
      <h1 class="error-code">401</h1>
      <p class="error-title">未授权</p>
      <p class="error-description">抱歉，您没有权限访问该页面</p>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/')
}
</script>

<style scoped lang="scss">
.error-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.error-content {
  text-align: center;

  .error-code {
    font-size: 120px;
    font-weight: bold;
    color: #409eff;
    margin: 0;
    line-height: 1;
  }

  .error-title {
    font-size: 24px;
    color: #303133;
    margin: 20px 0 10px;
  }

  .error-description {
    font-size: 14px;
    color: #909399;
    margin-bottom: 30px;
  }
}
</style>
