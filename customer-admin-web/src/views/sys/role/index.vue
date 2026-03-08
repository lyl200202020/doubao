<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <div>
            <el-button v-permission="['sys:role:add']" type="primary" @click="handleAdd">
              新增角色
            </el-button>
          </div>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="角色名称">
          <el-input v-model="searchForm.roleName" placeholder="请输入角色名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.roleStatus" placeholder="全部" clearable style="width: 100px">
            <el-option label="启用" :value="1" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table
        :data="tableData"
        v-loading="loading"
        border
        style="width: 100%"
        :empty-text="tableData.length === 0 ? '暂无数据' : ''"
      >
        <el-table-column prop="roleCode" label="角色代码" width="150" />
        <el-table-column prop="roleName" label="角色名称" width="150" />
        <el-table-column prop="roleDesc" label="角色描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="roleStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.roleStatus === 1 ? 'success' : 'danger'">
              {{ row.roleStatus === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="300">
          <template #default="{ row }">
            <el-button
              v-permission="['sys:role:edit']"
              type="primary"
              link
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
              v-permission="['sys:role:edit']"
              type="primary"
              link
              @click="handleAssignMenu(row)"
            >
              分配菜单
            </el-button>
            <el-button
              v-permission="['sys:role:edit']"
              type="warning"
              link
              @click="handleToggleStatus(row)"
            >
              {{ row.roleStatus === 1 ? '停用' : '启用' }}
            </el-button>
            <el-button
              v-permission="['sys:role:delete']"
              type="danger"
              link
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </el-card>

    <RoleForm
      v-model="formVisible"
      :form-data="formData"
      @submit="handleSubmit"
    />

    <RoleMenu
      v-model="menuVisible"
      :role-id="currentRoleId"
      @success="handleMenuSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRoleList, addRole, updateRole, deleteRole, toggleRoleStatus } from '@/api/sys/role'
import type { Role, RoleDTO } from '@/types/role'
import RoleForm from './components/RoleForm.vue'
import RoleMenu from './components/RoleMenu.vue'

const searchForm = ref({
  roleName: '',
  roleStatus: undefined as number | undefined
})

const tableData = ref<Role[]>([])
const loading = ref(false)
const formVisible = ref(false)
const menuVisible = ref(false)
const formData = ref<RoleDTO | null>(null)
const currentRoleId = ref<string | null>(null)

const pagination = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getRoleList({
      pageNum: pagination.value.pageNum,
      pageSize: pagination.value.pageSize,
      roleName: searchForm.value.roleName,
      roleStatus: searchForm.value.roleStatus
    })
    tableData.value = res.data.list || []
    pagination.value.total = res.data.total
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.value.pageNum = 1
  loadData()
}

function handleReset() {
  searchForm.value = {
    roleName: '',
    roleStatus: undefined
  }
  pagination.value.pageNum = 1
  loadData()
}

function handleAdd() {
  formData.value = null
  formVisible.value = true
}

function handleEdit(row: Role) {
  formData.value = { ...row }
  formVisible.value = true
}

function handleAssignMenu(row: Role) {
  currentRoleId.value = row.roleId
  menuVisible.value = true
}

async function handleToggleStatus(row: Role) {
  const action = row.roleStatus === 1 ? '停用' : '启用'
  await ElMessageBox.confirm(`确定要${action}该角色吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  await toggleRoleStatus(row.roleId)
  ElMessage.success(`${action}成功`)
  await loadData()
}

async function handleDelete(row: Role) {
  try {
    await ElMessageBox.confirm(`确定要删除"${row.roleName}"吗？`, '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteRole(row.roleId)
    ElMessage.success('删除成功')
    await loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

async function handleSubmit() {
  try {
    if (formData.value?.roleId) {
      await updateRole(formData.value.roleId, formData.value)
      ElMessage.success('修改成功')
    } else {
      await addRole(formData.value as RoleDTO)
      ElMessage.success('新增成功')
    }
    formVisible.value = false
    await loadData()
  } catch (error) {
    console.error('提交失败:', error)
  }
}

function handleMenuSuccess() {
  ElMessage.success('菜单分配成功')
}
</script>

<style scoped lang="scss">
.page-container {
  height: 100%;
  display: flex;
  flex-direction: column;

  :deep(.el-card) {
    flex: 1;
    display: flex;
    flex-direction: column;
    height: 100%;
  }

  :deep(.el-card__body) {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding: 20px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .search-form {
    margin: 0 0 20px 0;
  }

  :deep(.el-table) {
    flex: 1;
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
