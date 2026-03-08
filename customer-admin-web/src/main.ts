import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import { setupRouterGuard } from './router/guard'
import { setupDirectives } from './directives'
import { useUserStore } from './store/modules/user'
import './assets/styles/index.scss'

const app = createApp(App)
const pinia = createPinia()

// 注册 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(pinia)
app.use(router)
app.use(ElementPlus)
setupDirectives(app)

// 初始化用户信息（从 localStorage）
const userStore = useUserStore()
userStore.initFromStorage()

setupRouterGuard(router)

app.mount('#app')
