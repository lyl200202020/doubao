<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? '编辑用户' : '新增用户'"
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
      <el-form-item label="用户工号" prop="userCode">
        <el-input v-model="formData.userCode" placeholder="请输入用户工号" :disabled="isEdit" />
      </el-form-item>
      <el-form-item label="用户姓名" prop="userName">
        <el-input v-model="formData.userName" placeholder="请输入用户姓名" />
      </el-form-item>
      <el-form-item label="所属机构" prop="orgId">
        <el-tree-select
          v-model="formData.orgId"
          :data="orgTree"
          placeholder="请选择所属机构"
          :render-after-expand="false"
          value-key="orgId"
          :props="{ label: 'orgName', children: 'children', value: 'orgId' }"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="授权级别" prop="authLevel">
        <el-select v-model="formData.authLevel" placeholder="请选择授权级别" style="width: 100%">
          <el-option
            v-for="i in 16"
            :key="i"
            :label="`${i}级`"
            :value="i"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="userStatus">
        <el-switch v-model="formData.userStatus" :active-value="1" :inactive-value="0" />
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
import { ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElTreeSelect, ElDialog, ElButton, ElSwitch, type FormInstance, type FormRules } from 'element-plus'
import type { User, UserDTO } from '@/types/user'
import type { Org } from '@/types/org'
import type { Role } from '@/types/role'

interface Props {
  modelValue: boolean
  formData: UserDTO | null
  orgTree: Org[]
  roleOptions: Role[]
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

const isEdit = computed(() => !!props.formData?.userId)

const formData = ref<UserDTO>({
  userId: undefined,
  userCode: '',
  userName: '',
  password: '',
  orgId: '',
  authLevel: 1,
  userStatus: 1
})

const formRules: FormRules = {
  userCode: [
    { required: true, message: '请输入用户工号', trigger: 'blur' },
    { min: 4, max: 32, message: '长度在 4 到 32 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  userName: [
    { required: true, message: '请输入用户姓名', trigger: 'blur' },
    { min: 2, max: 64, message: '长度在 2 到 64 个字符', trigger: 'blur' }
  ],
  orgId: [
    { required: true, message: '请选择所属机构', trigger: 'change' }
  ],
  authLevel: [
    { required: true, message: '请选择授权级别', trigger: 'change' }
  ]
}

const resetForm = () => {
  formData.value = {
    userId: undefined,
    userCode: '',
    userName: '',
    password: '',
    orgId: '',
    authLevel: 1,
    userStatus: 1
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
