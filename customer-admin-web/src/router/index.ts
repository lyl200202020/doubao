import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { constantRoutes } from './routes/basic'
import { asyncRoutes } from './routes/index'

const routes: RouteRecordRaw[] = [...constantRoutes, ...asyncRoutes]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

export { constantRoutes, asyncRoutes }
