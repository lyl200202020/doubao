<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>机构管理</span>
          <el-button v-permission="['sys:org:add']" type="primary" @click="handleAdd">
            新增机构
          </el-button>
        </div>
      </template>

      <el-table
        :data="orgTree"
        row-key="orgId"
        :tree-props="{ children: 'children' }"
        border
        style="width: 100%"
      >
        <el-table-column prop="orgCode" label="机构代码" width="150" />
        <el-table-column prop="orgName" label="机构名称" width="200" />
        <el-table-column prop="orgLevel" label="机构级别" width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelTagType(row.orgLevel)">
              {{ getLevelText(row.orgLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orgStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.orgStatus === 1 ? 'success' : 'danger'">
              {{ row.orgStatus === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" fixed="right" width="280">
          <template #default="{ row }">
            <el-button
              v-permission="['sys:org:edit']"
              type="primary"
              link
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
              v-permission="['sys:org:edit']"
              type="warning"
              link
              @click="handleToggleStatus(row)"
            >
              {{ row.orgStatus === 1 ? '停用' : '启用' }}
            </el-button>
            <el-button
              v-permission="['sys:org:delete']"
              type="danger"
              link
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 机构表单弹窗 -->
    <OrgForm
      v-model="formVisible"
      :form-data="formData"
      :org-tree="orgTree"
      @submit="handleSubmit"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrgTree, addOrg, updateOrg, deleteOrg, toggleOrgStatus } from '@/api/sys/org'
import type { Org, OrgDTO } from '@/types/org'
import OrgForm from './components/OrgForm.vue'

const orgTree = ref<Org[]>([])
const formVisible = ref(false)
const formData = ref<OrgDTO | null>(null)

onMounted(() => {
  loadOrgTree()
})

async function loadOrgTree() {
  const res = await getOrgTree()
  orgTree.value = res.data
}

function handleAdd() {
  formData.value = null
  formVisible.value = true
}

function handleEdit(row: Org) {
  formData.value = { ...row }
  formVisible.value = true
}

async function handleToggleStatus(row: Org) {
  const action = row.orgStatus === 1 ? '停用' : '启用'
  await ElMessageBox.confirm(`确定要${action}该机构吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  await toggleOrgStatus(row.orgId)
  ElMessage.success(`${action}成功`)
  await loadOrgTree()
}

async function handleDelete(row: Org) {
  await ElMessageBox.confirm(`确定要删除"${row.orgName}"吗？`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  await deleteOrg(row.orgId)
  ElMessage.success('删除成功')
  await loadOrgTree()
}

async function handleSubmit() {
  try {
    if (formData.value?.orgId) {
      // 编辑
      await updateOrg(formData.value.orgId, formData.value)
      ElMessage.success('修改成功')
    } else {
      // 新增
      await addOrg(formData.value as OrgDTO)
      ElMessage.success('新增成功')
    }
    formVisible.value = false
    await loadOrgTree()
  } catch (error) {
    console.error('提交失败:', error)
  }
}

function getLevelText(level: number): string {
  const levelMap: Record<number, string> = {
    0: '总行',
    1: '分行',
    2: '支行',
    3: '分理处'
  }
  return levelMap[level] || `L${level}`
}

function getLevelTagType(level: number): string {
  const typeMap: Record<number, string> = {
    0: 'danger',
    1: 'warning',
    2: 'success',
    3: 'info'
  }
  return typeMap[level] || ''
}
</script>

<style scoped lang="scss">
.page-container {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>
