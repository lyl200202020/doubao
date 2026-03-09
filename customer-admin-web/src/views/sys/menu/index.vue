<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>菜单管理</span>
          <div>
            <el-button v-permission="['sys:menu:add']" type="primary" @click="handleAdd">
              新增菜单
            </el-button>
            <el-button v-permission="['sys:menu:expand']" @click="handleExpandAll">
              展开/折叠
            </el-button>
          </div>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="菜单名称">
          <el-input v-model="searchForm.menuName" placeholder="请输入菜单名称" clearable />
        </el-form-item>
        <el-form-item label="菜单类型">
          <el-select v-model="searchForm.menuType" placeholder="全部" clearable style="width: 120px">
            <el-option label="目录" :value="1" />
            <el-option label="菜单" :value="2" />
            <el-option label="按钮" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.visible" placeholder="全部" clearable style="width: 100px">
            <el-option label="显示" :value="1" />
            <el-option label="隐藏" :value="0" />
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
        row-key="menuId"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :default-expand-all="isExpandAll"
        :empty-text="tableData.length === 0 ? '暂无数据' : ''"
      >
        <el-table-column prop="menuName" label="菜单名称" min-width="200" />
        <el-table-column prop="menuType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getMenuTypeTag(row.menuType)">
              {{ getMenuTypeText(row.menuType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="menuIcon" label="图标" width="100">
          <template #default="{ row }">
            <el-icon v-if="row.menuIcon">
              <component :is="row.menuIcon" />
            </el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="menuUrl" label="路由地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="perms" label="权限标识" min-width="150" show-overflow-tooltip />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="visible" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.visible === 1 ? 'success' : 'info'">
              {{ row.visible === 1 ? '显示' : '隐藏' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="250">
          <template #default="{ row }">
            <el-button
              v-permission="['sys:menu:add']"
              type="primary"
              link
              @click="handleAddChild(row)"
            >
              新增下级
            </el-button>
            <el-button
              v-permission="['sys:menu:edit']"
              type="primary"
              link
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
              v-permission="['sys:menu:delete']"
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

    <MenuForm
      v-model="formVisible"
      :form-data="formData"
      :menu-tree="menuTree"
      @submit="handleSubmit"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMenus, addMenu, updateMenu, deleteMenu } from '@/api/sys/menu'
import type { Menu, MenuDTO } from '@/types/menu'
import MenuForm from './components/MenuForm.vue'

const searchForm = ref({
  menuName: '',
  menuType: undefined as number | undefined,
  visible: undefined as number | undefined
})

const tableData = ref<Menu[]>([])
const menuTree = ref<Menu[]>([])
const loading = ref(false)
const formVisible = ref(false)
const formData = ref<MenuDTO | null>(null)
const isExpandAll = ref(false)

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getMenus()
    menuTree.value = res.data
    tableData.value = filterMenus(res.data)
  } finally {
    loading.value = false
  }
}

function filterMenus(menus: Menu[]): Menu[] {
  let result = [...menus]
  
  if (searchForm.value.menuName) {
    result = filterByName(result, searchForm.value.menuName)
  }
  
  if (searchForm.value.menuType !== undefined) {
    result = filterByType(result, searchForm.value.menuType)
  }
  
  if (searchForm.value.visible !== undefined) {
    result = filterByVisible(result, searchForm.value.visible)
  }
  
  return result
}

function filterByName(menus: Menu[], name: string): Menu[] {
  const result: Menu[] = []
  for (const menu of menus) {
    if (menu.menuName.includes(name)) {
      result.push({ ...menu })
    } else if (menu.children && menu.children.length > 0) {
      const filteredChildren = filterByName(menu.children, name)
      if (filteredChildren.length > 0) {
        result.push({ ...menu, children: filteredChildren })
      }
    }
  }
  return result
}

function filterByType(menus: Menu[], type: number): Menu[] {
  const result: Menu[] = []
  for (const menu of menus) {
    if (menu.menuType === type) {
      result.push({ ...menu })
    } else if (menu.children && menu.children.length > 0) {
      const filteredChildren = filterByType(menu.children, type)
      if (filteredChildren.length > 0) {
        result.push({ ...menu, children: filteredChildren })
      }
    }
  }
  return result
}

function filterByVisible(menus: Menu[], visible: number): Menu[] {
  const result: Menu[] = []
  for (const menu of menus) {
    if (menu.visible === visible) {
      result.push({ ...menu })
    } else if (menu.children && menu.children.length > 0) {
      const filteredChildren = filterByVisible(menu.children, visible)
      if (filteredChildren.length > 0) {
        result.push({ ...menu, children: filteredChildren })
      }
    }
  }
  return result
}

function handleSearch() {
  loadData()
}

function handleReset() {
  searchForm.value = {
    menuName: '',
    menuType: undefined,
    visible: undefined

  }
  loadData()
}

function handleAdd() {
  formData.value = {
    menuName: '',
    menuType: 1,
    sortOrder: 0,
    visible: 1
  }
  formVisible.value = true
}

function handleAddChild(row: Menu) {
  formData.value = {
    menuName: '',
    menuType: row.menuType === 1 ? 2 : 3,
    parentMenuId: row.menuId,
    sortOrder: 0,
    visible: 1
  }
  formVisible.value = true
}

function handleEdit(row: Menu) {
  formData.value = { ...row }
  formVisible.value = true
}

function handleExpandAll() {
  isExpandAll.value = !isExpandAll.value
  loadData()
}

async function handleDelete(row: Menu) {
  if (row.children && row.children.length > 0) {
    ElMessage.warning('该菜单下存在子菜单，无法删除')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确定要删除"${row.menuName}"吗？`, '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteMenu(row.menuId)
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
    if (formData.value?.menuId) {
      await updateMenu(formData.value.menuId, formData.value)
      ElMessage.success('修改成功')
    } else {
      await addMenu(formData.value as MenuDTO)
      ElMessage.success('新增成功')
    }
    formVisible.value = false
    await loadData()
  } catch (error) {
    console.error('提交失败:', error)
  }
}

function getMenuTypeText(type: number) {
  const map = {
    1: '目录',
    2: '菜单',
    3: '按钮'
  }
  return map[type as keyof typeof map] || '未知'
}

function getMenuTypeTag(type: number) {
  const map = {
    1: 'primary',
    2: 'success',
    3: 'warning'
  }
  return map[type as keyof typeof map] || 'info'
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
}
</style>
