export interface Role {
  roleId: string
  roleCode: string
  roleName: string
  roleDesc?: string
  roleStatus: number
}

export interface RoleDTO {
  roleId?: string
  roleCode: string
  roleName: string
  roleDesc?: string
  roleStatus?: number
}
