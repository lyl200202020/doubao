import { ref, computed, Ref } from 'vue'

interface DialogOptions {
  title?: string
  width?: string | number
  destroyOnClose?: boolean
  closeOnClickModal?: boolean
  showClose?: boolean
}

export function useDialog(options?: DialogOptions) {
  const visible = ref(false)
  const loading = ref(false)

  const dialogOptions = ref<DialogOptions>({
    title: '',
    width: '50%',
    destroyOnClose: true,
    closeOnClickModal: false,
    showClose: true,
    ...options
  })

  const title = computed(() => dialogOptions.value.title)

  const open = (data?: any) => {
    visible.value = true
    return data
  }

  const close = () => {
    visible.value = false
  }

  const setLoading = (val: boolean) => {
    loading.value = val
  }

  const setTitle = (val: string) => {
    dialogOptions.value.title = val
  }

  return {
    visible,
    loading,
    dialogOptions,
    title,
    open,
    close,
    setLoading,
    setTitle
  }
}
