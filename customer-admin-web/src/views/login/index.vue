<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h1 class="title">客户运营管理后台</h1>
      </div>
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        autocomplete="on"
        label-position="left"
      >
        <el-form-item prop="userCode">
          <el-input
            v-model="loginForm.userCode"
            placeholder="请输入用户工号"
            name="userCode"
            type="text"
            tabindex="1"
            autocomplete="on"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            placeholder="请输入密码"
            name="password"
            type="password"
            tabindex="2"
            autocomplete="on"
            prefix-icon="Lock"
            size="large"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-button
          :loading="loading"
          type="primary"
          size="large"
          class="login-button"
          @click="handleLogin"
        >
          {{ loading ? '登录中...' : '登 录' }}
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, FormInstance, FormRules } from 'element-plus'
import { useUserStore } from '@/store/modules/user'
import { usePermissionStore } from '@/store/modules/permission'
import { generateRoutes } from '@/utils/router'
import type { RouteRecordRaw } from 'vue-router'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const permissionStore = usePermissionStore()

const loginFormRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive({
  userCode: 'admin',
  password: '123456'
})

const loginRules: FormRules = {
  userCode: [{ required: true, message: '请输入用户工号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        userStore.resetToken()
        permissionStore.clearMenus()
        
        const res = await userStore.loginAction(loginForm)
        if (res.code === 200) {
          const { menus, permissions } = res.data
          
          const accessRoutes = generateRoutes(menus)
          accessRoutes.forEach((routeItem: RouteRecordRaw) => {
            router.addRoute(routeItem)
          })
          
          permissionStore.setMenus(menus)
          permissionStore.setPermissions(permissions)
          
          ElMessage.success('登录成功')
          const redirect = route.query.redirect as string
          router.push(redirect || '/')
        }
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped lang="scss">
.login-container {
  min-height: 100%;
  width: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}

.login-box {
  width: 450px;
  padding: 50px 40px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;

  .title {
    font-size: 24px;
    font-weight: 600;
    color: #333;
    margin: 0;
  }
}

.login-form {
  .el-form-item {
    margin-bottom: 24px;
  }

  :deep(.el-input__wrapper) {
    padding: 4px 12px;
  }
}

.login-button {
  width: 100%;
  margin-top: 10px;
  font-weight: 500;
  font-size: 16px;
}
</style>
