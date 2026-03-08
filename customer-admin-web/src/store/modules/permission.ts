import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Menu } from '@/types/menu'

export const usePermissionStore = defineStore('permission', () => {
  const menus = ref<Menu[]>([])
  const permissions = ref<string[]>([])

  function setMenus(newMenus: Menu[]) {
    menus.value = newMenus
    permissions.value = extractPermissions(newMenus)
  }

  function setPermissions(newPermissions: string[]) {
    permissions.value = newPermissions
  }

  function extractPermissions(menus: Menu[]): string[] {
    const perms: string[] = []
    menus.forEach(menu => {
      if (menu.perms) {
        perms.push(menu.perms)
      }
      if (menu.children && menu.children.length > 0) {
        perms.push(...extractPermissions(menu.children))
      }
    })
    return perms
  }

  function hasPermission(permission: string): boolean {
    return permissions.value.includes(permission)
  }

  function getSidebarMenus(): Menu[] {
    return filterButtonMenus(menus.value)
  }

  function filterButtonMenus(menuList: Menu[]): Menu[] {
    return menuList
      .filter(menu => menu.menuType === 1 || menu.menuType === 2)
      .map(menu => ({
        ...menu,
        children: menu.children ? filterButtonMenus(menu.children) : undefined
      }))
  }

  function clearMenus() {
    menus.value = []
    permissions.value = []
  }

  return {
    menus,
    permissions,
    setMenus,
    setPermissions,
    hasPermission,
    getSidebarMenus,
    clearMenus
  }
})
