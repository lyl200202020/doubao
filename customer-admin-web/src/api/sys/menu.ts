import request from '@/utils/request'
import type { Result } from '@/utils/request'
import type { Menu, MenuDTO } from '@/types/menu'

/**
 * 获取菜单树
 */
export function getMenus() {
  return request<any, Result<Menu[]>>({
    url: '/sys/menu/tree',
    method: 'get'
  })
}

/**
 * 获取菜单详情
 */
export function getMenuDetail(id: string) {
  return request<any, Result<Menu>>({
    url: `/sys/menu/${id}`,
    method: 'get'
  })
}

/**
 * 新增菜单
 */
export function addMenu(data: MenuDTO) {
  return request<any, Result<void>>({
    url: '/sys/menu',
    method: 'post',
    data
  })
}

/**
 * 修改菜单
 */
export function updateMenu(id: string, data: MenuDTO) {
  return request<any, Result<void>>({
    url: `/sys/menu/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除菜单
 */
export function deleteMenu(id: string) {
  return request<any, Result<void>>({
    url: `/sys/menu/${id}`,
    method: 'delete'
  })
}

/**
 * 获取角色可选菜单
 */
export function getMenuOptions() {
  return request<any, Result<Menu[]>>({
    url: '/sys/menu/options',
    method: 'get'
  })
}
