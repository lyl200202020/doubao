<template>
  <el-dialog
    v-model="dialogVisible"
    :title="formData?.menuId ? '编辑菜单' : '新增菜单'"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="100px"
    >
      <el-form-item label="上级菜单" prop="parentMenuId">
        <el-tree-select
          v-model="form.parentMenuId"
          :data="menuTree"
          placeholder="请选择上级菜单"
          clearable
          value-key="menuId"
          :props="{ label: 'menuName', children: 'children', value: 'menuId' }"
          style="width: 100%"
        />
      </el-form-item>
      
      <el-form-item label="菜单类型" prop="menuType">
        <el-radio-group v-model="form.menuType">
          <el-radio :label="1">目录</el-radio>
          <el-radio :label="2">菜单</el-radio>
          <el-radio :label="3">按钮</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="菜单名称" prop="menuName">
        <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
      </el-form-item>
      
      <el-form-item label="路由地址" prop="menuUrl" v-if="form.menuType === 2">
        <el-input v-model="form.menuUrl" placeholder="请输入路由地址" />
      </el-form-item>
      
      <el-form-item label="权限标识" prop="perms" v-if="form.menuType === 3">
        <el-input v-model="form.perms" placeholder="请输入权限标识，如：sys:user:add" />
      </el-form-item>
      
      <el-form-item label="菜单图标" prop="menuIcon" v-if="form.menuType !== 3">
        <el-input v-model="form.menuIcon" placeholder="请输入图标名称" />
      </el-form-item>
      
      <el-form-item label="显示排序" prop="sortOrder">
        <el-input-number v-model="form.sortOrder" :min="0" :max="999" style="width: 100%" />
      </el-form-item>
      
      <el-form-item label="菜单状态" prop="visible">
        <el-radio-group v-model="form.visible">
          <el-radio :label="1">显示</el-radio>
          <el-radio :label="0">隐藏</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { Menu, MenuDTO } from '@/types/menu'

interface Props {
  modelValue: boolean
  formData: MenuDTO | null
  menuTree: Menu[]
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
  (e: 'submit'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const formRef = ref<FormInstance>()
const loading = ref(false)

const form = ref<MenuDTO>({
  menuName: '',
  menuType: 1,
  sortOrder: 0,
  visible: 1
})

const rules: FormRules = {
  menuName: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' }
  ],
  menuType: [
    { required: true, message: '请选择菜单类型', trigger: 'change' }
  ],
  menuUrl: [
    { required: true, message: '请输入路由地址', trigger: 'blur' }
  ],
  sortOrder: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ]
}

const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

watch(() => props.formData, (newVal) => {
  if (newVal) {
    form.value = { ...newVal }
  } else {
    form.value = {
      menuName: '',
      menuType: 1,
      sortOrder: 0,
      visible: 1
    }
  }
}, { immediate: true })

function handleClose() {
  dialogVisible.value = false
  formRef.value?.resetFields()
}

async function handleSubmit() {
  if (!formRef.value) return
  
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  
  Object.assign(props.formData || {}, form.value)
  emit('submit')
}
</script>

<style scoped lang="scss">
:deep(.el-dialog__body) {
  padding: 20px;
}
</style>
