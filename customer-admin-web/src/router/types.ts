import { RouteRecordRaw } from 'vue-router'

export interface RouteMeta {
  title: string
  icon?: string
  hidden?: boolean
  keepAlive?: boolean
  permission?: string[]
  roles?: string[]
  affix?: boolean
  breadcrumb?: boolean
}

declare module 'vue-router' {
  interface RouteMeta extends Record<string, any> {
    title: string
    icon?: string
    hidden?: boolean
    keepAlive?: boolean
    permission?: string[]
    roles?: string[]
    affix?: boolean
    breadcrumb?: boolean
  }
}
