import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login, logout, getCurrentUser } from '@/api/auth'
import { getMenus } from '@/api/sys/menu'
import type { UserInfo as AuthUserInfo } from '@/utils/auth'
import type { Menu } from '@/types/menu'
import { setToken, removeToken, getToken } from '@/utils/auth'

export interface UserInfo extends AuthUserInfo {
  menus: Menu[]
  permissions: string[]
}

export const useUserStore = defineStore('user', () => {
  const token = ref<string>('')
  const userInfo = ref<UserInfo | null>(null)
  const roles = ref<string[]>([])
  const permissions = ref<string[]>([])

  function setTokenValue(newToken: string) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  function setUserInfoValue(info: UserInfo) {
    userInfo.value = info
    roles.value = [info.userCode]
    permissions.value = info.permissions || []
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  function setPermissions(perms: string[]) {
    permissions.value = perms
  }

  async function loginAction(loginForm: { userCode: string; password: string }) {
    try {
      const res = await login(loginForm)
      const { token: newToken, userId, userCode, userName, orgId, orgName, authLevel, menus, permissions: perms } = res.data
      setTokenValue(newToken)
      
      const info: UserInfo = {
        userId,
        userCode,
        userName,
        orgId,
        orgName,
        authLevel,
        menus: menus || [],
        permissions: perms || []
      }
      setUserInfoValue(info)
      return Promise.resolve(res)
    } catch (error) {
      return Promise.reject(error)
    }
  }

  async function getInfo() {
    try {
      const res = await getCurrentUser()
      const { userId, userCode, userName, orgId, orgName, authLevel, menus, permissions: perms } = res.data
      
      const info: UserInfo = {
        userId,
        userCode,
        userName,
        orgId,
        orgName,
        authLevel,
        menus: menus || [],
        permissions: perms || []
      }
      
      userInfo.value = info
      roles.value = [userCode]
      permissions.value = perms || []
      
      return res
    } catch (error) {
      return Promise.reject(error)
    }
  }

  async function logoutAction() {
    try {
      await logout()
      resetToken()
      return Promise.resolve()
    } catch (error) {
      return Promise.reject(error)
    }
  }

  function resetToken() {
    token.value = ''
    userInfo.value = null
    roles.value = []
    permissions.value = []
    removeToken()
    localStorage.removeItem('userInfo')
  }

  function initFromStorage() {
    const storedToken = localStorage.getItem('token')
    const storedUserInfo = localStorage.getItem('userInfo')
    if (storedToken) {
      token.value = storedToken
    }
    if (storedUserInfo) {
      userInfo.value = JSON.parse(storedUserInfo)
      roles.value = [userInfo.value.userCode]
      permissions.value = userInfo.value.permissions || []
    }
  }

  return {
    token,
    userInfo,
    roles,
    permissions,
    setToken: setTokenValue,
    setUserInfo: setUserInfoValue,
    setPermissions,
    loginAction,
    getInfo,
    logoutAction,
    resetToken,
    initFromStorage
  }
})
