import request from '@/utils/request'
import type { Result } from '@/utils/request'
import type { Role, RoleDTO } from '@/types/role'

export interface RoleListParams {
  pageNum: number
  pageSize: number
  roleName?: string
  roleStatus?: number
}

export interface RoleListResult {
  list: Role[]
  total: number
  pageNum: number
  pageSize: number
  pages: number

}

/**
 * 获取角色列表
 */
export function getRoleList(params: RoleListParams) {
  return request<any, Result<RoleListResult>>({
    url: '/sys/role/list',
    method: 'get',
    params
  })
}

/**
 * 获取角色详情
 */
export function getRoleDetail(id: string) {
  return request<any, Result<Role>>({
    url: `/sys/role/${id}`,
    method: 'get'
  })
}

/**
 * 获取所有角色（下拉选项）
 */
export function getRoleOptions() {
  return request<any, Result<Role[]>>({
    url: '/sys/role/options',
    method: 'get'
  })
}

/**
 * 新增角色
 */
export function addRole(data: RoleDTO) {
  return request<any, Result<void>>({
    url: '/sys/role',
    method: 'post',
    data
  })
}

/**
 * 修改角色
 */
export function updateRole(id: string, data: RoleDTO) {
  return request<any, Result<void>>({
    url: `/sys/role/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除角色
 */
export function deleteRole(id: string) {
  return request<any, Result<void>>({
    url: `/sys/role/${id}`,
    method: 'delete'
  })
}

/**
 * 启用/停用角色
 */
export function toggleRoleStatus(id: string) {
  return request<any, Result<void>>({
    url: `/sys/role/${id}/status`,
    method: 'patch'
  })
}

/**
 * 为角色分配菜单
 */
export function assignMenus(roleId: string, menuIds: string[]) {
  return request<any, Result<void>>({
    url: `/sys/role/${roleId}/menus`,
    method: 'post',
    data: { menuIds }
  })
}

/**
 * 获取角色的菜单权限
 */
export function getRoleMenus(roleId: string) {
  return request<any, Result<string[]>>({
    url: `/sys/role/${roleId}/menus`,
    method: 'get'
  })
}
