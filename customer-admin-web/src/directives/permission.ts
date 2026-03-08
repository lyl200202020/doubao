import type { Directive, DirectiveBinding } from 'vue'
import { usePermissionStore } from '@/store/modules/permission'

interface PermissionDirectiveBinding {
  value: string | string[] | undefined
  oldValue: string | string[] | undefined
}

export const permission: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding<PermissionDirectiveBinding>) {
    const { value } = binding
    const permissionStore = usePermissionStore()

    if (value) {
      const permissions = Array.isArray(value) ? value : [value]
      const hasPermission = permissions.some((p) => permissionStore.hasPermission(p))

      if (!hasPermission) {
        el.parentNode?.removeChild(el)
      }
    }
  },
  updated(el: HTMLElement, binding: DirectiveBinding<PermissionDirectiveBinding>) {
    const { value, oldValue } = binding
    const permissionStore = usePermissionStore()

    if (value && value !== oldValue) {
      const permissions = Array.isArray(value) ? value : [value]
      const hasPermission = permissions.some((p) => permissionStore.hasPermission(p))

      if (!hasPermission) {
        el.parentNode?.removeChild(el)
      }
    }
  }
}

export default permission
