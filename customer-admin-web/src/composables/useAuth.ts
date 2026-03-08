import { ref, computed, Ref } from 'vue'
import { useUserStore } from '@/store/modules/user'
import { usePermissionStore } from '@/store/modules/permission'

export function useAuth() {
  const userStore = useUserStore()
  const permissionStore = usePermissionStore()

  const permissions = computed(() => userStore.userInfo?.permissions || [])
  const userCode = computed(() => userStore.userInfo?.userCode || '')
  const userName = computed(() => userStore.userInfo?.userName || '')
  const authLevel = computed(() => userStore.userInfo?.authLevel || 0)

  const hasPermission = (permission: string): boolean => {
    return permissions.value.includes(permission)
  }

  const hasAnyPermission = (permissionList: string[]): boolean => {
    return permissionList.some((p) => permissions.value.includes(p))
  }

  const hasAllPermissions = (permissionList: string[]): boolean => {
    return permissionList.every((p) => permissions.value.includes(p))
  }

  const isAdmin = computed(() => userCode.value === 'admin')

  return {
    permissions,
    userCode,
    userName,
    authLevel,
    hasPermission,
    hasAnyPermission,
    hasAllPermissions,
    isAdmin
  }
}
