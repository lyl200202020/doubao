/**
 * 用户类型
 */
export interface User {
  userId: string
  userCode: string
  userName: string
  orgId: string
  orgName: string
  authLevel: number
  userStatus: number
  createTime: string
}

/**
 * 用户表单 DTO
 */
export interface UserDTO {
  userId?: string
  userCode: string
  userName: string
  orgId: string
  authLevel: number
  userStatus?: number
}

/**
 * 用户信息（登录返回）
 */
export interface UserInfo {
  userId: string
  username: string
  orgId: string
  orgName: string
  authLevel: number
  permissions: string[]
}

import type { Menu } from './menu'
