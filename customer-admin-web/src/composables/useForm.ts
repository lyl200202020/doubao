import { ref, reactive, computed, Ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'

interface FormConfig {
  labelWidth?: string | number
  labelPosition?: 'left' | 'right' | 'top'
  rules?: FormRules
}

export function useForm<T extends Record<string, any>>(
  defaultData?: Partial<T>,
  config?: FormConfig
) {
  const formRef = ref<FormInstance>()
  const loading = ref(false)

  const formData = reactive<Partial<T>>(defaultData || ({} as Partial<T>))

  const resetFields = () => {
    formRef.value?.resetFields()
  }

  const clearValidate = (props?: string | string[]) => {
    formRef.value?.clearValidate(props)
  }

  const validate = async () => {
    if (!formRef.value) return false
    try {
      await formRef.value.validate()
      return true
    } catch {
      return false
    }
  }

  const setFieldValue = (field: keyof T, value: any) => {
    formData[field] = value
  }

  const getFieldValue = (field: keyof T) => {
    return formData[field]
  }

  const setValues = (values: Partial<T>) => {
    Object.assign(formData, values)
  }

  const getValues = () => {
    return { ...formData }
  }

  return {
    formRef,
    formData,
    loading,
    resetFields,
    clearValidate,
    validate,
    setFieldValue,
    getFieldValue,
    setValues,
    getValues
  }
}
