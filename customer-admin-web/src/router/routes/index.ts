import { RouteRecordRaw } from 'vue-router'

export const asyncRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('@/components/Layout/AppLayout.vue'),
    redirect: '/dashboard',
    meta: { title: '首页' },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '工作台', icon: 'HomeFilled', affix: true }
      }
    ]
  },
  {
    path: '/sys',
    component: () => import('@/components/Layout/AppLayout.vue'),
    redirect: '/sys/user',
    meta: { title: '系统管理', icon: 'Setting' },
    children: [
      {
        path: 'user',
        name: 'SysUser',
        component: () => import('@/views/sys/user/index.vue'),
        meta: { title: '用户管理', icon: 'User', permission: ['sys:user:list'] }
      },
      {
        path: 'role',
        name: 'SysRole',
        component: () => import('@/views/sys/role/index.vue'),
        meta: { title: '角色管理', icon: 'UserFilled', permission: ['sys:role:view'] }
      },
      {
        path: 'menu',
        name: 'SysMenu',
        component: () => import('@/views/sys/menu/index.vue'),
        meta: { title: '菜单管理', icon: 'Menu', permission: ['sys:menu:list'] }
      },
      {
        path: 'org',
        name: 'SysOrg',
        component: () => import('@/views/sys/org/index.vue'),
        meta: { title: '机构管理', icon: 'OfficeBuilding', permission: ['sys:org:list'] }
      }
    ]
  },
  {
    path: '/auth',
    component: () => import('@/components/Layout/AppLayout.vue'),
    redirect: '/auth/task/todo',
    meta: { title: '授权管理', icon: 'Lock' },
    children: [
      {
        path: 'task/todo',
        name: 'AuthTaskTodo',
        component: () => import('@/views/auth/task/todo.vue'),
        meta: { title: '待办任务', icon: 'List', permission: ['auth:task:todo'] }
      },
      {
        path: 'task/done',
        name: 'AuthTaskDone',
        component: () => import('@/views/auth/task/done.vue'),
        meta: { title: '我的已办', icon: 'Finished', permission: ['auth:task:done'] }
      },
      {
        path: 'history',
        name: 'AuthHistory',
        component: () => import('@/views/auth/history/index.vue'),
        meta: { title: '授权历史', icon: 'Clock', permission: ['auth:history:list'] }
      }
    ]
  },
  {
    path: '/crm',
    component: () => import('@/components/Layout/AppLayout.vue'),
    redirect: '/crm/customer',
    meta: { title: '客户管理', icon: 'UserPlus' },
    children: [
      {
        path: 'customer',
        name: 'CrmCustomer',
        component: () => import('@/views/crm/customer/index.vue'),
        meta: { title: '客户管理', icon: 'User', permission: ['crm:customer:list'] }
      },
      {
        path: 'contract',
        name: 'CrmContract',
        component: () => import('@/views/crm/contract/index.vue'),
        meta: { title: '合同管理', icon: 'Document', permission: ['crm:contract:list'] }
      }
    ]
  }
]
