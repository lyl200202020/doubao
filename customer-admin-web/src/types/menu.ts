/**
 * 菜单类型
 */
export interface Menu {
  menuId: string
  menuName: string
  menuType: 1 | 2 | 3 // 1-目录，2-菜单，3-按钮
  parentMenuId?: string
  menuUrl?: string
  menuIcon?: string
  perms?: string
  sortOrder: number
  visible: 0 | 1
  hidden?: boolean
  children?: Menu[]
  noShowingChildren?: boolean
}

/**
 * 菜单表单 DTO
 */
export interface MenuDTO {
  menuName: string
  menuType: 1 | 2 | 3
  parentMenuId?: string
  menuUrl?: string
  menuIcon?: string
  perms?: string
  sortOrder: number
  visible: 0 | 1
}
