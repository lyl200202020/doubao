import { useUserStore } from '@/store/modules/user'
import { usePermissionStore } from '@/store/modules/permission'
import { getToken } from '@/utils/auth'
import { generateRoutes } from '@/utils/router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import type { Router, RouteRecordRaw } from 'vue-router'
import { ElMessage } from 'element-plus'

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/404', '/401']

export function setupRouterGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    NProgress.start()
    const userStore = useUserStore()
    const permissionStore = usePermissionStore()
    const hasToken = getToken()

    if (hasToken) {
      if (to.path === '/login') {
        next({ path: '/' })
        NProgress.done()
      } else {
        const hasRoles = userStore.roles && userStore.roles.length > 0

        if (hasRoles) {
          next()
        } else {
          try {
            // 获取用户信息和菜单
            const res = await userStore.getInfo()
            const { menus, permissions } = res.data

            // 生成动态路由
            const accessRoutes = generateRoutes(menus)

            // 添加路由
            accessRoutes.forEach((route: RouteRecordRaw) => {
              router.addRoute(route)
            })

            // 保存菜单到 Store
            permissionStore.setMenus(menus)
            permissionStore.setPermissions(permissions)

            next({ ...to, replace: true })
          } catch (error) {
            // 获取失败，清空 token 并跳转登录页
            await userStore.resetToken()
            ElMessage.error('获取用户信息失败，请重新登录')
            next(`/login?redirect=${to.path}`)
            NProgress.done()
          }
        }
      }
    } else {
      // 没有 token
      if (whiteList.includes(to.path)) {
        next()
      } else {
        next(`/login?redirect=${to.path}`)
        NProgress.done()
      }
    }
  })

  router.afterEach(() => {
    NProgress.done()
  })
}
