/**
 * 机构类型
 */
export interface Org {
  orgId: string
  orgCode: string
  orgName: string
  orgLevel: number
  parentOrgId: string | null
  orgStatus: number
  sortOrder: number
  remark?: string
  children?: Org[]
}

/**
 * 机构表单 DTO
 */
export interface OrgDTO {
  orgId?: string
  orgCode: string
  orgName: string
  parentOrgId?: string
  sortOrder?: number
  remark?: string
}
