<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <div>
            <el-button v-permission="['sys:user:add']" type="primary" @click="handleAdd">
              新增用户
            </el-button>
          </div>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="所属机构">
          <el-tree-select
            v-model="searchForm.orgId"
            :data="orgTree"
            placeholder="请选择所属机构"
            clearable
            value-key="orgId"
            :props="{ label: 'orgName', children: 'children', value: 'orgId' }"
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.userStatus" placeholder="全部" clearable style="width: 100px">
            <el-option label="启用" :value="1" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table
        :data="tableData"
        v-loading="loading"
        border
        style="width: 100%"
        :empty-text="tableData.length === 0 ? '暂无数据' : ''"
      >
        <el-table-column prop="userCode" label="用户工号" width="150" />
        <el-table-column prop="userName" label="用户名" width="150" />
        <el-table-column prop="orgName" label="所属机构" min-width="200" />
        <el-table-column prop="authLevel" label="授权级别" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.authLevel }}级</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="userStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.userStatus === 1 ? 'success' : 'danger'">
              {{ row.userStatus === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="300">
          <template #default="{ row }">
            <el-button
              v-permission="['sys:user:edit']"
              type="primary"
              link
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
              v-permission="['sys:user:edit']"
              type="warning"
              link
              @click="handleToggleStatus(row)"
            >
              {{ row.userStatus === 1 ? '停用' : '启用' }}
            </el-button>
            <el-button
              v-permission="['sys:user:resetPwd']"
              type="warning"
              link
              @click="handleResetPassword(row)"
            >
              重置密码
            </el-button>
            <el-button
              v-permission="['sys:user:delete']"
              type="danger"
              link
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
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

    <!-- 用户表单弹窗 -->
    <UserForm
      v-model="formVisible"
      :form-data="formData"
      :org-tree="orgTree"
      :role-options="roleOptions"
      @submit="handleSubmit"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, addUser, updateUser, deleteUser, resetPassword, toggleUserStatus } from '@/api/sys/user'
import { getOrgTree } from '@/api/sys/org'
import { getRoleOptions } from '@/api/sys/role'
import type { User, UserDTO } from '@/types/user'
import type { Org } from '@/types/org'
import type { Role } from '@/types/role'
import UserForm from './components/UserForm.vue'

const searchForm = ref({
  username: '',
  orgId: '',
  userStatus: undefined as number | undefined
})

const tableData = ref<User[]>([])
const orgTree = ref<Org[]>([])
const roleOptions = ref<Role[]>([])
const loading = ref(false)
const formVisible = ref(false)
const formData = ref<UserDTO | null>(null)

const pagination = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

onMounted(() => {
  loadData()
  loadOrgTree()
  loadRoleOptions()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getUserList({
      pageNum: pagination.value.pageNum,
      pageSize: pagination.value.pageSize,
      userName: searchForm.value.username,
      orgId: searchForm.value.orgId,
      userStatus: searchForm.value.userStatus
    })
    tableData.value = res.data.records
    pagination.value.total = res.data.total
  } finally {
    loading.value = false
  }
}

async function loadOrgTree() {
  const res = await getOrgTree()
  orgTree.value = res.data
}

async function loadRoleOptions() {
  const res = await getRoleOptions()
  roleOptions.value = res.data
}

function handleSearch() {
  pagination.value.pageNum = 1
  loadData()
}

function handleReset() {
  searchForm.value = {
    username: '',
    orgId: '',
    userStatus: undefined
  }
  pagination.value.pageNum = 1
  loadData()
}

function handleAdd() {
  formData.value = null
  formVisible.value = true
}

function handleEdit(row: User) {
  formData.value = { ...row }
  formVisible.value = true
}

async function handleToggleStatus(row: User) {
  const action = row.userStatus === 1 ? '停用' : '启用'
  await ElMessageBox.confirm(`确定要${action}该用户吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  await toggleUserStatus(row.userId)
  ElMessage.success(`${action}成功`)
  await loadData()
}

async function handleResetPassword(row: User) {
  try {
    await ElMessageBox.confirm(`确定要重置"${row.userName}"的密码吗？`, '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await resetPassword(row.userId)
    ElMessage.success('密码已重置为默认密码')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('重置密码失败:', error)
    }
  }
}

async function handleDelete(row: User) {
  try {
    await ElMessageBox.confirm(`确定要删除"${row.userName}"吗？`, '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteUser(row.userId)
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
    if (formData.value?.userId) {
      // 编辑
      await updateUser(formData.value.userId, formData.value)
      ElMessage.success('修改成功')
    } else {
      // 新增
      await addUser(formData.value as UserDTO)
      ElMessage.success('新增成功')
    }
    formVisible.value = false
    await loadData()
  } catch (error) {
    console.error('提交失败:', error)
  }
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
