<template>
  <el-dialog
    v-model="dialogVisible"
    title="分配菜单权限"
    width="600px"
    :close-on-click-modal="false"
    @closed="handleClosed"
  >
    <el-alert
      title="提示"
      type="info"
      :closable="false"
      style="margin-bottom: 20px"
    >
      <template #default>
        选择菜单后，该角色将拥有对应菜单的访问权限
      </template>
    </el-alert>

    <el-tree
      ref="treeRef"
      :data="menuTree"
      :props="treeProps"
      :default-checked-keys="checkedKeys"
      show-checkbox
      node-key="menuId"
      default-expand-all
      :check-strictly="false"
    />

    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="loading" @click="handleSubmit">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { ElTree, ElDialog, ElButton, ElAlert, type TreeInstance } from 'element-plus'
import { getMenus } from '@/api/sys/menu'
import { getRoleMenus, assignMenus } from '@/api/sys/role'
import type { Menu } from '@/types/menu'

interface Props {
  modelValue: boolean
  roleId: string | null
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
  (e: 'success'): void
}

const props = withDefaults(defineProps<Props>(), {
  roleId: null
})

const emit = defineEmits<Emits>()

const treeRef = ref<TreeInstance>()
const loading = ref(false)
const menuTree = ref<Menu[]>([])
const checkedKeys = ref<string[]>([])

const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => { emit('update:modelValue', value) }
})

const treeProps = {
  label: 'menuName',
  children: 'children'
}

const loadMenuTree = async () => {
  const res = await getMenus()
  menuTree.value = res.data
}

const loadRoleMenus = async (roleId: string) => {
  const res = await getRoleMenus(roleId)
  checkedKeys.value = res.data
}

watch(() => props.modelValue, async (visible) => {
  if (visible && props.roleId) {
    await Promise.all([
      loadMenuTree(),
      loadRoleMenus(props.roleId)
    ])
  }
})

const handleSubmit = async () => {
  if (!props.roleId || !treeRef.value) return

  loading.value = true
  try {
    const checkedMenuIds = treeRef.value.getCheckedKeys() as string[]
    const halfCheckedKeys = treeRef.value.getHalfCheckedKeys() as string[]
    const allMenuIds = [...checkedMenuIds, ...halfCheckedKeys]

    await assignMenus(props.roleId, allMenuIds)
    emit('success')
    dialogVisible.value = false
  } finally {
    loading.value = false
  }
}

const handleClosed = () => {
  checkedKeys.value = []
}
</script>

<style scoped>
</style>
