import { RouteRecordRaw } from 'vue-router'

export const constantRoutes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', hidden: true }
  },
  {
    path: '/redirect',
    component: () => import('@/views/redirect/index.vue'),
    meta: { hidden: true }
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue'),
    meta: { hidden: true }
  },
  {
    path: '/401',
    name: 'Unauthorized',
    component: () => import('@/views/error/401.vue'),
    meta: { hidden: true }
  }
]
