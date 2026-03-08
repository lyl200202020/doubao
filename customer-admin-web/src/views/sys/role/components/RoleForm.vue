<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? '编辑角色' : '新增角色'"
    width="600px"
    :close-on-click-modal="false"
    @closed="handleClosed"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
    >
      <el-form-item label="角色代码" prop="roleCode">
        <el-input v-model="formData.roleCode" placeholder="请输入角色代码" :disabled="isEdit" />
      </el-form-item>
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="formData.roleName" placeholder="请输入角色名称" />
      </el-form-item>
      <el-form-item label="角色描述" prop="roleDesc">
        <el-input
          v-model="formData.roleDesc"
          type="textarea"
          :rows="3"
          placeholder="请输入角色描述"
        />
      </el-form-item>
      <el-form-item label="状态" prop="roleStatus">
        <el-switch v-model="formData.roleStatus" :active-value="1" :inactive-value="0" />
      </el-form-item>
    </el-form>
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
import { ElForm, ElFormItem, ElInput, ElDialog, ElButton, ElSwitch, type FormInstance, type FormRules } from 'element-plus'
import type { Role, RoleDTO } from '@/types/role'

interface Props {
  modelValue: boolean
  formData: RoleDTO | null
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
  (e: 'submit'): void
}

const props = withDefaults(defineProps<Props>(), {
  formData: null
})

const emit = defineEmits<Emits>()

const formRef = ref<FormInstance>()
const loading = ref(false)

const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const isEdit = computed(() => !!props.formData?.roleId)

const formData = ref<RoleDTO>({
  roleId: undefined,
  roleCode: '',
  roleName: '',
  roleDesc: '',
  roleStatus: 1
})

const formRules: FormRules = {
  roleCode: [
    { required: true, message: '请输入角色代码', trigger: 'blur' },
    { min: 2, max: 32, message: '长度在 2 到 32 个字符', trigger: 'blur' }
  ],
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 64, message: '长度在 2 到 64 个字符', trigger: 'blur' }
  ]
}

const resetForm = () => {
  formData.value = {
    roleId: undefined,
    roleCode: '',
    roleName: '',
    roleDesc: '',
    roleStatus: 1
  }
}

watch(() => props.formData, (newVal) => {
  if (newVal) {
    formData.value = { ...newVal }
  } else {
    resetForm()
  }
}, { immediate: true, deep: true })

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        emit('submit')
      } finally {
        loading.value = false
      }
    }
  })
}

const handleClosed = () => {
  formRef.value?.resetFields()
}
</script>

<style scoped>
</style>
