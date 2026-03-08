import request from '@/utils/request'
import type { Result } from '@/utils/request'
import type { Org, OrgDTO } from '@/types/org'

/**
 * 获取机构树
 */
export function getOrgTree() {
  return request<any, Result<Org[]>>({
    url: '/sys/org/tree',
    method: 'get'
  })
}

/**
 * 获取机构详情
 */
export function getOrgDetail(id: string) {
  return request<any, Result<Org>>({
    url: `/sys/org/${id}`,
    method: 'get'
  })
}

/**
 * 新增机构
 */
export function addOrg(data: OrgDTO) {
  return request<any, Result<void>>({
    url: '/sys/org',
    method: 'post',
    data
  })
}

/**
 * 修改机构
 */
export function updateOrg(id: string, data: OrgDTO) {
  return request<any, Result<void>>({
    url: `/sys/org/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除机构
 */
export function deleteOrg(id: string) {
  return request<any, Result<void>>({
    url: `/sys/org/${id}`,
    method: 'delete'
  })
}

/**
 * 启用/停用机构
 */
export function toggleOrgStatus(id: string) {
  return request<any, Result<void>>({
    url: `/sys/org/${id}/status`,
    method: 'patch'
  })
}
