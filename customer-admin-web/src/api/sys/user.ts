import request from '@/utils/request'
import type { Result } from '@/utils/request'
import type { User, UserDTO } from '@/types/user'

export interface UserListParams {
  pageNum: number
  pageSize: number
  userName?: string
  orgId?: string
  userStatus?: number
}

export interface UserListResult {
  records: User[]
  total: number
}

/**
 * 获取用户列表
 */
export function getUserList(params: UserListParams) {
  return request<any, Result<UserListResult>>({
    url: '/sys/user/list',
    method: 'get',
    params
  })
}

/**
 * 获取用户详情
 */
export function getUserDetail(id: string) {
  return request<any, Result<User>>({
    url: `/sys/user/${id}`,
    method: 'get'
  })
}

/**
 * 新增用户
 */
export function addUser(data: UserDTO) {
  return request<any, Result<void>>({
    url: '/sys/user',
    method: 'post',
    data
  })
}

/**
 * 修改用户
 */
export function updateUser(id: string, data: UserDTO) {
  return request<any, Result<void>>({
    url: `/sys/user/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除用户
 */
export function deleteUser(id: string) {
  return request<any, Result<void>>({
    url: `/sys/user/${id}`,
    method: 'delete'
  })
}

/**
 * 重置用户密码
 */
export function resetPassword(id: string) {
  return request<any, Result<void>>({
    url: `/sys/user/${id}/reset-pwd`,
    method: 'post'
  })
}

/**
 * 启用/停用用户
 */
export function toggleUserStatus(id: string) {
  return request<any, Result<void>>({
    url: `/sys/user/${id}/status`,
    method: 'patch'
  })
}
