import request from '@/utils/request'
import { Result } from '@/utils/request'
import type { Menu } from '@/types/menu'

export interface LoginParams {
  userCode: string
  password: string
}

export interface LoginResult {
  token: string
  userId: string
  userCode: string
  userName: string
  orgId: string
  orgName: string
  authLevel: number
  menus: Menu[]
  permissions: string[]
}

export function login(data: LoginParams): Promise<Result<LoginResult>> {
  return request.post<Result<LoginResult>>('/auth/login', data)
}

export function logout(): Promise<Result<null>> {
  return request.post<Result<null>>('/auth/logout')
}

export function getCurrentUser(): Promise<Result<LoginResult>> {
  return request.get<Result<LoginResult>>('/auth/current')
}
