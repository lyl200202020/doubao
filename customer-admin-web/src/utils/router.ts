import type { RouteRecordRaw } from 'vue-router'
import type { Menu } from '@/types/menu'

const modules = import.meta.glob('@/views/**/*.vue')

function generateRoutes(menus: Menu[], isRoot: boolean = true): RouteRecordRaw[] {
  const routes: RouteRecordRaw[] = []

  menus.forEach(menu => {
    if (menu.menuType === 1 || menu.menuType === 2) {
      const route: RouteRecordRaw = {
        path: isRoot ? (menu.menuUrl || '/') : (menu.menuUrl?.replace(/^\//, '') || ''),
        name: menu.menuId,
        meta: {
          title: menu.menuName,
          icon: menu.menuIcon,
          perms: menu.perms
        }
      }

      if (menu.menuType === 1) {
        route.component = () => import('@/components/Layout/AppLayout.vue')
        route.redirect = 'noRedirect'
      } else if (menu.menuType === 2 && menu.menuUrl) {
        const componentPath = `@/views${menu.menuUrl}/index.vue`
        route.component = modules[componentPath] || (() => import('@/views/error/404.vue'))
      }

      if (menu.children && menu.children.length > 0) {
        const childRoutes = generateRoutes(menu.children, false)
        if (childRoutes.length > 0) {
          route.children = childRoutes
        }
      }

      routes.push(route)
    }
  })

  return routes
}

export { generateRoutes }

export function getSidebarMenus(menus: Menu[]): Menu[] {
  return menus.filter(menu => menu.menuType === 1 || menu.menuType === 2)
}

export function getButtonPerms(menus: Menu[]): string[] {
  const perms: string[] = []

  menus.forEach(menu => {
    if (menu.menuType === 3 && menu.perms) {
      perms.push(menu.perms)
    }
    if (menu.children && menu.children.length > 0) {
      perms.push(...getButtonPerms(menu.children))
    }
  })

  return perms
}
