<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? '编辑机构' : '新增机构'"
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
      <el-form-item label="上级机构" prop="parentOrgId">
        <el-tree-select
          v-model="formData.parentOrgId"
          :data="orgTree"
          check-strictly
          placeholder="请选择上级机构"
          :render-after-expand="false"
          value-key="orgId"
          :props="{ label: 'orgName', children: 'children', value: 'orgId' }"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="机构代码" prop="orgCode">
        <el-input v-model="formData.orgCode" placeholder="请输入机构代码" />
      </el-form-item>
      <el-form-item label="机构名称" prop="orgName">
        <el-input v-model="formData.orgName" placeholder="请输入机构名称" />
      </el-form-item>
      <el-form-item label="排序号" prop="sortOrder">
        <el-input-number v-model="formData.sortOrder" :min="0" :max="9999" style="width: 100%" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注"
        />
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
import { ElForm, ElFormItem, ElInput, ElInputNumber, ElTreeSelect, ElDialog, ElButton, type FormInstance, type FormRules } from 'element-plus'
import type { Org, OrgDTO } from '@/types/org'

interface Props {
  modelValue: boolean
  formData: OrgDTO | null
  orgTree: Org[]
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

const isEdit = computed(() => !!props.formData?.orgId)

const formData = ref<OrgDTO>({
  orgCode: '',
  orgName: '',
  parentOrgId: undefined,
  sortOrder: 0,
  remark: ''
})

const formRules: FormRules = {
  orgCode: [
    { required: true, message: '请输入机构代码', trigger: 'blur' },
    { min: 2, max: 32, message: '长度在 2 到 32 个字符', trigger: 'blur' }
  ],
  orgName: [
    { required: true, message: '请输入机构名称', trigger: 'blur' },
    { min: 2, max: 128, message: '长度在 2 到 128 个字符', trigger: 'blur' }
  ],
  sortOrder: [
    { required: true, message: '请输入排序号', trigger: 'blur' }
  ]
}

watch(() => props.formData, (newVal) => {
  if (newVal) {
    formData.value = { ...newVal }
  } else {
    resetForm()
  }
}, { immediate: true })

function resetForm() {
  formData.value = {
    orgCode: '',
    orgName: '',
    parentOrgId: undefined,
    sortOrder: 0,
    remark: ''
  }
  formRef.value?.clearValidate()
}

function handleClosed() {
  resetForm()
}

async function handleSubmit() {
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

defineExpose({
  formRef
})
</script>
