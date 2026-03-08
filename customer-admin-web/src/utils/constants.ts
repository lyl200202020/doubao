export const AUTH_LEVEL = {
  ORG: 1,
  BRANCH: 2,
  REGION: 3,
  HEADQUARTERS: 16
} as const

export const AUTH_LEVEL_TEXT: Record<number, string> = {
  1: '网点',
  2: '支行',
  3: '区域',
  16: '总行'
}

export const USER_STATUS = {
  NORMAL: 0,
  DISABLED: 1
} as const

export const USER_STATUS_TEXT: Record<number, string> = {
  0: '正常',
  1: '禁用'
}

export const PAGE_SIZE = 10
export const PAGE_SIZE_OPTIONS = [10, 20, 50, 100]
